package com.example.constructionmanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class SettingsScreenViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // StateFlow to manage deletion state
//    private val _deletionStatus = MutableStateFlow<Result<Unit>?>(null)
//    val deletionStatus: StateFlow<Result<Unit>?> = _deletionStatus

    // Function to delete the user's authentication account
    fun deleteUserAccount(onSuccess: () -> Unit, onFailure: (Exception?) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception)
                }
            }
        } ?: run {
            onFailure(Exception("User is not authenticated"))
        }
    }

//    private fun deleteUserData(uid: String) {
//        val database = FirebaseDatabase.getInstance()
//        val userRef = database.getReference("users").child(uid)
//
//        userRef.removeValue()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("Settings", "User data successfully deleted from Realtime Database.")
//                } else {
//                    Log.e("Settings", "Error deleting user data from Realtime Database: ${task.exception?.message}")
//                }
//            }
//    }

}