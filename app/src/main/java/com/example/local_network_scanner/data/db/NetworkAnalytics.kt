package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Network analytics entity for tracking network performance and usage
 */
@Entity(tableName = "network_analytics")
data class NetworkAnalytics(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ssid: String,
    val timestamp: Long,
    val downloadSpeed: Double, // Mbps
    val uploadSpeed: Double, // Mbps
    val signalStrength: Int, // 0-100
    val dataUsed: Long, // bytes
    val connectionDuration: Long, // milliseconds
    val threatsBlocked: Int
)
