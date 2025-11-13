package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "traffic_log")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val appName: String,
    val packageName: String,
    val destinationIp: String,
    val destinationPort: Int,
    val protocol: String,
    val status: String, // "ALLOWED" or "BLOCKED"
    val isUnencrypted: Boolean = false
)
