package com.example.bt10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bt10.models.Task
import com.example.bt10.repository.TaskRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)
    
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> = _deleteResult
    
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty
    
    fun loadTasks(userId: Int) {
        val taskList = taskRepository.getAllTasks(userId)
        _tasks.value = taskList
        _isEmpty.value = taskList.isEmpty()
    }
    
    fun deleteTask(taskId: Int, userId: Int) {
        val result = taskRepository.deleteTask(taskId)
        _deleteResult.value = result > 0
        if (result > 0) {
            loadTasks(userId)
        }
    }
    
    fun toggleTaskCompletion(taskId: Int, userId: Int) {
        taskRepository.toggleTaskCompletion(taskId)
        loadTasks(userId)
    }
}

