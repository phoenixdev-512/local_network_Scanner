package com.example.local_network_scanner.data.models

/**
 * Data model representing a country with ISO code and flag emoji.
 * Used for displaying available countries in the geo-blocking UI.
 * 
 * @property code ISO 3166-1 alpha-2 country code (e.g., "US", "CN")
 * @property name Human-readable country name
 * @property flagEmoji Unicode flag emoji for visual representation
 */
data class Country(
    val code: String,
    val name: String,
    val flagEmoji: String
)
