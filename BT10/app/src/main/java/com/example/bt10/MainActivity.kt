package com.example.bt10

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bt10.adapters.TaskAdapter
import com.example.bt10.database.DatabaseHelper
import com.example.bt10.databinding.ActivityMainBinding
import com.example.bt10.models.Task
import com.example.bt10.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var taskAdapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        setupActionBar()
        setupRecyclerView()
        setupClickListeners()
        loadTasks()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Danh sách công việc"
        supportActionBar?.subtitle = "Xin chào, ${sessionManager.getUsername()}"
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            taskList = taskList,
            onEditClick = { task -> editTask(task) },
            onDeleteClick = { task -> confirmDeleteTask(task) },
            onToggleComplete = { task -> toggleTaskCompletion(task) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, TaskEditorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        val userId = sessionManager.getUserId()
        taskList.clear()
        taskList.addAll(dbHelper.getAllTasks(userId))
        taskAdapter.notifyDataSetChanged()

        // Show/hide empty state
        if (taskList.isEmpty()) {
            binding.tvEmptyState.visibility = android.view.View.VISIBLE
            binding.recyclerView.visibility = android.view.View.GONE
        } else {
            binding.tvEmptyState.visibility = android.view.View.GONE
            binding.recyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun editTask(task: Task) {
        val intent = Intent(this, TaskEditorActivity::class.java)
        intent.putExtra("TASK_ID", task.id)
        intent.putExtra("TASK_TITLE", task.title)
        intent.putExtra("TASK_DESCRIPTION", task.description)
        intent.putExtra("TASK_DATE", task.date)
        intent.putExtra("TASK_COMPLETED", task.isCompleted)
        startActivity(intent)
    }

    private fun confirmDeleteTask(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Xóa công việc")
            .setMessage("Bạn có chắc chắn muốn xóa công việc \"${task.title}\"?")
            .setPositiveButton("Xóa") { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteTask(task: Task) {
        val result = dbHelper.deleteTask(task.id)
        if (result > 0) {
            loadTasks()
            android.widget.Toast.makeText(this, "Đã xóa công việc", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleTaskCompletion(task: Task) {
        dbHelper.toggleTaskCompletion(task.id)
        loadTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng xuất") { _, _ ->
                sessionManager.logout()
                navigateToLogin()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

