package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_stats")
data class DailyStats(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val connectionsBlocked: Long = 0,
    val adsBlocked: Long = 0, // Will be 0 until Threat Intel is added
    val malwareBlocked: Long = 0, // Will be 0 until Threat Intel is added
    val dataSavedBytes: Long = 0 // Data *not* sent due to blocked rule
)
