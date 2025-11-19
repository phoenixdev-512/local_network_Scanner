package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for NetworkAnalytics operations
 */
@Dao
interface NetworkAnalyticsDao {
    @Query("SELECT * FROM network_analytics WHERE ssid = :ssid AND timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getAnalytics(ssid: String, startTime: Long, endTime: Long): Flow<List<NetworkAnalytics>>
    
    @Insert
    suspend fun insertAnalytics(analytics: NetworkAnalytics)
    
    @Query("DELETE FROM network_analytics WHERE timestamp < :cutoffTime")
    suspend fun deleteOldAnalytics(cutoffTime: Long)
}
