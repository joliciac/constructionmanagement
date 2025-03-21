package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class FCMViewModel: ViewModel() {
    private val userRef = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("users")
    private val auth = FirebaseAuth.getInstance()

    fun updateNotification(userId: String, isEnabled: Boolean) {
        userRef.child(userId).child("notificationsEnabled").setValue(isEnabled)
            .addOnSuccessListener {
            Log.d("FCM", "Notification preference updated!")
        }
            .addOnFailureListener { e->
                Log.e("FCM", "Failed to update notification preference", e)
            }
    }

    fun getFCMToken(onTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful){
                    Log.w("FCM", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                onTokenReceived(token)
                Log.d("FCM", "FCM Token: $token")
            }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun storeFCMToken() {
        val userId = getCurrentUserId()
        if (userId != null) {
            getFCMToken { token ->
                userRef.child(userId).child("fcmToken").setValue(token)
                    .addOnSuccessListener {
                        Log.d("FCM Token", "FCM token stored successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FCM Token", "Failed to store FCM token", e)
                    }
            }
        }
    }

}