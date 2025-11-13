package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Saved network entity for network management
 */
@Entity(tableName = "saved_networks")
data class SavedNetwork(
    @PrimaryKey val ssid: String,
    val bssid: String,
    val securityType: String,
    val isTrusted: Boolean = false,
    val customDns: String? = null,
    val firewallPolicyId: Long? = null,
    val lastConnected: Long? = null,
    val averageSpeed: Double = 0.0,
    val signalStrength: Int = 0
)
