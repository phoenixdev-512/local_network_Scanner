package com.example.local_network_scanner.util

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages export and import of app configuration settings
 */
@Singleton
class ConfigurationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TAG = "ConfigurationManager"
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    /**
     * Exports settings to a JSON file
     * @param settings Map of setting keys and values
     * @return File object pointing to the exported configuration file
     */
    suspend fun exportToFile(settings: Map<String, Any>): Result<File> {
        return try {
            // Create config directory
            val configDir = File(context.getExternalFilesDir(null), "config")
            if (!configDir.exists()) {
                configDir.mkdirs()
            }
            
            // Create timestamped filename
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
            val configFile = File(configDir, "senet_config_$timestamp.json")
            
            // Convert settings to JSON-compatible map
            val jsonMap = settings.mapValues { (_, value) ->
                when (value) {
                    is Int -> value
                    is Long -> value
                    is Float -> value
                    is Double -> value
                    is Boolean -> value
                    is String -> value
                    else -> value.toString()
                }
            }
            
            // Write JSON to file
            val jsonString = json.encodeToString(jsonMap)
            configFile.writeText(jsonString)
            
            Log.i(TAG, "Configuration exported to: ${configFile.absolutePath}")
            Result.success(configFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting configuration", e)
            Result.failure(e)
        }
    }
    
    /**
     * Imports settings from a JSON file URI
     * @param uri Uri pointing to the configuration file
     * @return Map of setting keys and values
     */
    suspend fun importFromUri(uri: Uri): Result<Map<String, Any>> {
        return try {
            // Read file content
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            } ?: throw IllegalArgumentException("Unable to read file")
            
            // Parse JSON
            val settings = json.decodeFromString<Map<String, Any>>(jsonString)
            
            Log.i(TAG, "Configuration imported successfully from URI")
            Result.success(settings)
        } catch (e: Exception) {
            Log.e(TAG, "Error importing configuration", e)
            Result.failure(e)
        }
    }
    
    /**
     * Validates that imported settings are in correct format
     */
    fun validateSettings(settings: Map<String, Any>): Boolean {
        return try {
            // Basic validation - ensure it's a valid map with string keys
            settings.keys.all { it is String }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Clears old configuration files (keeps only last 3)
     */
    suspend fun clearOldConfigs() {
        try {
            val configDir = File(context.getExternalFilesDir(null), "config")
            if (configDir.exists()) {
                val configFiles = configDir.listFiles { file ->
                    file.name.startsWith("senet_config_") && file.name.endsWith(".json")
                }?.sortedByDescending { it.lastModified() } ?: emptyList()
                
                // Delete all but the 3 most recent
                configFiles.drop(3).forEach { file ->
                    file.delete()
                    Log.d(TAG, "Deleted old config file: ${file.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing old configs", e)
        }
    }
}
