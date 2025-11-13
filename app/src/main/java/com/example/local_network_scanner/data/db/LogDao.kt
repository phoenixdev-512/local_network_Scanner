package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(logEntry: LogEntry)

    @Query("SELECT * FROM traffic_log ORDER BY timestamp DESC")
    fun getAll(): Flow<List<LogEntry>>

    @Query("DELETE FROM traffic_log")
    suspend fun deleteAll()
}
