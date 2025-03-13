package com.example.constructionmanagement.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.constructionmanagement.data.LogEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class LogsScreenViewModel: ViewModel() {
    private val _logs = mutableStateListOf<LogEntry>()
    val logs: SnapshotStateList<LogEntry> get() = _logs


    private val logsDatabase = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("logs")
    private val userRef = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("users")
    private val auth = FirebaseAuth.getInstance()
    private var userRole: String = ""

    init {
        fetchLogs()
    }

    private fun fetchLogs() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        userRef.child(uid).child("role").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val role = snapshot.getValue(String::class.java) ?: "Worker"
                userRole = role
                if (role == "admin") {
                    fetchAllLogs()
                } else {
                    fetchUserLogs(uid)
                }
                Log.d("LogsViewModel", "Logs fetched: ${_logs.size} entries")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LogsViewModel", "Failed to fetch user role", error.toException())
            }
        })
    }

    private fun fetchUserLogs(uid: String) {
        val userLogsRef = logsDatabase.child(uid)
        userLogsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _logs.clear()
                for (logSnapshot in snapshot.children) {
                    val log = logSnapshot.getValue(LogEntry::class.java)
                    log?.let { _logs.add(it)}
                }
                Log.d("LogsViewModel", "User logs fetched: ${_logs.size} entries")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LogsViewModel", "Failed to fetch logs", error.toException())
            }
        })
    }

    private fun fetchAllLogs() {
        logsDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            _logs.clear()
                for (userSnapshot in snapshot.children) {
                    for (logSnapshot in userSnapshot.children){
                        val log = logSnapshot.getValue(LogEntry::class.java)
                        log?.let { _logs.add(it) }
                    }
                }
                Log.d("LogsViewModel", "All logs fetched: ${_logs.size} entries")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("LogsViewModel", "Failed to fetch all logs", error.toException())

            }
        })
    }

    fun submitLog(log: LogEntry, onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid
        val logId = logsDatabase.child(uid).push().key ?: UUID.randomUUID().toString()

        val logWithUserId = log.copy(userId = uid, userRole = userRole)

        logsDatabase.child(uid).child(logId).setValue(logWithUserId)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                Log.e("LogsViewModel", "Failed to submit log", it)
                onResult(false)
            }
    }
}