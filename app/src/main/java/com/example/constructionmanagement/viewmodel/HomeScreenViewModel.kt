package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : ViewModel() {
    private val progressDatabase = FirebaseDatabase.getInstance().getReference("progress")

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    init {
        fetchProgressFromFirebase()
    }
    private fun fetchProgressFromFirebase() {
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

    fun updateProgress(newProgress: Float) {
        progressDatabase.setValue(newProgress)
    }
}