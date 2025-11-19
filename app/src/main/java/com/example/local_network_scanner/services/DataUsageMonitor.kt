package com.example.local_network_scanner.services

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import android.os.Build
import com.example.local_network_scanner.data.model.DataUsageStats
import com.example.local_network_scanner.data.model.TimeRange
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataUsageMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val networkStatsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(Context.NETWORK_STATS_SERVICE) as? NetworkStatsManager
    } else {
        null
    }
    
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    /**
     * Get data usage statistics for the specified time range
     * Uses NetworkStatsManager for API 23+ for accurate stats
     * Falls back to TrafficStats for older versions
     */
    suspend fun getDataUsageStats(timeRange: TimeRange): DataUsageStats = withContext(Dispatchers.IO) {
        try {
            val (startTime, endTime) = getTimeRangeBounds(timeRange)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && networkStatsManager != null) {
                // Use NetworkStatsManager for more accurate data
                return@withContext getNetworkStatsManagerData(startTime, endTime)
            } else {
                // Fallback to TrafficStats (total device usage since boot)
                return@withContext getTrafficStatsData()
            }
        } catch (e: Exception) {
            android.util.Log.e("DataUsageMonitor", "Error getting data usage stats", e)
            DataUsageStats()
        }
    }
    
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.M)
    private fun getNetworkStatsManagerData(startTime: Long, endTime: Long): DataUsageStats {
        try {
            var wifiRx = 0L
            var wifiTx = 0L
            var mobileRx = 0L
            var mobileTx = 0L
            
            // Get WiFi usage
            val wifiBucket = networkStatsManager?.querySummaryForDevice(
                ConnectivityManager.TYPE_WIFI,
                null,
                startTime,
                endTime
            )
            
            if (wifiBucket != null) {
                wifiRx = wifiBucket.rxBytes
                wifiTx = wifiBucket.txBytes
            }
            
            // Get Mobile usage
            val mobileBucket = networkStatsManager?.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,
                startTime,
                endTime
            )
            
            if (mobileBucket != null) {
                mobileRx = mobileBucket.rxBytes
                mobileTx = mobileBucket.txBytes
            }
            
            val totalWifi = wifiRx + wifiTx
            val totalMobile = mobileRx + mobileTx
            val totalDownload = wifiRx + mobileRx
            val totalUpload = wifiTx + mobileTx
            val total = totalWifi + totalMobile
            
            return DataUsageStats(
                total = total,
                wifi = totalWifi,
                mobile = totalMobile,
                download = totalDownload,
                upload = totalUpload,
                apps = total // Apps data included in total
            )
        } catch (e: Exception) {
            android.util.Log.e("DataUsageMonitor", "Error with NetworkStatsManager", e)
            return getTrafficStatsData()
        }
    }
    
    private fun getTrafficStatsData(): DataUsageStats {
        try {
            val totalRx = TrafficStats.getTotalRxBytes()
            val totalTx = TrafficStats.getTotalTxBytes()
            val mobileRx = TrafficStats.getMobileRxBytes()
            val mobileTx = TrafficStats.getMobileTxBytes()
            
            val totalMobile = mobileRx + mobileTx
            val total = totalRx + totalTx
            val totalWifi = total - totalMobile
            
            return DataUsageStats(
                total = total.coerceAtLeast(0),
                wifi = totalWifi.coerceAtLeast(0),
                mobile = totalMobile.coerceAtLeast(0),
                download = totalRx.coerceAtLeast(0),
                upload = totalTx.coerceAtLeast(0),
                apps = total.coerceAtLeast(0)
            )
        } catch (e: Exception) {
            android.util.Log.e("DataUsageMonitor", "Error with TrafficStats", e)
            return DataUsageStats()
        }
    }
    
    private fun getTimeRangeBounds(timeRange: TimeRange): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        
        when (timeRange) {
            TimeRange.TODAY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            TimeRange.THIS_WEEK -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            TimeRange.THIS_MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            TimeRange.CUSTOM -> {
                // For custom, use last 30 days as default
                calendar.add(Calendar.DAY_OF_MONTH, -30)
            }
        }
        
        val startTime = calendar.timeInMillis
        return Pair(startTime, endTime)
    }
}
