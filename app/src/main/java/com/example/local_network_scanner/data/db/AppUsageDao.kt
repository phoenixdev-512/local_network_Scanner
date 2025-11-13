package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {
    @Query("""
        INSERT INTO app_usage_daily (packageName, date, dataUsedBytes)
        VALUES (:packageName, :date, :bytes)
        ON CONFLICT(packageName, date) DO UPDATE SET
        dataUsedBytes = dataUsedBytes + :bytes
    """)
    suspend fun incrementUsage(packageName: String, date: String, bytes: Long)

    @Query("SELECT * FROM app_usage_daily WHERE date = :date")
    fun getUsageForDay(date: String): Flow<List<AppUsageDaily>>
}
