package com.example.bt9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText customerNameInput;
    private TextInputEditText customerIdInput;
    private TextInputEditText managerNameInput;
    private TextInputEditText managerIdInput;
    private Button customerStartButton;
    private Button managerStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        customerNameInput = findViewById(R.id.customerNameInput);
        customerIdInput = findViewById(R.id.customerIdInput);
        managerNameInput = findViewById(R.id.managerNameInput);
        managerIdInput = findViewById(R.id.managerIdInput);
        customerStartButton = findViewById(R.id.customerStartButton);
        managerStartButton = findViewById(R.id.managerStartButton);

        // Customer button click
        customerStartButton.setOnClickListener(v -> {
            String name = customerNameInput.getText().toString().trim();
            String id = customerIdInput.getText().toString().trim();

            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, CustomerChatActivity.class);
            intent.putExtra("customerId", id);
            intent.putExtra("customerName", name);
            startActivity(intent);
        });

        // Manager button click
        managerStartButton.setOnClickListener(v -> {
            String name = managerNameInput.getText().toString().trim();
            String id = managerIdInput.getText().toString().trim();

            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, ManagerDashboardActivity.class);
            intent.putExtra("managerId", id);
            intent.putExtra("managerName", name);
            startActivity(intent);
        });
    }
}
