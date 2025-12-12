package com.example.bt9;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.adapters.ChatMessageAdapter;
import com.example.bt9.models.ChatMessage;
import com.example.bt9.utils.SocketManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class ManagerChatActivity extends AppCompatActivity {

    private String managerId;
    private String managerName;
    private String customerId;
    private String customerName;
    private String roomId;

    private Socket socket;
    private RecyclerView messagesRecyclerView;
    private ChatMessageAdapter messageAdapter;
    private EditText messageInput;
    private Button sendButton;
    private Button endChatButton;
    private TextView customerNameText;
    private TextView statusText;
    private TextView typingIndicator;

    private Handler typingHandler = new Handler();
    private Runnable typingRunnable;
    private boolean isTyping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_chat);

        // Get data from intent
        roomId = getIntent().getStringExtra("roomId");
        customerId = getIntent().getStringExtra("customerId");
        customerName = getIntent().getStringExtra("customerName");
        managerId = getIntent().getStringExtra("managerId");
        managerName = getIntent().getStringExtra("managerName");

        if (roomId == null || customerId == null || managerId == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupSocket();
        setupListeners();
    }

    private void initViews() {
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        endChatButton = findViewById(R.id.endChatButton);
        customerNameText = findViewById(R.id.customerNameText);
        statusText = findViewById(R.id.statusText);
        typingIndicator = findViewById(R.id.typingIndicator);

        customerNameText.setText(customerName);

        getSupportActionBar().setTitle(R.string.manager_chat_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        messageAdapter = new ChatMessageAdapter(managerId, "manager");
        messagesRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupSocket() {
        socket = SocketManager.getInstance().getSocket();
        
        if (!socket.connected()) {
            SocketManager.getInstance().connect();
        }

        socket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
            joinRoom();
        }));

        socket.on("room:joined", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                statusText.setText(R.string.connected);

                // Load previous messages if any
                if (data.has("messages")) {
                    JsonArray messagesArray = new Gson().fromJson(data.getJSONArray("messages").toString(), JsonArray.class);
                    for (int i = 0; i < messagesArray.size(); i++) {
                        ChatMessage message = new Gson().fromJson(messagesArray.get(i), ChatMessage.class);
                        messageAdapter.addMessage(message);
                    }
                    messagesRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                }

                // Mark messages as read
                markMessagesAsRead();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on("message:received", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                ChatMessage message = new Gson().fromJson(data.toString(), ChatMessage.class);
                messageAdapter.addMessage(message);
                messagesRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

                // Mark as read
                markMessagesAsRead();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        socket.on("typing:user-typing", args -> runOnUiThread(() -> {
            typingIndicator.setVisibility(View.VISIBLE);
        }));

        socket.on("typing:user-stopped", args -> runOnUiThread(() -> {
            typingIndicator.setVisibility(View.GONE);
        }));

        socket.on("customer:disconnected", args -> runOnUiThread(() -> {
            statusText.setText(R.string.customer_disconnected);
            Toast.makeText(this, "Khách hàng đã ngắt kết nối", Toast.LENGTH_SHORT).show();
        }));

        socket.on("customer:reconnected", args -> runOnUiThread(() -> {
            statusText.setText(R.string.connected);
            Toast.makeText(this, "Khách hàng đã kết nối lại", Toast.LENGTH_SHORT).show();
        }));

        socket.on("chat:ended", args -> runOnUiThread(() -> {
            Toast.makeText(this, "Cuộc trò chuyện đã kết thúc", Toast.LENGTH_LONG).show();
            finish();
        }));

        socket.on(Socket.EVENT_DISCONNECT, args -> runOnUiThread(() -> {
            statusText.setText(R.string.disconnected);
        }));

        // If already connected, join room immediately
        if (socket.connected()) {
            joinRoom();
        }
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        endChatButton.setOnClickListener(v -> showEndChatDialog());

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !isTyping) {
                    isTyping = true;
                    emitTypingStart();
                }

                // Reset typing timeout
                if (typingRunnable != null) {
                    typingHandler.removeCallbacks(typingRunnable);
                }

                typingRunnable = () -> {
                    if (isTyping) {
                        isTyping = false;
                        emitTypingStop();
                    }
                };

                typingHandler.postDelayed(typingRunnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void joinRoom() {
        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            data.put("managerId", managerId);
            socket.emit("manager:join-room", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            data.put("message", message);
            data.put("senderId", managerId);
            data.put("senderName", managerName);
            data.put("senderType", "manager");

            socket.emit("message:send", data);
            messageInput.setText("");

            // Stop typing indicator
            if (isTyping) {
                isTyping = false;
                emitTypingStop();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi gửi tin nhắn", Toast.LENGTH_SHORT).show();
        }
    }

    private void emitTypingStart() {
        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            data.put("userName", managerName);
            data.put("userType", "manager");
            socket.emit("typing:start", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void emitTypingStop() {
        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            socket.emit("typing:stop", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void markMessagesAsRead() {
        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            socket.emit("message:mark-read", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showEndChatDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.end_chat)
                .setMessage(R.string.confirm_end_chat)
                .setPositiveButton(R.string.yes, (dialog, which) -> endChat())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void endChat() {
        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            socket.emit("chat:end", data);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (typingHandler != null && typingRunnable != null) {
            typingHandler.removeCallbacks(typingRunnable);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

