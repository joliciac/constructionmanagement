package com.example.constructionmanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntryEntity(
    @PrimaryKey val logId: String,
    val title: String,
    val date: String,
    val time: String,
    val area: String,
    val description: String,
    val mediaUri: String?,
    val userId: String,
    val userRole: String,
    val isSynced: Boolean = false
)
