package com.example.bt10.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bt10.R
import com.example.bt10.databinding.ItemTaskBinding
import com.example.bt10.models.Task

class TaskAdapter(
    private val taskList: List<Task>,
    private val onEditClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onToggleComplete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDescription.text = task.description
            binding.tvDate.text = task.date
            binding.cbCompleted.isChecked = task.isCompleted

            // Apply strikethrough if completed
            if (task.isCompleted) {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            } else {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            }

            // Set click listeners
            binding.cbCompleted.setOnClickListener {
                onToggleComplete(task)
            }

            binding.btnEdit.setOnClickListener {
                onEditClick(task)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(task)
            }

            binding.root.setOnClickListener {
                onEditClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size
}

