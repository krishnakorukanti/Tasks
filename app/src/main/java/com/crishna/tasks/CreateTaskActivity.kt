package com.crishna.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.crishna.tasks.databinding.ActivityCreateTaskBinding
import com.crishna.tasks.models.Task
import com.google.android.gms.common.util.UidVerifier
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private val tasksViewModel: TasksViewModel by viewModels()
    private companion object {
        private const val TAG = "CreateTaskActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        tasksViewModel.createLiveData.observe(this) {
            Log.d(TAG, "Created")
            onCreate(it)
        }
    }
    private fun onCreate(it: Boolean) {
        if (it) {
            tasksViewModel.getList()

            val intent = Intent(this,HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
    private fun initViews() {
        binding.dateAndTimeSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
        if (isChecked){
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
            val picker =  MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Choose Date of Task")
                .build()
            picker.show(supportFragmentManager,"DatePicker")
        }
        }
        binding.apply {
            addTaskBtn.setOnClickListener {
                val task = Task()
                task.apply {
                    id = UUID.randomUUID().toString()
                    title = titleEditText.text.toString()
                    description = descriptionEditText.text.toString()
                    isDone = completedSwitch.isChecked
                    create_date = Timestamp.now()
                    update_date = Timestamp.now()
                }
             tasksViewModel.create(task)

            }
        }

    }
}