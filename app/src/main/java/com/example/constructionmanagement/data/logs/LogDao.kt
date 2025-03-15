package com.example.constructionmanagement.data.logs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntryEntity)

    @Query("SELECT * FROM logs WHERE isSynced = 0")
    suspend fun getUnsyncedLogs(): List<LogEntryEntity>

    @Query("UPDATE logs SET isSynced = 1 WHERE logId = :logId")
    suspend fun markAsSynced(logId: String)

    @Query("DELETE FROM logs WHERE logId = :logId")
    suspend fun deleteLog(logId: String)
}