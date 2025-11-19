package com.example.local_network_scanner.services

import com.example.local_network_scanner.data.model.NetworkSpeed
import com.example.local_network_scanner.data.model.DataUsageStats
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for NetworkSpeed data class
 * Tests speed calculations and conversions
 */
class NetworkSpeedTest {
    
    @Test
    fun `networkSpeed converts bytes to Mbps correctly`() {
        // 1 MB/s = 8 Mbps
        val speed = NetworkSpeed(
            downloadBytesPerSecond = 1_000_000, // 1 MB/s
            uploadBytesPerSecond = 500_000      // 0.5 MB/s
        )
        
        assertEquals(8.0, speed.downloadMbps, 0.1)
        assertEquals(4.0, speed.uploadMbps, 0.1)
    }
    
    @Test
    fun `networkSpeed handles zero values`() {
        val speed = NetworkSpeed(
            downloadBytesPerSecond = 0,
            uploadBytesPerSecond = 0
        )
        
        assertEquals(0.0, speed.downloadMbps, 0.0)
        assertEquals(0.0, speed.uploadMbps, 0.0)
    }
    
    @Test
    fun `networkSpeed handles large values`() {
        val speed = NetworkSpeed(
            downloadBytesPerSecond = 100_000_000, // 100 MB/s
            uploadBytesPerSecond = 50_000_000     // 50 MB/s
        )
        
        assertEquals(800.0, speed.downloadMbps, 1.0)
        assertEquals(400.0, speed.uploadMbps, 1.0)
    }
    
    @Test
    fun `networkSpeed handles fractional Mbps`() {
        val speed = NetworkSpeed(
            downloadBytesPerSecond = 128_000, // ~1 Mbps
            uploadBytesPerSecond = 64_000     // ~0.5 Mbps
        )
        
        assertTrue(speed.downloadMbps > 0.9 && speed.downloadMbps < 1.1)
        assertTrue(speed.uploadMbps > 0.4 && speed.uploadMbps < 0.6)
    }
}

/**
 * Unit tests for DataUsageStats data class
 */
class DataUsageStatsTest {
    
    @Test
    fun `dataUsageStats creates with defaults`() {
        val stats = DataUsageStats()
        
        assertEquals(0L, stats.totalUpload)
        assertEquals(0L, stats.totalDownload)
        assertEquals(0, stats.activeAppsCount)
    }
    
    @Test
    fun `dataUsageStats creates with values`() {
        val stats = DataUsageStats(
            totalUpload = 1_000_000,
            totalDownload = 5_000_000,
            activeAppsCount = 5
        )
        
        assertEquals(1_000_000L, stats.totalUpload)
        assertEquals(5_000_000L, stats.totalDownload)
        assertEquals(5, stats.activeAppsCount)
    }
}

/**
 * Unit tests for SuspiciousApp data class and RiskLevel enum
 */
class SuspiciousAppTest {
    
    @Test
    fun `suspiciousApp creates correctly`() {
        val app = SuspiciousApp(
            packageName = "com.example.test",
            appName = "Test App",
            reasons = listOf("Permission issue", "High network usage"),
            riskLevel = RiskLevel.HIGH
        )
        
        assertEquals("com.example.test", app.packageName)
        assertEquals("Test App", app.appName)
        assertEquals(2, app.reasons.size)
        assertEquals(RiskLevel.HIGH, app.riskLevel)
    }
    
    @Test
    fun `riskLevel enum has correct values`() {
        val levels = RiskLevel.values()
        assertEquals(3, levels.size)
        assertTrue(levels.contains(RiskLevel.LOW))
        assertTrue(levels.contains(RiskLevel.MEDIUM))
        assertTrue(levels.contains(RiskLevel.HIGH))
    }
}

/**
 * Unit tests for NetworkDevice data class
 */
class NetworkDeviceTest {
    
    @Test
    fun `networkDevice creates correctly`() {
        val device = NetworkDevice(
            ipAddress = "192.168.1.1",
            deviceName = "Router",
            isReachable = true
        )
        
        assertEquals("192.168.1.1", device.ipAddress)
        assertEquals("Router", device.deviceName)
        assertTrue(device.isReachable)
    }
    
    @Test
    fun `networkDevice handles unreachable state`() {
        val device = NetworkDevice(
            ipAddress = "192.168.1.100",
            deviceName = "Unknown Device",
            isReachable = false
        )
        
        assertFalse(device.isReachable)
    }
}
