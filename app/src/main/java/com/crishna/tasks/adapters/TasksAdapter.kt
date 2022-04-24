package com.crishna.tasks.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crishna.tasks.interfaces.UpdateData
import com.crishna.tasks.databinding.TaskItemBinding
import com.crishna.tasks.models.Task
import com.google.firebase.Timestamp
import java.util.*

class TasksAdapter(var tasks: List<Task>, val updateData: UpdateData, val showComments: Boolean) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(tasks[position]) {
                binding.taskTitleText.text = this.title
                binding.taskDescription.text = this.description
                if (this.scheduledTime!=null){
                    val dateTimeText = getTimeAndDate(this.scheduledTime!!)
                    Log.d("TimeStamp", dateTimeText)
                    binding.dateAndTimeText.text = dateTimeText
                }


                if (showComments) {
                    binding.comments.visibility = View.VISIBLE
                    binding.dateAndTime.visibility = View.VISIBLE
                } else {
                    binding.comments.visibility = View.GONE
                    binding.dateAndTime.visibility = View.GONE
                }
                if (this.description.isNullOrEmpty()) {
                    binding.comments.visibility = View.GONE
                }
                binding.taskCheckBox.isChecked = this.isDone
                if (this.isDone) {
                    binding.taskTitleText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                binding.taskCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    updateData.onTaskChecked(this)
                }
                if (this.scheduledTime == null) {
                    binding.dateAndTime.visibility = View.GONE
                }

            }
        }
    }

    private fun getTimeAndDate(scheduledTime: Timestamp) : String{
        val date = Date(scheduledTime.seconds)
        return date.toString()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}