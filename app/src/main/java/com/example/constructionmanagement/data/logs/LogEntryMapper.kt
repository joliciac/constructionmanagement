package com.example.constructionmanagement.data.logs

fun LogEntryEntity.toLogEntry(): LogEntry {
    return LogEntry(
        logId = logId,
        title = title,
        date = date,
        time = time,
        area = area,
        description = description,
        mediaUri = mediaUri,
        userId = userId,
        userRole = userRole,
        isSynced = isSynced
    )
}

fun LogEntry.toLogEntryEntity(): LogEntryEntity {
    return LogEntryEntity(
        logId = logId,
        title = title,
        date = date,
        time = time,
        area = area,
        description = description,
        mediaUri = mediaUri,
        userId = userId,
        userRole = userRole,
        isSynced = isSynced
    )
}