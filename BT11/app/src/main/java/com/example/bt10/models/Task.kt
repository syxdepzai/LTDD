package com.example.bt10.models

data class Task(
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String,
    val date: String,
    val isCompleted: Boolean = false
)

