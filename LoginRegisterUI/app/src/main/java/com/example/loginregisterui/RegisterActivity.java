package com.example.loginregisterui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvTerms, tvSignIn;
    private MaterialCardView btnGoogle, btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các view
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvTerms = findViewById(R.id.tvTerms);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);

        // Xử lý sự kiện nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (fullName.isEmpty()) {
                    etFullName.setError("Vui lòng nhập họ và tên");
                    etFullName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    etEmail.setError("Vui lòng nhập email");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Vui lòng nhập mật khẩu");
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    etPassword.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
                    etConfirmPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Mật khẩu không khớp");
                    etConfirmPassword.requestFocus();
                    return;
                }

                // Xử lý đăng ký (thêm logic của bạn ở đây)
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                
                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện Điều khoản & Chính sách
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Điều khoản & Chính sách", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chuyển sang màn hình Đăng nhập
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình đăng nhập
            }
        });

        // Xử lý sự kiện đăng ký bằng Google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Đăng ký bằng Google", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện đăng ký bằng Facebook
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Đăng ký bằng Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

