package com.crishna.tasks

import com.crishna.tasks.models.Task

interface UpdateData {
    fun onTaskChecked(task : Task)
}