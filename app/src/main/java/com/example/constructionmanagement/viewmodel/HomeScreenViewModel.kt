package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val progressDatabase = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("progress")
    private val userRef = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("users")
    private val auth = FirebaseAuth.getInstance()

    //Progress Bar
    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    //User
    private val _userRole = MutableStateFlow<String>("")
    val userRole: StateFlow<String> = _userRole

    //Check in
    private val _checkInTime = MutableStateFlow<Long?>(null)
    val checkInTime: StateFlow<Long?> = _checkInTime
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private var isCheckedIn = false

    init {
        fetchProgress()
        fetchUserRole()
        fetchCheckInTime()
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
    private fun fetchCheckInTime() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid

        userRef.child(userId).child("checkInTime").get().addOnSuccessListener {
            val checkInTime = it.getValue(Long::class.java)
            if (checkInTime != null) {
                _checkInTime.value = checkInTime
                isCheckedIn = true
                calculateElapsedTime(checkInTime)
            }
        }
    }

    fun checkIn() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid
        val checkInTime = System.currentTimeMillis() / 1000 // current time in seconds

        // Save check-in time to Firebase
        userRef.child(userId).child("checkInTime").setValue(checkInTime).addOnSuccessListener {
            _checkInTime.value = checkInTime
            isCheckedIn = true
            startTimer(checkInTime)
        }
    }

    fun checkOut() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid
        val checkOutTime = System.currentTimeMillis() / 1000 // current time in seconds

        // Save check-out time to Firebase
        userRef.child(userId).child("checkOutTime").setValue(checkOutTime).addOnSuccessListener {
            isCheckedIn = false
            _elapsedTime.value = 0L // Reset elapsed time on check-out
        }
    }

    private fun startTimer(checkInTime: Long) {
        val startTime = checkInTime * 1000 // Convert to milliseconds

        // Timer updates every second
        viewModelScope.launch {
            while (isCheckedIn) {
                delay(1000)
                val currentTime = System.currentTimeMillis()
                _elapsedTime.value = (currentTime - startTime) / 1000 // elapsed time in seconds
            }
        }
    }

    private fun calculateElapsedTime(checkInTime: Long) {
        val startTime = checkInTime * 1000 // Convert to milliseconds
        val currentTime = System.currentTimeMillis()
        _elapsedTime.value = (currentTime - startTime) / 1000 // elapsed time in seconds
    }

}