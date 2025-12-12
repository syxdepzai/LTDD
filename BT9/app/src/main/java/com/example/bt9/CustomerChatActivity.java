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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.adapters.ChatMessageAdapter;
import com.example.bt9.models.ChatMessage;
import com.example.bt9.utils.DateUtils;
import com.example.bt9.utils.SocketManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class CustomerChatActivity extends AppCompatActivity {

    private String customerId;
    private String customerName;
    private String roomId;
    private String managerId;
    private String managerName;

    private Socket socket;
    private RecyclerView messagesRecyclerView;
    private ChatMessageAdapter messageAdapter;
    private EditText messageInput;
    private Button sendButton;
    private TextView statusText;
    private TextView managerNameText;
    private TextView typingIndicator;
    private TextView emptyStateText;

    private Handler typingHandler = new Handler();
    private Runnable typingRunnable;
    private boolean isTyping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat);

        // Get customer info from intent
        customerId = getIntent().getStringExtra("customerId");
        customerName = getIntent().getStringExtra("customerName");

        if (customerId == null || customerName == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin khách hàng", Toast.LENGTH_SHORT).show();
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
        statusText = findViewById(R.id.statusText);
        managerNameText = findViewById(R.id.managerNameText);
        typingIndicator = findViewById(R.id.typingIndicator);
        emptyStateText = findViewById(R.id.emptyStateText);

        getSupportActionBar().setTitle(R.string.customer_chat_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        messageAdapter = new ChatMessageAdapter(customerId, "customer");
        messagesRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupSocket() {
        socket = SocketManager.getInstance().getSocket();
        SocketManager.getInstance().connect();

        // Socket event listeners
        socket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
            Toast.makeText(this, "Đã kết nối", Toast.LENGTH_SHORT).show();
            joinAsCustomer();
        }));

        socket.on("customer:waiting", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                String message = data.getString("message");
                int position = data.getInt("position");
                statusText.setText(message + " (Vị trí: " + position + ")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on("room:joined", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                roomId = data.getString("roomId");
                managerId = data.optString("managerId", "");
                managerName = data.optString("managerName", "");

                statusText.setText(R.string.connected_to_manager);
                managerNameText.setText(managerName);
                managerNameText.setVisibility(View.VISIBLE);
                emptyStateText.setVisibility(View.GONE);

                // Enable input
                messageInput.setEnabled(true);
                sendButton.setEnabled(true);

                // Load previous messages if any
                if (data.has("messages")) {
                    JsonArray messagesArray = new Gson().fromJson(data.getJSONArray("messages").toString(), JsonArray.class);
                    for (int i = 0; i < messagesArray.size(); i++) {
                        ChatMessage message = new Gson().fromJson(messagesArray.get(i), ChatMessage.class);
                        messageAdapter.addMessage(message);
                    }
                    messagesRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                }

                Toast.makeText(this, "Đã kết nối với " + managerName, Toast.LENGTH_SHORT).show();
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

        socket.on("manager:disconnected", args -> runOnUiThread(() -> {
            statusText.setText(R.string.manager_disconnected);
            Toast.makeText(this, "Manager đã ngắt kết nối", Toast.LENGTH_SHORT).show();
        }));

        socket.on("chat:ended", args -> runOnUiThread(() -> {
            Toast.makeText(this, "Cuộc trò chuyện đã kết thúc", Toast.LENGTH_LONG).show();
            finish();
        }));

        socket.on(Socket.EVENT_DISCONNECT, args -> runOnUiThread(() -> {
            statusText.setText(R.string.disconnected);
            messageInput.setEnabled(false);
            sendButton.setEnabled(false);
        }));
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (roomId != null && s.length() > 0 && !isTyping) {
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

    private void joinAsCustomer() {
        try {
            JSONObject data = new JSONObject();
            data.put("customerId", customerId);
            data.put("customerName", customerName);
            socket.emit("customer:join", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty() || roomId == null) {
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("roomId", roomId);
            data.put("message", message);
            data.put("senderId", customerId);
            data.put("senderName", customerName);
            data.put("senderType", "customer");

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
        if (roomId != null) {
            try {
                JSONObject data = new JSONObject();
                data.put("roomId", roomId);
                data.put("userName", customerName);
                data.put("userType", "customer");
                socket.emit("typing:start", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void emitTypingStop() {
        if (roomId != null) {
            try {
                JSONObject data = new JSONObject();
                data.put("roomId", roomId);
                socket.emit("typing:stop", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (typingHandler != null && typingRunnable != null) {
            typingHandler.removeCallbacks(typingRunnable);
        }
        // Socket will be disconnected when app is fully closed
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

