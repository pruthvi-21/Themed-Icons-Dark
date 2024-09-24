package com.jw.tip.dark.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jw.tip.dark.models.FirebaseRequestData

object FirebaseDatabaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = database.reference

    private const val CHECK_CONNECTION_PATH = "check/"
    private const val ICON_REQUEST_PATH = "requests/"

//    fun hasSentRequestToday(userId: String, onComplete: (Boolean) -> Unit) {
//        val currentDate = Calendar.getInstance()
//        val startOfDay = currentDate.apply {
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }.timeInMillis
//
//        val endOfDay = currentDate.apply {
//            set(Calendar.HOUR_OF_DAY, 23)
//            set(Calendar.MINUTE, 59)
//            set(Calendar.SECOND, 59)
//            set(Calendar.MILLISECOND, 999)
//        }.timeInMillis
//
//        databaseReference.child(ICON_REQUEST_PATH)
//            .orderByChild("userId")
//            .equalTo(userId)
//            .get()
//            .addOnCompleteListener { task ->
//
//                TODO: not working
//                if (task.isSuccessful) {
//                    val requests = task.result?.children
//                    val hasRequestedToday = requests?.any { snapshot ->
//                        val requestData = snapshot.getValue(FirebaseRequestData::class.java)
//                        requestData?.timestamp in startOfDay..endOfDay
//                    } ?: false
//
//                    onComplete(hasRequestedToday)
//                } else {
//                    // Handle failure (e.g., log the error)
//                    onComplete(false)
//                }
//            }
//    }

    fun sendRequest(
        data: FirebaseRequestData,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        databaseReference.child(ICON_REQUEST_PATH)
            .push()
            .setValue(data)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
            .addOnFailureListener { err ->
                onFailure(err)
            }
    }

    fun checkConnection(onComplete: (Boolean) -> Unit) {
        databaseReference.child(CHECK_CONNECTION_PATH)
            .get().addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

}
