package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing and modifying blocked countries in the database.
 */
@Dao
interface BlockedCountryDao {
    
    @Query("SELECT * FROM blocked_countries WHERE isBlocked = 1")
    fun getBlockedCountries(): Flow<List<BlockedCountry>>
    
    @Query("SELECT * FROM blocked_countries")
    fun getAllCountries(): Flow<List<BlockedCountry>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: BlockedCountry)
    
    @Query("UPDATE blocked_countries SET isBlocked = :isBlocked WHERE countryCode = :code")
    suspend fun updateBlockStatus(code: String, isBlocked: Boolean)
    
    @Delete
    suspend fun deleteCountry(country: BlockedCountry)
    
    @Query("SELECT isBlocked FROM blocked_countries WHERE countryCode = :code")
    suspend fun isCountryBlocked(code: String): Boolean?
    
    @Query("DELETE FROM blocked_countries")
    suspend fun clearAll()
}
