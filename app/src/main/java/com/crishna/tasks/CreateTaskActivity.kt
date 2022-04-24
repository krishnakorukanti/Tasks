package com.crishna.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.crishna.tasks.databinding.ActivityCreateTaskBinding
import com.crishna.tasks.models.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.util.*

class CreateTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0
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

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun initViews() {
        binding.dateAndTimeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                val constraintsBuilder =
//                    CalendarConstraints.Builder()
//                        .setValidator(DateValidatorPointForward.now())
//                val picker = MaterialDatePicker.Builder.datePicker()
//                    .setCalendarConstraints(constraintsBuilder.build())
//                    .setTitleText("Choose Date of Task")
//                    .build()
//                picker.show(supportFragmentManager, "DatePicker")
//            }
            if (isChecked) {
                pickDateAndTime()
                binding.timeAndDateText.visibility = View.VISIBLE
            } else {
                binding.timeAndDateText.visibility = View.GONE
            }
        }
        binding.apply {
            addTaskBtn.setOnClickListener {
                val task = Task()
                task.apply {
                    id = UUID.randomUUID().toString()
                    title = titleEditText.text.toString().ifEmpty {
                        Snackbar.make(binding.root,"Title Required",Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                        }
                    description = descriptionEditText.text.toString()
                    isDone = completedSwitch.isChecked
                    if (dateAndTimeSwitch.isChecked){
                        scheduledTime = Timestamp(Date(savedYear,savedMonth,savedDay,savedHour,savedMinute))
                    }
                    create_date = Timestamp.now()
                    update_date = Timestamp.now()
                }
                tasksViewModel.create(task)

            }
        }

    }

    private fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDateAndTime() {
        getDateTimeCalender()
        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedYear = year
        savedMonth = month
        getDateTimeCalender()
        TimePickerDialog(this, this, hour, minute, true).show()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        binding.timeAndDateText.text =
            "Date : $savedDay - $savedMonth - $savedYear // Time - $savedHour : $savedMinute"
    }
}