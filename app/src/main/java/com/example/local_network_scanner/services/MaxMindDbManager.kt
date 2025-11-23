package com.example.local_network_scanner.services

import android.content.Context
import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.model.CountryResponse
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the MaxMind GeoLite2 database lifecycle.
 * Handles extracting from assets and providing a DatabaseReader instance.
 */
@Singleton
class MaxMindDbManager @Inject constructor(
    private val context: Context
) {
    private var databaseReader: DatabaseReader? = null
    private val databaseFile = File(context.filesDir, "GeoLite2-Country.mmdb")
    
    /**
     * Initialize the database reader by extracting from assets if needed.
     */
    fun initialize() {
        try {
            // Extract database from assets to internal storage if not already done
            if (!databaseFile.exists()) {
                extractDatabaseFromAssets()
            }
            
            // Create database reader with cache for better performance
            databaseReader = DatabaseReader.Builder(databaseFile)
                .withCache(CHMCache())
                .build()
        } catch (e: Exception) {
            // Log error - database file might not be in assets yet
            android.util.Log.e("MaxMindDbManager", "Failed to initialize GeoIP database", e)
        }
    }
    
    /**
     * Extract the .mmdb file from assets to internal storage.
     */
    private fun extractDatabaseFromAssets() {
        try {
            context.assets.open("GeoLite2-Country.mmdb").use { input ->
                FileOutputStream(databaseFile).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MaxMindDbManager", "Failed to extract GeoIP database from assets", e)
            throw e
        }
    }
    
    /**
     * Get the DatabaseReader instance.
     * @return DatabaseReader or null if not initialized
     */
    fun getReader(): DatabaseReader? = databaseReader
    
    /**
     * Close the database reader and release resources.
     */
    fun close() {
        databaseReader?.close()
        databaseReader = null
    }
}
