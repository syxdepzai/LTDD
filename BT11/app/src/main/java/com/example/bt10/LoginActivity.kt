package com.example.bt10

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bt10.databinding.ActivityLoginBinding
import com.example.bt10.utils.SessionManager
import com.example.bt10.viewmodel.LoginResult
import com.example.bt10.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Observe login result
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    sessionManager.createLoginSession(result.user.id, result.user.username)
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe username error
        viewModel.usernameError.observe(this) { error ->
            binding.etUsername.error = error
            if (error != null) {
                binding.etUsername.requestFocus()
            }
        }

        // Observe password error
        viewModel.passwordError.observe(this) { error ->
            binding.etPassword.error = error
            if (error != null) {
                binding.etPassword.requestFocus()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(username, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

