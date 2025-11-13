package com.example.local_network_scanner.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for SpeedTestResult operations
 */
@Dao
interface SpeedTestResultDao {
    @Query("SELECT * FROM speed_test_results ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentResults(limit: Int = 10): Flow<List<SpeedTestResult>>
    
    @Query("SELECT * FROM speed_test_results WHERE ssid = :ssid ORDER BY timestamp DESC")
    fun getResultsForNetwork(ssid: String): Flow<List<SpeedTestResult>>
    
    @Query("SELECT AVG(downloadSpeed) FROM speed_test_results WHERE ssid = :ssid")
    suspend fun getAverageDownloadSpeed(ssid: String): Double?
    
    @Query("SELECT AVG(uploadSpeed) FROM speed_test_results WHERE ssid = :ssid")
    suspend fun getAverageUploadSpeed(ssid: String): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: SpeedTestResult): Long
    
    @Delete
    suspend fun deleteResult(result: SpeedTestResult)
    
    @Query("DELETE FROM speed_test_results WHERE timestamp < :timestamp")
    suspend fun deleteOldResults(timestamp: Long)
}
