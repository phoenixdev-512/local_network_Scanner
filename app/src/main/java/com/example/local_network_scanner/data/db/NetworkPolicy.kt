package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Network policy entity for per-network firewall rules
 */
@Entity(tableName = "network_policies")
data class NetworkPolicy(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val allowedAppsJson: String = "[]", // JSON array of package names
    val blockedDomainsJson: String = "[]", // JSON array of domains
    val dnsProvider: DnsProvider,
    val customDnsPrimary: String? = null,
    val customDnsSecondary: String? = null,
    val enableAdBlocking: Boolean = false,
    val enableMalwareProtection: Boolean = true,
    val enableTrackerBlocking: Boolean = false,
    val blockedPortsJson: String = "[]", // JSON array of port numbers
    val isDefault: Boolean = false,
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class DnsProvider {
    CLOUDFLARE, GOOGLE, QUAD9, OPENDNS, CUSTOM
}
