package com.example.local_network_scanner.data.repository

import com.example.local_network_scanner.data.db.BlockedCountry
import com.example.local_network_scanner.data.db.BlockedCountryDao
import com.example.local_network_scanner.services.GeoLocationService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing geo-blocking functionality.
 * Provides access to blocked countries and IP-based blocking decisions.
 */
@Singleton
class GeoBlockRepository @Inject constructor(
    private val blockedCountryDao: BlockedCountryDao,
    private val geoLocationService: GeoLocationService
) {
    /**
     * Get all blocked countries as a Flow.
     */
    fun getBlockedCountries(): Flow<List<BlockedCountry>> = 
        blockedCountryDao.getBlockedCountries()
    
    /**
     * Get all countries (blocked and unblocked) as a Flow.
     */
    fun getAllCountries(): Flow<List<BlockedCountry>> =
        blockedCountryDao.getAllCountries()
    
    /**
     * Add a country to the blocked list.
     */
    suspend fun addBlockedCountry(countryCode: String, countryName: String) {
        blockedCountryDao.insertCountry(
            BlockedCountry(
                countryCode = countryCode,
                countryName = countryName,
                isBlocked = true
            )
        )
    }
    
    /**
     * Toggle the block status of a country.
     */
    suspend fun toggleCountryBlock(countryCode: String, isBlocked: Boolean) {
        blockedCountryDao.updateBlockStatus(countryCode, isBlocked)
    }
    
    /**
     * Remove a country from the database entirely.
     */
    suspend fun removeBlockedCountry(country: BlockedCountry) {
        blockedCountryDao.deleteCountry(country)
    }
    
    /**
     * Check if a specific IP address should be blocked based on its country.
     * @param ipAddress The IP address to check
     * @return true if the IP's country is blocked, false otherwise
     */
    suspend fun isIpBlocked(ipAddress: String): Boolean {
        val countryCode = geoLocationService.getCountryCode(ipAddress)
        return if (countryCode != null) {
            blockedCountryDao.isCountryBlocked(countryCode) ?: false
        } else {
            false
        }
    }
    
    /**
     * Clear all blocked countries from the database.
     */
    suspend fun clearAll() {
        blockedCountryDao.clearAll()
    }
}
