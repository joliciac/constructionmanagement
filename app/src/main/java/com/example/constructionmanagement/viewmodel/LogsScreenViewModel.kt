package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.constructionmanagement.data.LogEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class LogsScreenViewModel: ViewModel() {
    private val _logs = mutableStateListOf<LogEntry>()
    val logs: SnapshotStateList<LogEntry> get() = _logs


    private val database = FirebaseDatabase.getInstance().reference.child("logs")

    init {
        fetchLogs()
    }

    private fun fetchLogs() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _logs.clear()
                for (logSnapshot in snapshot.children) {
                    val log = logSnapshot.getValue(LogEntry::class.java)
                    log?.let { _logs.add(it) }
                }
                Log.d("LogsViewModel", "Logs fetched: ${_logs.size} entries")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LogsViewModel", "Failed to fetch logs", error.toException())
            }
        })
    }

    fun submitLog(log: LogEntry, onResult: (Boolean) -> Unit) {
        val logId = database.push().key ?: UUID.randomUUID().toString()
        database.child(logId).setValue(log)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                Log.e("LogsViewModel", "Failed to submit log", it)
                onResult(false)
            }
    }
}