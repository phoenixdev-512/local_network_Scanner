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
    val autoStartVpn: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val darkMode: Boolean = true,
    val selectedTheme: String = "default"
)

/**
 * User role enumeration
 * ADMIN: Full firewall configuration and network-wide policy management
 * STANDARD: Basic network monitoring with view-only access to logs
 */
enum class UserRole {
    ADMIN,
    STANDARD
}
