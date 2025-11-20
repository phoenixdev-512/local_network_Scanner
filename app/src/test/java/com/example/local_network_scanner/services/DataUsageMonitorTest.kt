package com.example.local_network_scanner.services

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for DataUsageMonitor data classes
 */
class DataUsageMonitorTest {
    
    @Test
    fun `DataUsageStats calculates total correctly`() {
        val stats = DataUsageStats(
            totalRx = 1_000_000,
            totalTx = 500_000,
            mobileRx = 600_000,
            mobileTx = 300_000,
            wifiRx = 400_000,
            wifiTx = 200_000
        )
        
        assertEquals(1_500_000L, stats.total)
        assertEquals(900_000L, stats.mobile)
        assertEquals(600_000L, stats.wifi)
    }
    
    @Test
    fun `DataUsageStats handles zero values`() {
        val stats = DataUsageStats(
            totalRx = 0,
            totalTx = 0,
            mobileRx = 0,
            mobileTx = 0,
            wifiRx = 0,
            wifiTx = 0
        )
        
        assertEquals(0L, stats.total)
        assertEquals(0L, stats.mobile)
        assertEquals(0L, stats.wifi)
    }
    
    @Test
    fun `AppDataUsage calculates total correctly`() {
        val appUsage = AppDataUsage(
            uid = 10001,
            packageName = "com.example.app",
            appName = "Example App",
            downloadBytes = 1_000_000,
            uploadBytes = 500_000
        )
        
        assertEquals(1_500_000L, appUsage.total)
        assertEquals("com.example.app", appUsage.packageName)
        assertEquals("Example App", appUsage.appName)
    }
    
    @Test
    fun `AppDataUsage handles zero values`() {
        val appUsage = AppDataUsage(
            uid = 10002,
            packageName = "com.example.app2",
            appName = "Example App 2",
            downloadBytes = 0,
            uploadBytes = 0
        )
        
        assertEquals(0L, appUsage.total)
    }
    
    @Test
    fun `TimeRange enum has correct values`() {
        val ranges = TimeRange.values()
        assertEquals(3, ranges.size)
        assertTrue(ranges.contains(TimeRange.TODAY))
        assertTrue(ranges.contains(TimeRange.WEEK))
        assertTrue(ranges.contains(TimeRange.MONTH))
    }
}
