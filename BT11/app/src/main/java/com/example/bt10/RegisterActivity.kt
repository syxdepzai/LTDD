package com.example.bt10

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bt10.databinding.ActivityRegisterBinding
import com.example.bt10.viewmodel.RegisterResult
import com.example.bt10.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Observe register result
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is RegisterResult.Success -> {
                    Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is RegisterResult.Error -> {
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

        // Observe email error
        viewModel.emailError.observe(this) { error ->
            binding.etEmail.error = error
            if (error != null) {
                binding.etEmail.requestFocus()
            }
        }

        // Observe password error
        viewModel.passwordError.observe(this) { error ->
            binding.etPassword.error = error
            if (error != null) {
                binding.etPassword.requestFocus()
            }
        }

        // Observe confirm password error
        viewModel.confirmPasswordError.observe(this) { error ->
            binding.etConfirmPassword.error = error
            if (error != null) {
                binding.etConfirmPassword.requestFocus()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            
            viewModel.register(username, email, password, confirmPassword)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}

