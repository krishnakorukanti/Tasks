package com.crishna.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crishna.tasks.models.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TasksViewModel : ViewModel() {

    private companion object{
        private const val tasks = "tasks"
    }
    private var db = Firebase.firestore
    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    fun create(task: Task) {
        val docRef = db.collection(tasks)
        docRef.add(task.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(task: Task) {
        val docRef = db.collection(tasks)
        docRef.document(task.id!!).update(task.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(tasks)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }
    fun getList() {
        val docRef = db.collection(tasks)
        docRef.get().addOnSuccessListener {
            val products = ArrayList<Task>()
            for (item in it.documents) {
                val product = Task()
                product.id = item.id
                product.title = item.data!!["title"] as String?
                product.isDone = item.data!!["isDone"] as Boolean
                product.description = item.data!!["description"] as String?
                product.scheduledTime = item.data!!["scheduledTime"] as Timestamp?
                product.create_date = item.data!!["create_date"] as Timestamp?
                product.update_date = item.data!!["update_date"] as Timestamp?
                products.add(product)
            }

            getListLiveData.postValue(products)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

}