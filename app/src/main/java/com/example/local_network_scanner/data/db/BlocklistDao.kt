package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BlocklistDao {
    @Query("SELECT EXISTS(SELECT 1 FROM blocklist WHERE domain = :domain)")
    suspend fun isBlocked(domain: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<BlocklistEntry>)

    @Query("DELETE FROM blocklist")
    suspend fun clearAll()
}
