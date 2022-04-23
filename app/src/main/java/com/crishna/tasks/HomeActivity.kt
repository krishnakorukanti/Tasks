package com.crishna.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.crishna.tasks.models.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val tasksViewModel: TasksViewModel by viewModels()


    private companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        initObservers()



    }

    private fun initObservers() {
        tasksViewModel.createLiveData.observe(this) {
            Log.d(TAG, "Created")
            onCreate(it)
        }
        tasksViewModel.getListLiveData.observe(this) {
            Log.d(TAG, "Data - ${it.toString()}")

        }
        tasksViewModel.create(Task(title = "FirstTest", create_date = Timestamp.now()))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    private fun onCreate(it: Boolean) {
        if (it) {
            tasksViewModel.getList()

        }
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
}