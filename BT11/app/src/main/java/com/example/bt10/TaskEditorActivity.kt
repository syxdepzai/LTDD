package com.example.bt10

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bt10.databinding.ActivityTaskEditorBinding
import com.example.bt10.utils.SessionManager
import com.example.bt10.viewmodel.SaveResult
import com.example.bt10.viewmodel.TaskEditorViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskEditorBinding
    private val viewModel: TaskEditorViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var taskId: Int = 0
    private var isEditMode = false
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Check if editing existing task
        taskId = intent.getIntExtra("TASK_ID", 0)
        isEditMode = taskId != 0

        if (isEditMode) {
            setupEditMode()
        } else {
            setupAddMode()
        }

        setupObservers()
        setupClickListeners()
    }

    private fun setupEditMode() {
        supportActionBar?.title = "Sửa công việc"

        val title = intent.getStringExtra("TASK_TITLE") ?: ""
        val description = intent.getStringExtra("TASK_DESCRIPTION") ?: ""
        val date = intent.getStringExtra("TASK_DATE") ?: ""
        val isCompleted = intent.getBooleanExtra("TASK_COMPLETED", false)

        binding.etTitle.setText(title)
        binding.etDescription.setText(description)
        binding.tvSelectedDate.text = date
        binding.cbCompleted.isChecked = isCompleted
        selectedDate = date

        binding.btnSave.text = "Cập nhật"
    }

    private fun setupAddMode() {
        supportActionBar?.title = "Thêm công việc mới"
        binding.cbCompleted.visibility = android.view.View.GONE

        // Set default date to today
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.tvSelectedDate.text = today
        selectedDate = today
    }

    private fun setupObservers() {
        // Observe save result
        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is SaveResult.Success -> {
                    val message = if (isEditMode) "Đã cập nhật công việc" else "Đã thêm công việc mới"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SaveResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe title error
        viewModel.titleError.observe(this) { error ->
            binding.etTitle.error = error
            if (error != null) {
                binding.etTitle.requestFocus()
            }
        }

        // Observe date error
        viewModel.dateError.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            saveTask()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        
        // Parse current selected date if exists
        if (selectedDate.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.parse(selectedDate)
                if (date != null) {
                    calendar.time = date
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                binding.tvSelectedDate.text = formattedDate
                selectedDate = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun saveTask() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val userId = sessionManager.getUserId()
        val isCompleted = if (isEditMode) binding.cbCompleted.isChecked else false

        viewModel.saveTask(
            taskId = taskId,
            userId = userId,
            title = title,
            description = description,
            date = selectedDate,
            isCompleted = isCompleted
        )
    }
}

