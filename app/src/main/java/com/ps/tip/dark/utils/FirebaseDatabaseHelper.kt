package com.ps.tip.dark.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = database.reference

    private const val CHECK_CONNECTION_PATH = "check/"
    private const val ICON_REQUEST_PATH = "requests/"

    fun sendRequest(data: String, onComplete: (Boolean) -> Unit) {
        databaseReference.child(ICON_REQUEST_PATH)
            .push()
            .setValue(data)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun checkConnection(onComplete: (Boolean) -> Unit) {
        databaseReference.child(CHECK_CONNECTION_PATH)
            .get().addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

}
