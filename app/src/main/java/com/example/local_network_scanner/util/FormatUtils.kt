package com.example.local_network_scanner.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatUtils {
    
    /**
     * Format bytes to human-readable string
     */
    fun formatBytes(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024))
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024))
        }
    }
    
    /**
     * Format speed in bytes per second to Mbps
     */
    fun formatSpeed(bytesPerSecond: Long): String {
        val mbps = (bytesPerSecond * 8.0) / (1024 * 1024)
        return when {
            mbps < 1 -> String.format("%.2f Kbps", (bytesPerSecond * 8.0) / 1024)
            else -> String.format("%.2f Mbps", mbps)
        }
    }
    
    /**
     * Format timestamp to readable time
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to date
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to full date and time
     */
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format duration in milliseconds to readable string
     */
    fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "${days}d ${hours % 24}h"
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
    
    /**
     * Format ping in milliseconds with color coding
     */
    fun getPingQuality(ping: Int): String {
        return when {
            ping < 0 -> "N/A"
            ping < 50 -> "Excellent"
            ping < 100 -> "Good"
            ping < 200 -> "Fair"
            else -> "Poor"
        }
    }
    
    /**
     * Format percentage
     */
    fun formatPercentage(value: Float, total: Float): String {
        if (total == 0f) return "0%"
        val percentage = (value / total) * 100
        return String.format("%.1f%%", percentage)
    }
}
