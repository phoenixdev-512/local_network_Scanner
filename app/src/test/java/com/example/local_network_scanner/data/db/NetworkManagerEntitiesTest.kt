package com.example.local_network_scanner.data.db

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Network Manager entities
 */
class NetworkManagerEntitiesTest {
    
    @Test
    fun `SavedNetwork creates with required fields`() {
        val network = SavedNetwork(
            ssid = "Test WiFi",
            bssid = "00:11:22:33:44:55",
            securityType = SecurityType.WPA2,
            isTrusted = true
        )
        
        assertEquals("Test WiFi", network.ssid)
        assertEquals("00:11:22:33:44:55", network.bssid)
        assertEquals(SecurityType.WPA2, network.securityType)
        assertTrue(network.isTrusted)
        assertEquals(0, network.averageSignalStrength)
        assertEquals(0L, network.totalDataUsed)
        assertEquals(0, network.connectionCount)
    }
    
    @Test
    fun `SavedNetwork creates with optional fields`() {
        val network = SavedNetwork(
            ssid = "Home WiFi",
            bssid = "AA:BB:CC:DD:EE:FF",
            securityType = SecurityType.WPA3,
            isTrusted = true,
            customDnsPrimary = "1.1.1.1",
            customDnsSecondary = "1.0.0.1",
            firewallPolicyId = 1L,
            lastConnectedAt = System.currentTimeMillis(),
            averageSignalStrength = 85,
            totalDataUsed = 1_000_000L,
            connectionCount = 10
        )
        
        assertEquals("1.1.1.1", network.customDnsPrimary)
        assertEquals("1.0.0.1", network.customDnsSecondary)
        assertEquals(1L, network.firewallPolicyId)
        assertNotNull(network.lastConnectedAt)
        assertEquals(85, network.averageSignalStrength)
        assertEquals(1_000_000L, network.totalDataUsed)
        assertEquals(10, network.connectionCount)
    }
    
    @Test
    fun `SecurityType enum has all expected values`() {
        val types = SecurityType.values()
        assertEquals(5, types.size)
        assertTrue(types.contains(SecurityType.OPEN))
        assertTrue(types.contains(SecurityType.WEP))
        assertTrue(types.contains(SecurityType.WPA))
        assertTrue(types.contains(SecurityType.WPA2))
        assertTrue(types.contains(SecurityType.WPA3))
    }
    
    @Test
    fun `NetworkPolicy creates with required fields`() {
        val policy = NetworkPolicy(
            name = "Test Policy",
            description = "Test Description",
            dnsProvider = DnsProvider.CLOUDFLARE
        )
        
        assertEquals("Test Policy", policy.name)
        assertEquals("Test Description", policy.description)
        assertEquals(DnsProvider.CLOUDFLARE, policy.dnsProvider)
        assertEquals("[]", policy.allowedAppsJson)
        assertEquals("[]", policy.blockedDomainsJson)
        assertFalse(policy.enableAdBlocking)
        assertTrue(policy.enableMalwareProtection)
        assertFalse(policy.enableTrackerBlocking)
        assertEquals("[]", policy.blockedPortsJson)
        assertFalse(policy.isDefault)
        assertFalse(policy.isActive)
    }
    
    @Test
    fun `NetworkPolicy creates with all fields`() {
        val policy = NetworkPolicy(
            id = 1L,
            name = "Strict Policy",
            description = "Maximum security",
            allowedAppsJson = """["com.example.app"]""",
            blockedDomainsJson = """["ads.example.com"]""",
            dnsProvider = DnsProvider.QUAD9,
            customDnsPrimary = "9.9.9.9",
            customDnsSecondary = "149.112.112.112",
            enableAdBlocking = true,
            enableMalwareProtection = true,
            enableTrackerBlocking = true,
            blockedPortsJson = """[80, 443]""",
            isDefault = true,
            isActive = true
        )
        
        assertEquals(1L, policy.id)
        assertEquals("Strict Policy", policy.name)
        assertEquals(DnsProvider.QUAD9, policy.dnsProvider)
        assertEquals("9.9.9.9", policy.customDnsPrimary)
        assertTrue(policy.enableAdBlocking)
        assertTrue(policy.enableMalwareProtection)
        assertTrue(policy.enableTrackerBlocking)
        assertTrue(policy.isDefault)
        assertTrue(policy.isActive)
    }
    
    @Test
    fun `DnsProvider enum has all expected values`() {
        val providers = DnsProvider.values()
        assertEquals(5, providers.size)
        assertTrue(providers.contains(DnsProvider.CLOUDFLARE))
        assertTrue(providers.contains(DnsProvider.GOOGLE))
        assertTrue(providers.contains(DnsProvider.QUAD9))
        assertTrue(providers.contains(DnsProvider.OPENDNS))
        assertTrue(providers.contains(DnsProvider.CUSTOM))
    }
    
    @Test
    fun `NetworkAnalytics creates correctly`() {
        val analytics = NetworkAnalytics(
            ssid = "Test Network",
            timestamp = System.currentTimeMillis(),
            downloadSpeed = 100.0,
            uploadSpeed = 50.0,
            signalStrength = 80,
            dataUsed = 1_000_000L,
            connectionDuration = 3600000L,
            threatsBlocked = 5
        )
        
        assertEquals("Test Network", analytics.ssid)
        assertEquals(100.0, analytics.downloadSpeed, 0.01)
        assertEquals(50.0, analytics.uploadSpeed, 0.01)
        assertEquals(80, analytics.signalStrength)
        assertEquals(1_000_000L, analytics.dataUsed)
        assertEquals(3600000L, analytics.connectionDuration)
        assertEquals(5, analytics.threatsBlocked)
    }
}

/**
 * Unit tests for TypeConverters
 */
class ConvertersTest {
    
    private val converters = Converters()
    
    @Test
    fun `converts SecurityType to String and back`() {
        SecurityType.values().forEach { type ->
            val string = converters.fromSecurityType(type)
            val converted = converters.toSecurityType(string)
            assertEquals(type, converted)
        }
    }
    
    @Test
    fun `converts invalid SecurityType string to OPEN`() {
        val result = converters.toSecurityType("INVALID")
        assertEquals(SecurityType.OPEN, result)
    }
    
    @Test
    fun `converts DnsProvider to String and back`() {
        DnsProvider.values().forEach { provider ->
            val string = converters.fromDnsProvider(provider)
            val converted = converters.toDnsProvider(string)
            assertEquals(provider, converted)
        }
    }
    
    @Test
    fun `converts invalid DnsProvider string to CLOUDFLARE`() {
        val result = converters.toDnsProvider("INVALID")
        assertEquals(DnsProvider.CLOUDFLARE, result)
    }
}
