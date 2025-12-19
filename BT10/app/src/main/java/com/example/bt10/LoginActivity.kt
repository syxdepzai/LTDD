package com.example.bt10

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bt10.database.DatabaseHelper
import com.example.bt10.databinding.ActivityLoginBinding
import com.example.bt10.utils.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.etUsername.error = "Vui lòng nhập tên đăng nhập"
            binding.etUsername.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Vui lòng nhập mật khẩu"
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun performLogin(username: String, password: String) {
        val user = dbHelper.loginUser(username, password)

        if (user != null) {
            sessionManager.createLoginSession(user.id, user.username)
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
            navigateToMainActivity()
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

