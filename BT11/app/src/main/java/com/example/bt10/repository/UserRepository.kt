package com.example.bt10.repository

import android.content.Context
import com.example.bt10.database.DatabaseHelper
import com.example.bt10.models.User

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun registerUser(user: User): Long {
        return dbHelper.registerUser(user)
    }

    fun loginUser(username: String, password: String): User? {
        return dbHelper.loginUser(username, password)
    }

    fun isUsernameExists(username: String): Boolean {
        return dbHelper.isUsernameExists(username)
    }
}

