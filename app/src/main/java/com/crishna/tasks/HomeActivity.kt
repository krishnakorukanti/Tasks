package com.crishna.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crishna.tasks.databinding.ActivityHomeBinding
import com.crishna.tasks.databinding.ActivityMainBinding
import com.crishna.tasks.models.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity(),UpdateData {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var auth: FirebaseAuth
    private val tasksViewModel: TasksViewModel by viewModels()

    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasks : List<Task>

    private companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        initObservers()
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this,CreateTaskActivity::class.java))
        }


    }

    private fun initObservers() {
        tasksViewModel.getList()

        tasksViewModel.getListLiveData.observe(this) {
            Log.d(TAG, "Data - ${it.toString()}")
            tasks = it
            tasksAdapter = TasksAdapter(tasks,this,false)

            // create  layoutManager
            val layoutManager = LinearLayoutManager(this)

            // pass it to rvLists layoutManager
            binding.tasksRecyclerView.layoutManager = layoutManager
            binding.tasksRecyclerView.adapter = tasksAdapter
            binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                tasksAdapter = TasksAdapter(tasks,this,isChecked)
                binding.tasksRecyclerView.adapter = tasksAdapter            }

        }
        tasksViewModel.updateLiveData.observe(this){
            tasksViewModel.getList()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.homeLogout) {
            Log.i(TAG, "Logout")
            //Logout user
            auth.signOut()
            val logOutIntent = Intent(this, MainActivity::class.java)
            logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logOutIntent)

        }
        return super.onOptionsItemSelected(item)
    }



    override fun onTaskClicked(task: Task) {
        //TODO("Not yet implemented")
    }

    override fun onTaskChecked(task: Task) {
       task.isDone = !task.isDone
        tasksViewModel.update(task)
    }
}