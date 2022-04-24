package com.crishna.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crishna.tasks.databinding.TaskItemBinding
import com.crishna.tasks.models.Task

class TasksAdapter(var tasks :List<Task>,val updateData: UpdateData,val showComments : Boolean) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       with(holder){
           with(tasks[position]){
               binding.taskTitleText.text = this.title
               binding.taskDescription.text = this.description
               if (showComments){
                   binding.comments.visibility = View.VISIBLE
               }else{
                   binding.comments.visibility = View.GONE
               }
               if (this.description.isNullOrEmpty()){
                   binding.comments.visibility = View.GONE
               }
               binding.taskCheckBox.isChecked = this.isDone
               if(this.isDone){
                   binding.taskTitleText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
               }
               binding.taskCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                   updateData.onTaskChecked(this)
               }

           }
       }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}