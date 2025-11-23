package com.example.local_network_scanner.services

import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for performing IP geolocation lookups.
 * Uses MaxMind GeoLite2 database if available, falls back to simplified ranges.
 */
@Singleton
class GeoLocationService @Inject constructor(
    private val maxMindDbManager: MaxMindDbManager,
    private val simpleGeoLocationService: SimpleGeoLocationService
) {
    // Cache recently looked up IPs to reduce database queries
    private val ipCache = mutableMapOf<String, String?>()
    private val maxCacheSize = 1000
    private var useMaxMind = false
    
    init {
        // Initialize the database manager
        try {
            maxMindDbManager.initialize()
            useMaxMind = maxMindDbManager.getReader() != null
            if (useMaxMind) {
                android.util.Log.i("GeoLocationService", "Using MaxMind GeoLite2 database")
            } else {
                android.util.Log.i("GeoLocationService", "Using simplified IP ranges (MaxMind not available)")
            }
        } catch (e: Exception) {
            android.util.Log.w("GeoLocationService", "MaxMind initialization failed, using simplified ranges", e)
            useMaxMind = false
        }
    }
    
    /**
     * Get the ISO country code for the given IP address.
     * @param ipAddress IP address as a string (e.g., "8.8.8.8")
     * @return ISO 3166-1 alpha-2 country code (e.g., "US") or null if not found
     */
    fun getCountryCode(ipAddress: String): String? {
        // Skip private/local IPs
        if (simpleGeoLocationService.isPrivateIp(ipAddress)) {
            return null
        }
        
        // Check cache first
        if (ipCache.containsKey(ipAddress)) {
            return ipCache[ipAddress]
        }
        
        val countryCode = if (useMaxMind) {
            getCountryCodeFromMaxMind(ipAddress)
        } else {
            simpleGeoLocationService.getCountryCode(ipAddress)
        }
        
        // Cache the result
        if (ipCache.size >= maxCacheSize) {
            // Remove oldest entry (simple LRU)
            ipCache.remove(ipCache.keys.first())
        }
        ipCache[ipAddress] = countryCode
        
        return countryCode
    }
    
    private fun getCountryCodeFromMaxMind(ipAddress: String): String? {
        val reader = maxMindDbManager.getReader() ?: return simpleGeoLocationService.getCountryCode(ipAddress)
        
        return try {
            val address = InetAddress.getByName(ipAddress)
            val response = reader.country(address)
            response.country?.isoCode
        } catch (e: Exception) {
            // Fallback to simplified service
            android.util.Log.d("GeoLocationService", "MaxMind lookup failed, using fallback for: $ipAddress", e)
            simpleGeoLocationService.getCountryCode(ipAddress)
        }
    }
    
    /**
     * Get the full country name for the given IP address.
     * @param ipAddress IP address as a string
     * @return Country name or null if not found
     */
    fun getCountryName(ipAddress: String): String? {
        if (!useMaxMind) return null
        
        val reader = maxMindDbManager.getReader() ?: return null
        
        return try {
            val address = InetAddress.getByName(ipAddress)
            val response = reader.country(address)
            response.country?.name
        } catch (e: Exception) {
            android.util.Log.d("GeoLocationService", "Failed to lookup country name for IP: $ipAddress", e)
            null
        }
    }
    
    /**
     * Clear the IP lookup cache.
     */
    fun clearCache() {
        ipCache.clear()
    }
}
