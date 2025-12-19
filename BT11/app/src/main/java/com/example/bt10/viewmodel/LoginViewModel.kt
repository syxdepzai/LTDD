package com.example.bt10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bt10.models.User
import com.example.bt10.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    
    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError
    
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError
    
    fun login(username: String, password: String) {
        // Validate input
        var isValid = true
        
        if (username.isEmpty()) {
            _usernameError.value = "Vui lòng nhập tên đăng nhập"
            isValid = false
        } else {
            _usernameError.value = null
        }
        
        if (password.isEmpty()) {
            _passwordError.value = "Vui lòng nhập mật khẩu"
            isValid = false
        } else {
            _passwordError.value = null
        }
        
        if (!isValid) return
        
        // Perform login
        val user = userRepository.loginUser(username, password)
        if (user != null) {
            _loginResult.value = LoginResult.Success(user)
        } else {
            _loginResult.value = LoginResult.Error("Tên đăng nhập hoặc mật khẩu không đúng!")
        }
    }
    
    fun clearErrors() {
        _usernameError.value = null
        _passwordError.value = null
    }
}

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

