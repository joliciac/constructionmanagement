package com.example.constructionmanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class SettingsScreenViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // delete the user's authentication account
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
}