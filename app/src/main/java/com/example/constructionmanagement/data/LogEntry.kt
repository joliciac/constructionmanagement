package com.example.constructionmanagement.data

data class LogEntry(
    val date: String = "",
    val time: String = "",
    val area: String = "",
    val description: String = "",
    val mediaUri: String? = null
)
