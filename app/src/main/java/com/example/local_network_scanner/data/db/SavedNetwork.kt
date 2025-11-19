package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Saved network entity for network management
 */
@Entity(tableName = "saved_networks")
data class SavedNetwork(
    @PrimaryKey val ssid: String,
    val bssid: String,
    val securityType: SecurityType,
    val isTrusted: Boolean = false,
    val customDnsPrimary: String? = null,
    val customDnsSecondary: String? = null,
    val firewallPolicyId: Long? = null,
    val lastConnectedAt: Long? = null,
    val averageSignalStrength: Int = 0, // 0-100
    val totalDataUsed: Long = 0, // bytes
    val connectionCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SecurityType {
    OPEN, WEP, WPA, WPA2, WPA3
}
