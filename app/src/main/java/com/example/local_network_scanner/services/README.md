# Services Module

## Overview

The services module contains core business logic and background operations for NetSentry. All services are implemented as singletons using Hilt dependency injection.

## Services

### NetworkMonitor

**File**: `NetworkMonitor.kt`  
**Purpose**: Real-time network speed and latency monitoring

**Key Features**:
- Network speed measurement (download/upload)
- Ping latency monitoring
- Battery-aware update intervals
- TrafficStats integration

**Update Frequency**:
- Normal mode: 0.5 seconds
- Power save mode: 2.0 seconds

**Usage**:
```kotlin
@Inject lateinit var networkMonitor: NetworkMonitor

// Start monitoring
networkMonitor.startMonitoring()

// Observe speed
networkMonitor.networkSpeed.collect { speed ->
    println("Download: ${speed.downloadMbps} Mbps")
}
```

### SecurityAnalyzer

**File**: `SecurityAnalyzer.kt`  
**Purpose**: Application security scanning and threat detection

**Key Features**:
- Permission combination analysis
- Network usage monitoring per app
- Risk level classification
- Security score calculation (0-100)

**Detection Rules**:
- Internet + Location + Camera = Suspicious
- Internet + SMS = Suspicious
- Internet + Contacts + Phone = Suspicious
- Network usage > 100MB = High usage flag

**Usage**:
```kotlin
@Inject lateinit var securityAnalyzer: SecurityAnalyzer

// Scan for threats
val suspiciousApps = securityAnalyzer.scanForSuspiciousApps()

// Get security score
val score = securityAnalyzer.calculateSecurityScore()
```

### DeviceScanner

**File**: `DeviceScanner.kt`  
**Purpose**: Local network device discovery

**Key Features**:
- Network device discovery
- Gateway identification
- Reachability testing
- Optimized scanning algorithm

**Scan Strategy**:
Scans IP ranges: 1-2, 10-20, 50-60, 100-110, 200-210 (75 IPs total instead of 254)

**Usage**:
```kotlin
@Inject lateinit var deviceScanner: DeviceScanner

// Full scan
val deviceCount = deviceScanner.scanNetwork()

// Quick estimate
val estimate = deviceScanner.estimateDeviceCount()
```

### GeoIpService

**File**: `GeoIpService.kt`  
**Purpose**: Geographic IP address lookup

**Key Features**:
- IP geolocation
- Country/region identification
- ISP information (where available)

### NetSentryTileService

**File**: `NetSentryTileService.kt`  
**Purpose**: Quick Settings tile for fast access

**Key Features**:
- Quick network monitoring toggle
- VPN service control
- Status indication

## Data Classes

### NetworkSpeed
```kotlin
data class NetworkSpeed(
    val downloadBytesPerSecond: Long,
    val uploadBytesPerSecond: Long
) {
    val downloadMbps: Double
    val uploadMbps: Double
}
```

### SuspiciousApp
```kotlin
data class SuspiciousApp(
    val packageName: String,
    val appName: String,
    val reasons: List<String>,
    val riskLevel: RiskLevel
)
```

### NetworkDevice
```kotlin
data class NetworkDevice(
    val ipAddress: String,
    val deviceName: String,
    val isReachable: Boolean
)
```

## Performance Considerations

### Battery Impact

Services implement battery-aware monitoring:
- Reduced update frequency in power save mode
- Background job cancellation when inactive
- Efficient network scanning with timeouts

### Memory Usage

- StateFlow for reactive updates (no memory leaks)
- Proper coroutine scope management
- Efficient data structures

### Network Efficiency

- Connection timeouts (100ms for device scanning, 3s for ping)
- Optimized scan ranges
- Background thread execution

## Testing

Unit tests are located in `app/src/test/java/com/example/local_network_scanner/services/`

Run tests:
```bash
./gradlew test
```

## Dependencies

- Android Context (for system services)
- Kotlin Coroutines (for async operations)
- StateFlow (for reactive state)
- Hilt (for dependency injection)

## Future Enhancements

- MAC address lookup
- Device vendor identification
- Advanced threat detection algorithms
- Machine learning-based anomaly detection
