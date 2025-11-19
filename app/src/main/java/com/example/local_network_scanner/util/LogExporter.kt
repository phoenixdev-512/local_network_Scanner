package com.example.local_network_scanner.util

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for exporting application logs to files
 */
@Singleton
class LogExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TAG = "LogExporter"
    
    /**
     * Exports logcat output to a timestamped file in external files directory
     * @return File object pointing to the exported log file
     * @throws IOException if file creation or log export fails
     */
    suspend fun exportLogs(): Result<File> {
        return try {
            // Create logs directory
            val logsDir = File(context.getExternalFilesDir(null), "logs")
            if (!logsDir.exists()) {
                logsDir.mkdirs()
            }
            
            // Create timestamped filename
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
            val logFile = File(logsDir, "senet_logs_$timestamp.txt")
            
            // Execute logcat command
            val process = Runtime.getRuntime().exec("logcat -d -v time *:V")
            val inputStream = process.inputStream
            
            // Write to file
            logFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            
            // Wait for process to complete
            process.waitFor()
            
            Log.i(TAG, "Logs exported successfully to: ${logFile.absolutePath}")
            Result.success(logFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting logs", e)
            Result.failure(e)
        }
    }
    
    /**
     * Clears old log files (keeps only last 5)
     */
    suspend fun clearOldLogs() {
        try {
            val logsDir = File(context.getExternalFilesDir(null), "logs")
            if (logsDir.exists()) {
                val logFiles = logsDir.listFiles { file ->
                    file.name.startsWith("senet_logs_") && file.name.endsWith(".txt")
                }?.sortedByDescending { it.lastModified() } ?: emptyList()
                
                // Delete all but the 5 most recent
                logFiles.drop(5).forEach { file ->
                    file.delete()
                    Log.d(TAG, "Deleted old log file: ${file.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing old logs", e)
        }
    }
}
