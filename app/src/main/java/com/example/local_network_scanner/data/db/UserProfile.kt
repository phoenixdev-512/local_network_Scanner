package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User profile entity with role-based access control
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val role: UserRole,
    val avatarUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false,
    
    // Profile-specific preferences
    val autoStartVpn: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val customDnsServer: String? = null,
    val firewallRulesJson: String = "[]", // JSON array of custom firewall rules
    val blockedAppsJson: String = "[]", // JSON array of blocked package names
    
    // Legacy fields for backwards compatibility
    val darkMode: Boolean = true,
    val selectedTheme: String = "default"
)

/**
 * User role enumeration
 * ADMIN: Full access - create/edit/delete profiles, advanced settings, security configs
 * STANDARD: Limited access - view-only for logs, cannot modify security settings
 */
enum class UserRole {
    ADMIN,
    STANDARD
}
