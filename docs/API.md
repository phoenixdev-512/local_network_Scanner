# NetSentry API Documentation

## Overview

This document provides detailed API documentation for NetSentry's core services and ViewModels. All classes are written in Kotlin and use coroutines for asynchronous operations.

## Core Services

### NetworkMonitor

**Package**: `com.example.local_network_scanner.services`  
**Scope**: Singleton  
**Purpose**: Real-time network speed and latency monitoring

#### Properties

```kotlin
val networkSpeed: StateFlow<NetworkSpeed>
```
Reactive stream of network speed measurements.

**NetworkSpeed Data Class**:
```kotlin
data class NetworkSpeed(
    val downloadBytesPerSecond: Long = 0,
    val uploadBytesPerSecond: Long = 0
) {
    val downloadMbps: Double
    val uploadMbps: Double
}
```

```kotlin
val ping: StateFlow<Int>
```
Current ping latency in milliseconds. Returns -1 if network is unavailable.

```kotlin
val isMonitoring: StateFlow<Boolean>
```
Indicates whether monitoring is currently active.

#### Methods

```kotlin
fun startMonitoring()
```
Begins network speed and ping monitoring with periodic updates.

**Update Interval**: 0.5 seconds (normal mode), 2 seconds (power save mode)

```kotlin
fun stopMonitoring()
```
Stops all monitoring operations and cancels background jobs.

```kotlin
private fun measureSpeed()
```
Internal method that calculates network speed using TrafficStats API.

```kotlin
private suspend fun measurePing()
```
Internal method that measures ping latency by connecting to 8.8.8.8:53.

#### Usage Example

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    val networkSpeed = networkMonitor.networkSpeed
    val ping = networkMonitor.ping
    
    init {
        networkMonitor.startMonitoring()
    }
}
```

---

### SecurityAnalyzer

**Package**: `com.example.local_network_scanner.services`  
**Scope**: Singleton  
**Purpose**: Application security analysis and threat detection

#### Properties

```kotlin
val securityScore: StateFlow<Int>
```
Security score from 0-100, where higher values indicate better security.

```kotlin
val threatsDetected: StateFlow<Int>
```
Number of high-risk threats currently detected.

```kotlin
val activeConnections: StateFlow<Int>
```
Number of active network connections.

```kotlin
val appsWithNetworkAccess: StateFlow<Int>
```
Total count of apps with INTERNET permission.

```kotlin
val suspiciousApps: StateFlow<List<SuspiciousApp>>
```
List of apps flagged as potentially suspicious.

#### Methods

```kotlin
suspend fun calculateSecurityScore(): Int
```
Calculates comprehensive security score based on multiple factors.

**Scoring Factors**:
- Threat count (-5 points each, max -30)
- Suspicious apps (-10 points each, max -25)
- Excessive network apps (-15 points max)
- Unusual connections (-15 points max)

**Returns**: Security score (0-100)

```kotlin
suspend fun scanForSuspiciousApps(): List<SuspiciousApp>
```
Scans all installed apps for suspicious behavior.

**Detection Criteria**:
- Dangerous permission combinations
- Excessive network usage (>100MB)
- Unknown sources

**Returns**: List of suspicious apps with risk levels

```kotlin
suspend fun countAppsWithNetworkAccess(): Int
```
Counts apps with INTERNET permission.

**Returns**: Total count of network-capable apps

```kotlin
fun updateActiveConnections(count: Int)
```
Updates the active connections count.

**Parameters**:
- `count`: Current number of active connections

```kotlin
fun updateThreatsDetected(count: Int)
```
Updates the threats detected count.

**Parameters**:
- `count`: Current number of threats

#### Data Classes

```kotlin
data class SuspiciousApp(
    val packageName: String,
    val appName: String,
    val reasons: List<String>,
    val riskLevel: RiskLevel
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}
```

#### Usage Example

```kotlin
@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityAnalyzer: SecurityAnalyzer
) : ViewModel() {
    val securityScore = securityAnalyzer.securityScore
    
    fun performScan() {
        viewModelScope.launch {
            val suspicious = securityAnalyzer.scanForSuspiciousApps()
            // Handle results
        }
    }
}
```

---

### DeviceScanner

**Package**: `com.example.local_network_scanner.services`  
**Scope**: Singleton  
**Purpose**: Local network device discovery

#### Properties

```kotlin
val connectedDevicesCount: StateFlow<Int>
```
Number of devices detected on the local network.

```kotlin
val discoveredDevices: StateFlow<List<NetworkDevice>>
```
List of discovered network devices with details.

#### Methods

```kotlin
suspend fun scanNetwork(): Int
```
Performs comprehensive network scan to discover devices.

**Process**:
1. Get current device IP and gateway
2. Scan common IP ranges (1-2, 10-20, 50-60, 100-110, 200-210)
3. Test reachability with 100ms timeout
4. Collect device information

**Returns**: Number of reachable devices found

**Performance**: Optimized to scan ~75 IPs instead of full 254 range

```kotlin
suspend fun estimateDeviceCount(): Int
```
Provides quick device count estimation without full scan.

**Returns**: Estimated device count (3-8 for typical home network)

#### Data Classes

```kotlin
data class NetworkDevice(
    val ipAddress: String,
    val deviceName: String,
    val isReachable: Boolean
)
```

#### Usage Example

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceScanner: DeviceScanner
) : ViewModel() {
    val connectedDevices = deviceScanner.connectedDevicesCount
    
    fun scanNetwork() {
        viewModelScope.launch {
            val count = deviceScanner.scanNetwork()
            // Handle results
        }
    }
}
```

---

## ViewModels

### DashboardViewModel

**Package**: `com.example.local_network_scanner.ui.viewmodel`  
**Scope**: ViewModel (Hilt)  
**Purpose**: Dashboard state management

#### Properties

```kotlin
val isMonitoring: StateFlow<Boolean>
```
Monitoring state indicator.

```kotlin
val networkStats: StateFlow<NetworkStats>
```
Aggregated network statistics.

```kotlin
val networkSpeed: StateFlow<NetworkSpeed>
```
Real-time network speed from NetworkMonitor.

```kotlin
val ping: StateFlow<Int>
```
Current ping latency from NetworkMonitor.

```kotlin
val securityScore: StateFlow<Int>
```
Security score from SecurityAnalyzer.

```kotlin
val connectedDevicesCount: StateFlow<Int>
```
Device count from DeviceScanner.

#### Methods

```kotlin
fun startMonitoring()
```
Starts network monitoring and periodic updates.

```kotlin
fun stopMonitoring()
```
Stops network monitoring.

```kotlin
fun refreshStats()
```
Manually triggers stats refresh.

```kotlin
fun triggerWiFiScan()
```
Initiates WiFi network scan.

---

### SecurityViewModel

**Package**: `com.example.local_network_scanner.ui.viewmodel`  
**Scope**: ViewModel (Hilt)  
**Purpose**: Security screen state management

#### Properties

```kotlin
val isScanning: StateFlow<Boolean>
```
Indicates if security scan is in progress.

```kotlin
val scanProgress: StateFlow<Float>
```
Scan progress (0.0 to 1.0).

```kotlin
val suspiciousApps: StateFlow<List<SuspiciousApp>>
```
List of detected suspicious apps.

```kotlin
val scanComplete: StateFlow<Boolean>
```
Indicates if scan has completed.

```kotlin
val securityScore: StateFlow<Int>
```
Current security score.

#### Methods

```kotlin
fun performDeepScan()
```
Executes comprehensive security scan.

**Phases**:
1. Count network-capable apps (20%)
2. Scan for suspicious apps (60%)
3. Calculate security score (20%)

```kotlin
fun resetScan()
```
Resets scan state and clears results.

---

### WifiViewModel

**Package**: `com.example.local_network_scanner.ui.viewmodel`  
**Scope**: ViewModel (Hilt)  
**Purpose**: WiFi operations management

#### Properties

```kotlin
val currentSsid: StateFlow<String>
```
Currently connected WiFi SSID.

```kotlin
val signalStrength: StateFlow<Int>
```
Current WiFi signal strength (RSSI).

```kotlin
val localIp: StateFlow<String>
```
Device's local IP address.

```kotlin
val scanResults: StateFlow<List<ScanResult>>
```
List of discovered WiFi networks.

```kotlin
val permissionGranted: StateFlow<Boolean>
```
Location permission status.

```kotlin
val isScanning: StateFlow<Boolean>
```
WiFi scan in progress indicator.

#### Methods

```kotlin
fun startScan()
```
Initiates WiFi network scan.

**Requirements**: Location permission must be granted

```kotlin
fun connectToNetwork(scanResult: ScanResult, password: String?)
```
Connects to specified WiFi network.

**Parameters**:
- `scanResult`: Target network from scan results
- `password`: Network password (null for open networks)

---

### ActivityViewModel

**Package**: `com.example.local_network_scanner.ui.viewmodel`  
**Scope**: ViewModel (Hilt)  
**Purpose**: Activity tracking and logging

#### Properties

```kotlin
val last5MinutesActivity: StateFlow<List<AppNetworkActivity>>
```
Network activity for apps in the last 5 minutes.

```kotlin
val dataUsageStats: StateFlow<DataUsageStats>
```
Aggregated data usage statistics.

```kotlin
val isLoading: StateFlow<Boolean>
```
Loading state indicator.

#### Methods

```kotlin
fun refreshActivity()
```
Manually refreshes activity data.

#### Data Classes

```kotlin
data class AppNetworkActivity(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable?,
    val connectionCount: Int,
    val uploadBytes: Long,
    val downloadBytes: Long,
    val lastActiveTimestamp: Long
)

data class DataUsageStats(
    val totalUpload: Long,
    val totalDownload: Long,
    val activeAppsCount: Int
)
```

---

### SettingsViewModel

**Package**: `com.example.local_network_scanner.ui.viewmodel`  
**Scope**: ViewModel (Hilt)  
**Purpose**: Settings management

#### Properties

```kotlin
val blockAllByDefault: StateFlow<Boolean>
```
Default firewall block state.

```kotlin
val dnsSettings: StateFlow<DnsSettings>
```
DNS configuration.

```kotlin
val enableWeeklySummary: StateFlow<Boolean>
```
Weekly summary notification setting.

```kotlin
val adBlockingEnabled: StateFlow<Boolean>
```
Ad blocking state.

```kotlin
val malwareBlockingEnabled: StateFlow<Boolean>
```
Malware blocking state.

```kotlin
val notifyOnThreats: StateFlow<Boolean>
```
Threat notification setting.

```kotlin
val notifyOnNewApps: StateFlow<Boolean>
```
New app notification setting.

#### Methods

```kotlin
fun setBlockAllByDefault(block: Boolean)
```
Updates default firewall block setting.

```kotlin
fun setDnsMode(mode: String)
```
Sets DNS mode (System/Custom/Cloudflare/Google).

```kotlin
fun setCustomDnsIp(ip: String)
```
Configures custom DNS server IP.

```kotlin
fun setEnableSecureDns(enable: Boolean)
```
Enables/disables DNS over HTTPS.

```kotlin
fun setAdBlockingEnabled(enabled: Boolean)
```
Toggles ad blocking.

```kotlin
fun setMalwareBlockingEnabled(enabled: Boolean)
```
Toggles malware blocking.

```kotlin
fun setNotifyOnThreats(enabled: Boolean)
```
Toggles threat notifications.

```kotlin
fun setNotifyOnNewApps(enabled: Boolean)
```
Toggles new app notifications.

---

## Error Handling

### Exception Types

All service methods handle exceptions gracefully:

```kotlin
try {
    val result = performOperation()
    _state.value = result
} catch (e: Exception) {
    // Log error and maintain previous state
    e.printStackTrace()
}
```

### Permission Errors

Permission-dependent operations check status before execution:

```kotlin
if (checkPermission()) {
    performOperation()
} else {
    requestPermission()
}
```

## Best Practices

### StateFlow Usage

1. Always use `asStateFlow()` for public exposure
2. Update state from main thread or use proper dispatchers
3. Collect state in UI with `collectAsState()`

### Coroutine Scope

1. Use `viewModelScope` for ViewModel operations
2. Use appropriate dispatchers (IO, Default, Main)
3. Handle cancellation properly

### Resource Management

1. Clean up in `onCleared()` for ViewModels
2. Unregister receivers and callbacks
3. Cancel coroutine jobs

## Version History

### v1.0.0
- Initial API release
- Core services implemented
- ViewModel architecture established

---

For implementation examples and usage patterns, see the Architecture Guide.
