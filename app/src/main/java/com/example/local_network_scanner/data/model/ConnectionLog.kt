package com.example.local_network_scanner.data.model

/**
 * Connection log data model for UI display
 */
data class ConnectionLog(
    val id: Int = 0,
    val timestamp: Long,
    val appName: String,
    val packageName: String,
    val destinationIp: String,
    val destinationPort: Int,
    val protocol: String,
    val status: String, // "ALLOWED" or "BLOCKED"
    val isUnencrypted: Boolean = false
)
