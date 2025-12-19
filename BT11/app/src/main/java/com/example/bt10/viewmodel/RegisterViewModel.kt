package com.example.bt10.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bt10.models.User
import com.example.bt10.repository.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult
    
    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError
    
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError
    
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError
    
    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError
    
    fun register(username: String, email: String, password: String, confirmPassword: String) {
        // Validate input
        var isValid = true
        
        if (username.isEmpty()) {
            _usernameError.value = "Vui lòng nhập tên đăng nhập"
            isValid = false
        } else if (username.length < 4) {
            _usernameError.value = "Tên đăng nhập phải có ít nhất 4 ký tự"
            isValid = false
        } else {
            _usernameError.value = null
        }
        
        if (email.isEmpty()) {
            _emailError.value = "Vui lòng nhập email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Email không hợp lệ"
            isValid = false
        } else {
            _emailError.value = null
        }
        
        if (password.isEmpty()) {
            _passwordError.value = "Vui lòng nhập mật khẩu"
            isValid = false
        } else if (password.length < 6) {
            _passwordError.value = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        } else {
            _passwordError.value = null
        }
        
        if (confirmPassword.isEmpty()) {
            _confirmPasswordError.value = "Vui lòng xác nhận mật khẩu"
            isValid = false
        } else if (password != confirmPassword) {
            _confirmPasswordError.value = "Mật khẩu không khớp"
            isValid = false
        } else {
            _confirmPasswordError.value = null
        }
        
        if (!isValid) return
        
        // Check if username exists
        if (userRepository.isUsernameExists(username)) {
            _usernameError.value = "Tên đăng nhập đã tồn tại"
            return
        }
        
        // Perform registration
        val user = User(username = username, password = password, email = email)
        val result = userRepository.registerUser(user)
        
        if (result > 0) {
            _registerResult.value = RegisterResult.Success
        } else {
            _registerResult.value = RegisterResult.Error("Đăng ký thất bại. Vui lòng thử lại.")
        }
    }
    
    fun clearErrors() {
        _usernameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }
}

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

