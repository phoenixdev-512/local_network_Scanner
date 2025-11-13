package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStatsDao {
    @Query("""
        INSERT INTO daily_stats (date, connectionsBlocked)
        VALUES (:date, :count)
        ON CONFLICT(date) DO UPDATE SET
        connectionsBlocked = connectionsBlocked + :count
    """)
    suspend fun incrementConnectionsBlocked(date: String, count: Long)

    @Query("SELECT * FROM daily_stats WHERE date >= :since")
    fun getStatsSince(since: String): Flow<List<DailyStats>>
}
