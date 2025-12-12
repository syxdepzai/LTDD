package com.example.bt9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.adapters.ChatRoomAdapter;
import com.example.bt9.adapters.WaitingCustomerAdapter;
import com.example.bt9.models.ChatRoom;
import com.example.bt9.models.WaitingCustomer;
import com.example.bt9.utils.SocketManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class ManagerDashboardActivity extends AppCompatActivity {

    private String managerId;
    private String managerName;

    private Socket socket;
    private RecyclerView waitingCustomersRecyclerView;
    private RecyclerView activeChatsRecyclerView;
    private WaitingCustomerAdapter waitingCustomerAdapter;
    private ChatRoomAdapter chatRoomAdapter;
    private TextView managerNameText;
    private TextView noWaitingCustomersText;
    private TextView noActiveChatsText;
    private View waitingCustomersSection;
    private View activeChatsSection;
    private Button toggleButton;

    private boolean showingWaitingCustomers = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        // Get manager info from intent
        managerId = getIntent().getStringExtra("managerId");
        managerName = getIntent().getStringExtra("managerName");

        if (managerId == null || managerName == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin manager", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerViews();
        setupSocket();
        setupToggle();
    }

    private void initViews() {
        managerNameText = findViewById(R.id.managerNameText);
        waitingCustomersRecyclerView = findViewById(R.id.waitingCustomersRecyclerView);
        activeChatsRecyclerView = findViewById(R.id.activeChatsRecyclerView);
        noWaitingCustomersText = findViewById(R.id.noWaitingCustomersText);
        noActiveChatsText = findViewById(R.id.noActiveChatsText);
        waitingCustomersSection = findViewById(R.id.waitingCustomersSection);
        activeChatsSection = findViewById(R.id.activeChatsSection);

        managerNameText.setText("Manager: " + managerName);

        getSupportActionBar().setTitle(R.string.manager_dashboard_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerViews() {
        // Waiting customers
        waitingCustomerAdapter = new WaitingCustomerAdapter(customer -> {
            acceptCustomer(customer);
        });
        waitingCustomersRecyclerView.setAdapter(waitingCustomerAdapter);
        waitingCustomersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Active chats
        chatRoomAdapter = new ChatRoomAdapter(room -> {
            openChatRoom(room);
        });
        activeChatsRecyclerView.setAdapter(chatRoomAdapter);
        activeChatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSocket() {
        socket = SocketManager.getInstance().getSocket();
        SocketManager.getInstance().connect();

        socket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
            Toast.makeText(this, "Đã kết nối", Toast.LENGTH_SHORT).show();
            joinAsManager();
        }));

        socket.on("manager:waiting-customers", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                JSONArray customersArray = data.getJSONArray("customers");
                
                Gson gson = new Gson();
                for (int i = 0; i < customersArray.length(); i++) {
                    WaitingCustomer customer = gson.fromJson(customersArray.getJSONObject(i).toString(), WaitingCustomer.class);
                    waitingCustomerAdapter.addCustomer(customer);
                }

                updateEmptyStates();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on("manager:active-rooms", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                JSONArray roomsArray = data.getJSONArray("rooms");
                
                Gson gson = new Gson();
                for (int i = 0; i < roomsArray.length(); i++) {
                    ChatRoom room = gson.fromJson(roomsArray.getJSONObject(i).toString(), ChatRoom.class);
                    chatRoomAdapter.addRoom(room);
                }

                updateEmptyStates();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on("manager:new-customer-waiting", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                WaitingCustomer customer = new Gson().fromJson(data.toString(), WaitingCustomer.class);
                waitingCustomerAdapter.addCustomer(customer);
                updateEmptyStates();
                Toast.makeText(this, "Khách hàng mới: " + customer.getCustomerName(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        socket.on("manager:customer-accepted", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                String customerId = data.getString("customerId");
                waitingCustomerAdapter.removeCustomer(customerId);
                updateEmptyStates();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on("room:created", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                String roomId = data.getString("roomId");
                String customerId = data.getString("customerId");
                String customerName = data.getString("customerName");

                // Open chat activity
                Intent intent = new Intent(ManagerDashboardActivity.this, ManagerChatActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("customerId", customerId);
                intent.putExtra("customerName", customerName);
                intent.putExtra("managerId", managerId);
                intent.putExtra("managerName", managerName);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        socket.on(Socket.EVENT_DISCONNECT, args -> runOnUiThread(() -> {
            Toast.makeText(this, "Đã ngắt kết nối", Toast.LENGTH_SHORT).show();
        }));
    }

    private void setupToggle() {
        waitingCustomersSection.setOnClickListener(v -> {
            // Already showing, do nothing
        });

        // Simple toggle between sections
        findViewById(R.id.waitingCustomersSection).setVisibility(View.VISIBLE);
        findViewById(R.id.activeChatsSection).setVisibility(View.GONE);
    }

    private void joinAsManager() {
        try {
            JSONObject data = new JSONObject();
            data.put("managerId", managerId);
            data.put("managerName", managerName);
            socket.emit("manager:join", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void acceptCustomer(WaitingCustomer customer) {
        try {
            JSONObject data = new JSONObject();
            data.put("customerId", customer.getCustomerId());
            data.put("managerId", managerId);
            data.put("managerName", managerName);
            socket.emit("manager:accept-customer", data);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi chấp nhận khách hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void openChatRoom(ChatRoom room) {
        Intent intent = new Intent(this, ManagerChatActivity.class);
        intent.putExtra("roomId", room.getRoomId());
        intent.putExtra("customerId", room.getCustomerId());
        intent.putExtra("customerName", room.getCustomerName());
        intent.putExtra("managerId", managerId);
        intent.putExtra("managerName", managerName);
        startActivity(intent);
    }

    private void updateEmptyStates() {
        // Waiting customers
        if (waitingCustomerAdapter.getItemCount() == 0) {
            noWaitingCustomersText.setVisibility(View.VISIBLE);
            waitingCustomersRecyclerView.setVisibility(View.GONE);
        } else {
            noWaitingCustomersText.setVisibility(View.GONE);
            waitingCustomersRecyclerView.setVisibility(View.VISIBLE);
        }

        // Active chats
        if (chatRoomAdapter.getItemCount() == 0) {
            noActiveChatsText.setVisibility(View.VISIBLE);
            activeChatsRecyclerView.setVisibility(View.GONE);
        } else {
            noActiveChatsText.setVisibility(View.GONE);
            activeChatsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to dashboard
        if (socket != null && socket.connected()) {
            joinAsManager();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

