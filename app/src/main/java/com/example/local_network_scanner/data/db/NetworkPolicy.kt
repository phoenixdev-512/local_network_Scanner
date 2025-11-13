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
    val dnsProvider: String = "CLOUDFLARE",
    val enableAdBlocking: Boolean = false,
    val enableMalwareProtection: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * DNS Provider options
 */
enum class DnsProvider(val displayName: String, val primaryDns: String, val secondaryDns: String) {
    CLOUDFLARE("Cloudflare", "1.1.1.1", "1.0.0.1"),
    GOOGLE("Google", "8.8.8.8", "8.8.4.4"),
    QUAD9("Quad9", "9.9.9.9", "149.112.112.112"),
    ADGUARD("AdGuard", "94.140.14.14", "94.140.15.15")
}
