package com.example.local_network_scanner.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for SavedNetwork operations
 */
@Dao
interface SavedNetworkDao {
    @Query("SELECT * FROM saved_networks ORDER BY lastConnected DESC")
    fun getAllNetworks(): Flow<List<SavedNetwork>>
    
    @Query("SELECT * FROM saved_networks WHERE ssid = :ssid")
    suspend fun getNetworkBySsid(ssid: String): SavedNetwork?
    
    @Query("SELECT * FROM saved_networks WHERE isTrusted = 1")
    fun getTrustedNetworks(): Flow<List<SavedNetwork>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetwork(network: SavedNetwork)
    
    @Update
    suspend fun updateNetwork(network: SavedNetwork)
    
    @Delete
    suspend fun deleteNetwork(network: SavedNetwork)
    
    @Query("DELETE FROM saved_networks WHERE ssid = :ssid")
    suspend fun deleteNetworkBySsid(ssid: String)
}
