package com.crishna.tasks

import com.crishna.tasks.models.Task

interface UpdateData {
    fun onTaskClicked(task : Task)
    fun onTaskChecked(task : Task)
}