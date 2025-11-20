package com.example.local_network_scanner.data.db

import androidx.room.TypeConverter

/**
 * Type converters for Room database
 */
class Converters {
    
    @TypeConverter
    fun fromSecurityType(value: SecurityType): String {
        return value.name
    }
    
    @TypeConverter
    fun toSecurityType(value: String): SecurityType {
        return try {
            SecurityType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            SecurityType.OPEN
        }
    }
    
    @TypeConverter
    fun fromDnsProvider(value: DnsProvider): String {
        return value.name
    }
    
    @TypeConverter
    fun toDnsProvider(value: String): DnsProvider {
        return try {
            DnsProvider.valueOf(value)
        } catch (e: IllegalArgumentException) {
            DnsProvider.CLOUDFLARE
        }
    }
}
