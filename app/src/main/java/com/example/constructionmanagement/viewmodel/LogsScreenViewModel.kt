package com.example.constructionmanagement.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.constructionmanagement.data.logs.LogDao
import com.example.constructionmanagement.data.logs.LogEntry
import com.example.constructionmanagement.data.logs.LogsDatabase
import com.example.constructionmanagement.data.logs.ConnectivityHelper
import com.example.constructionmanagement.data.logs.toLogEntry
import com.example.constructionmanagement.data.logs.toLogEntryEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class LogsScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val _logs = mutableStateListOf<LogEntry>()
    val logs: SnapshotStateList<LogEntry> get() = _logs


    private val logsDatabase = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("logs")
    private val userRef = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app").reference.child("users")
    private val auth = FirebaseAuth.getInstance()
    private var userRole: String = ""

    private val _selectedLog = MutableStateFlow<LogEntry?>(null)
    val selectedLog: StateFlow<LogEntry?> = _selectedLog

    private val logDao: LogDao = LogsDatabase.getDatabase(application).logDao()

    init {
        fetchLogs()
        viewModelScope.launch {
            ConnectivityHelper.isOnline.collect() { isOnline ->
                if (isOnline) {
                    syncLogsWithFirebase()
                }
            }
        }
    }

    private fun fetchLogs() {
        viewModelScope.launch {
            val cachedLogs = logDao.getUnsyncedLogs()
            _logs.clear()
            _logs.addAll(cachedLogs.map { it.toLogEntry() })
        }

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
                    log?.let {
                        _logs.add(it)
                        saveLogToLocal(it)
                    }
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
                        log?.let {
                            _logs.add(it)
                            saveLogToLocal(it)
                        }
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

        val logCapture = log.copy(logId = logId, userId = uid, userRole = userRole)

        logsDatabase.child(uid).child(logId).setValue(logCapture)
            .addOnSuccessListener {
                saveLogToLocal(logCapture.copy(isSynced = true))
                onResult(true)
            }
            .addOnFailureListener {
                saveLogToLocal(logCapture.copy(isSynced = false))
                Log.e("LogsViewModel", "Failed to submit log", it)
                onResult(false)
            }
    }

    private fun saveLogToLocal(log: LogEntry) {
        viewModelScope.launch {
            logDao.insertLog(log.toLogEntryEntity())
        }
    }


    fun deleteLog(log: LogEntry, onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid
        val logId = log.logId

        if (logId.isEmpty()) {
            onResult(false)
            return
        }

        logsDatabase.child(uid).child(logId).removeValue()
            .addOnSuccessListener {
                _logs.removeIf{ it.logId == logId}
                viewModelScope.launch { logDao.deleteLog(log.logId) }
                onResult(true)
            }
            .addOnFailureListener {
                Log.e("LogsViewModel", "Failed to delete log", it)
                onResult(false)
            }
    }

    fun showLogOptions(log: LogEntry) {
        _selectedLog.value = log
    }

    fun hideLogOptions() {
        _selectedLog.value = null
    }

    fun updateLog(updatedLog: LogEntry, onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid
        val logId = updatedLog.logId

        if (logId.isEmpty()) {
            Log.e("LogsViewModel", "Log ID is empty, cannot update log.")
            onResult(false)
            return
        }

        logsDatabase.child(uid).child(logId).setValue(updatedLog)
            .addOnSuccessListener {
                val index = _logs.indexOfFirst { it.logId == logId }
                if (index != -1) {
                    _logs[index] = updatedLog
                }
                onResult(true)
                _selectedLog.value = null
            }
            .addOnFailureListener {
                Log.e("LogsViewModel", "Failed to update log", it)
                onResult(false)
            }
    }

    fun markLogAsSynced(logId: String) {
        viewModelScope.launch {
            logDao.markAsSynced(logId)
        }
    }

    fun syncLogsWithFirebase() {
        viewModelScope.launch {
            val unsyncedLogs = logDao.getUnsyncedLogs()
            for (log in unsyncedLogs ) {
                submitLog(log.toLogEntry()) { success ->
                    if (success) {
                        markLogAsSynced(log.logId)
                    }
                }
            }
        }

    }

    fun setSelectedLog(log: LogEntry) {
        _selectedLog.value = log
    }
}