package com.example.constructionmanagement.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogEntryEntity::class], version = 1)
abstract class LogsDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        @Volatile private var INSTANCE: LogsDatabase? = null

        fun getDatabase(context: Context): LogsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LogsDatabase::class.java,
                    "logs_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
