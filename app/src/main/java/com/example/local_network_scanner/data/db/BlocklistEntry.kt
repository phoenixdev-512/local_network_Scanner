package com.example.local_network_scanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocklist")
data class BlocklistEntry(
    @PrimaryKey val domain: String,
    val type: Int // e.g., Type 1=Ad, Type 2=Malware
)
