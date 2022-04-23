package com.crishna.tasks.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Task(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var isDone: Boolean = false,
    var scheduledTime: Timestamp? = null,
    var create_date: Timestamp? = null,
    var update_date: Timestamp? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "isDone" to isDone,
            "scheduledTime" to scheduledTime,
            "description" to description,
            "create_date" to create_date,
            "update_date" to update_date,
        )
    }
}