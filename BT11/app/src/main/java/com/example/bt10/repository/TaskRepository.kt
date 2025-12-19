package com.example.bt10.repository

import android.content.Context
import com.example.bt10.database.DatabaseHelper
import com.example.bt10.models.Task

class TaskRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addTask(task: Task): Long {
        return dbHelper.addTask(task)
    }

    fun getAllTasks(userId: Int): List<Task> {
        return dbHelper.getAllTasks(userId)
    }

    fun updateTask(task: Task): Int {
        return dbHelper.updateTask(task)
    }

    fun deleteTask(taskId: Int): Int {
        return dbHelper.deleteTask(taskId)
    }

    fun toggleTaskCompletion(taskId: Int): Int {
        return dbHelper.toggleTaskCompletion(taskId)
    }
}

