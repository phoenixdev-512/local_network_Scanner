package com.example.local_network_scanner.data.db

import androidx.room.Entity

@Entity(tableName = "app_usage_daily", primaryKeys = ["packageName", "date"])
data class AppUsageDaily(
    val packageName: String,
    val date: String, // Format: "YYYY-MM-DD"
    val dataUsedBytes: Long
)
