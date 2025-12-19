package com.example.bt10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bt10.models.Task
import com.example.bt10.repository.TaskRepository

class TaskEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)
    
    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult
    
    private val _titleError = MutableLiveData<String?>()
    val titleError: LiveData<String?> = _titleError
    
    private val _dateError = MutableLiveData<String?>()
    val dateError: LiveData<String?> = _dateError
    
    fun saveTask(
        taskId: Int,
        userId: Int,
        title: String,
        description: String,
        date: String,
        isCompleted: Boolean
    ) {
        // Validate input
        var isValid = true
        
        if (title.isEmpty()) {
            _titleError.value = "Vui lòng nhập tiêu đề"
            isValid = false
        } else {
            _titleError.value = null
        }
        
        if (date.isEmpty()) {
            _dateError.value = "Vui lòng chọn ngày"
            isValid = false
        } else {
            _dateError.value = null
        }
        
        if (!isValid) return
        
        val task = Task(
            id = taskId,
            userId = userId,
            title = title,
            description = description,
            date = date,
            isCompleted = isCompleted
        )
        
        val result = if (taskId == 0) {
            taskRepository.addTask(task)
        } else {
            taskRepository.updateTask(task).toLong()
        }
        
        if (result > 0) {
            _saveResult.value = SaveResult.Success
        } else {
            _saveResult.value = SaveResult.Error("Lưu công việc thất bại")
        }
    }
    
    fun clearErrors() {
        _titleError.value = null
        _dateError.value = null
    }
}

sealed class SaveResult {
    object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}

