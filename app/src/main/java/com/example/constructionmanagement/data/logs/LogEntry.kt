package com.example.constructionmanagement.data.logs

data class LogEntry(
    val logId: String = "",
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val area: String = "",
    val description: String = "",
    val mediaUri: String? = null,
    val userId: String = "",
    val userRole: String = "",
    val isSynced: Boolean = false
)
