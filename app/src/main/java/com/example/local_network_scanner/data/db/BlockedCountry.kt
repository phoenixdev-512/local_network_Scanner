package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a blocked country for geo-blocking feature.
 * 
 * @property countryCode ISO 3166-1 alpha-2 country code (e.g., "CN", "RU")
 * @property countryName Human-readable country name
 * @property isBlocked Whether this country is currently blocked
 * @property addedDate Timestamp when the country was added to the block list
 */
@Entity(tableName = "blocked_countries")
data class BlockedCountry(
    @PrimaryKey val countryCode: String,
    val countryName: String,
    val isBlocked: Boolean = true,
    val addedDate: Long = System.currentTimeMillis()
)
