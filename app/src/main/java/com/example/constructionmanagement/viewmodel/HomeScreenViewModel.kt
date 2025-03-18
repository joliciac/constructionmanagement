package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : ViewModel() {
    private val progressDatabase = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("progress")
    private val userRef = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("users")
    private val auth = FirebaseAuth.getInstance()

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    private val _userRole = MutableStateFlow<String>("")
    val userRole: StateFlow<String> = _userRole

    init {
        fetchProgress()
    }
    private fun fetchProgress() {
        progressDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Float::class.java)?.let {
                    _progress.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching progress", error.toException())
            }
        })
    }

    private fun fetchUserRole() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        userRef.child(uid).child("role").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val role = snapshot.getValue(String::class.java) ?: "Worker"
                _userRole.value = role
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeScreenViewModel", "Failed to fetch user role", error.toException())
            }
        })
    }

    fun updateProgress(newProgress: Float) {
        progressDatabase.setValue(newProgress)
            .addOnSuccessListener {
                Log.d("HomeScreenViewModel", "Progress updated successfully")
            }
            .addOnFailureListener {
                Log.d("HomeScreenViewModel", "Failed to update progress", it)

            }
    }
}