package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Speed test result entity for network analytics
 */
@Entity(tableName = "speed_test_results")
data class SpeedTestResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ssid: String,
    val downloadSpeed: Double, // Mbps
    val uploadSpeed: Double, // Mbps
    val ping: Int, // ms
    val jitter: Int = 0, // ms
    val timestamp: Long = System.currentTimeMillis(),
    val serverLocation: String? = null
)
