package com.example.local_network_scanner.services

import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for performing IP geolocation lookups using MaxMind GeoLite2 database.
 * Provides country code resolution for IP addresses.
 */
@Singleton
class GeoLocationService @Inject constructor(
    private val maxMindDbManager: MaxMindDbManager
) {
    // Cache recently looked up IPs to reduce database queries
    private val ipCache = mutableMapOf<String, String?>()
    private val maxCacheSize = 1000
    
    init {
        // Initialize the database manager
        maxMindDbManager.initialize()
    }
    
    /**
     * Get the ISO country code for the given IP address.
     * @param ipAddress IP address as a string (e.g., "8.8.8.8")
     * @return ISO 3166-1 alpha-2 country code (e.g., "US") or null if not found
     */
    fun getCountryCode(ipAddress: String): String? {
        // Check cache first
        if (ipCache.containsKey(ipAddress)) {
            return ipCache[ipAddress]
        }
        
        val reader = maxMindDbManager.getReader() ?: return null
        
        return try {
            val address = InetAddress.getByName(ipAddress)
            val response = reader.country(address)
            val countryCode = response.country?.isoCode
            
            // Cache the result
            if (ipCache.size >= maxCacheSize) {
                // Remove oldest entry (simple LRU)
                ipCache.remove(ipCache.keys.first())
            }
            ipCache[ipAddress] = countryCode
            
            countryCode
        } catch (e: Exception) {
            // IP not found in database or invalid format
            android.util.Log.d("GeoLocationService", "Failed to lookup IP: $ipAddress", e)
            null
        }
    }
    
    /**
     * Get the full country name for the given IP address.
     * @param ipAddress IP address as a string
     * @return Country name or null if not found
     */
    fun getCountryName(ipAddress: String): String? {
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
