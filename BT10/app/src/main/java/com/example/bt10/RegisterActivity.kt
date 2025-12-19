package com.example.bt10

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bt10.database.DatabaseHelper
import com.example.bt10.databinding.ActivityRegisterBinding
import com.example.bt10.models.User

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(username, email, password, confirmPassword)) {
                performRegistration(username, email, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty()) {
            binding.etUsername.error = "Vui lòng nhập tên đăng nhập"
            binding.etUsername.requestFocus()
            return false
        }

        if (username.length < 4) {
            binding.etUsername.error = "Tên đăng nhập phải có ít nhất 4 ký tự"
            binding.etUsername.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Vui lòng nhập email"
            binding.etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email không hợp lệ"
            binding.etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Vui lòng nhập mật khẩu"
            binding.etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            binding.etPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Mật khẩu không khớp"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun performRegistration(username: String, email: String, password: String) {
        // Check if username already exists
        if (dbHelper.isUsernameExists(username)) {
            binding.etUsername.error = "Tên đăng nhập đã tồn tại"
            binding.etUsername.requestFocus()
            return
        }

        val user = User(
            username = username,
            password = password,
            email = email
        )

        val result = dbHelper.registerUser(user)

        if (result > 0) {
            Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
        }
    }
}

