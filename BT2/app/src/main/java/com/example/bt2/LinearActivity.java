package com.example.bt2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LinearActivity extends AppCompatActivity {

    private Button btnGreen, btnBlue, btnPurple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);

        // Tham chiếu các Button
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnPurple = findViewById(R.id.btnPurple);

        // Xử lý sự kiện Button 1
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(android.R.id.content).setBackgroundResource(R.drawable.bg_green);
                Toast.makeText(LinearActivity.this, "Background Xanh Lá", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện Button 2
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(android.R.id.content).setBackgroundResource(R.drawable.bg_blue);
                Toast.makeText(LinearActivity.this, "Background Xanh Dương", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện Button 3
        btnPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(android.R.id.content).setBackgroundResource(R.drawable.bg_purple);
                Toast.makeText(LinearActivity.this, "Background Tím", Toast.LENGTH_SHORT).show();
            }
        });
    }
}