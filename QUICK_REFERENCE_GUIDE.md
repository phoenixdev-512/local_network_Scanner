# Quick Reference Guide - Enhanced Dashboard

## Overview
This guide provides quick access to the new dashboard features and how to use them.

## New Components

### 1. Speed Unit Toggle
**Location:** SpeedTestWidget (top right)  
**Function:** Cycles through speed display units  
**Units:** Mbps → MB/s → Kbps → KB/s → Mbps  
**Persistence:** Saved to user settings

### 2. Security Scan Button
**Location:** SecurityOverviewWidget  
**Button:** "Scan Now"  
**Function:** Performs deep security analysis  
**Output:**
- Security score (0-100)
- Suspicious app detection
- Network permission analysis
- Timestamp of last scan

### 3. Data Usage Time Range
**Location:** DataUsageWidget (dropdown)  
**Options:**
- TODAY - Since midnight
- THIS_WEEK - Since week start
- THIS_MONTH - Since month start
- CUSTOM - Last 30 days

**Display:**
- Total usage
- WiFi breakdown
- Mobile breakdown

### 4. Connected Devices
**Location:** ConnectedDevicesWidget  
**Display:** Live count of devices on network  
**Update:** Every 10 seconds

## Widget Hierarchy

```
DashboardScreen
├── SpeedTestWidget
│   ├── Download speed (with unit)
│   ├── Upload speed (with unit)
│   ├── Ping (ms)
│   └── Unit toggle button
│
├── SecurityOverviewWidget
│   ├── Security score gauge
│   ├── Scan button
│   ├── Last scan time
│   └── Progress indicator
│
├── DataUsageWidget
│   ├── Time range selector
│   ├── Total usage display
│   └── WiFi/Mobile breakdown
│
├── ConnectedDevicesWidget
│   └── Device count
│
└── QuickActionsWidget
    └── (existing actions)
```

## Data Models

### NetworkSpeed
```kotlin
data class NetworkSpeed(
    val downloadBytesPerSecond: Long,
    val uploadBytesPerSecond: Long
)

enum class SpeedUnit {
    MBPS("Mbps"),    // Megabits per second
    MBS("MB/s"),     // Megabytes per second
    KBPS("Kbps"),    // Kilobits per second
    KBS("KB/s")      // Kilobytes per second
}
```

### DataUsageStats
```kotlin
data class DataUsageStats(
    val total: Long,      // Total bytes used
    val wifi: Long,       // WiFi bytes
    val mobile: Long,     // Mobile data bytes
    val download: Long,   // Downloaded bytes
    val upload: Long,     // Uploaded bytes
    val apps: Long        // App usage bytes
)

enum class TimeRange {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    CUSTOM
}
```

### SecurityScanResult
```kotlin
data class SecurityScanResult(
    val score: Int,                      // 0-100
    val suspiciousApps: List<SuspiciousApp>,
    val appsWithNetworkAccess: Int,
    val timestamp: Long
)
```

## ViewModel Methods

### DashboardViewModel

**Speed Unit:**
```kotlin
val speedUnit: StateFlow<SpeedUnit>
fun toggleSpeedUnit()
```

**Security Scanning:**
```kotlin
val securityScore: StateFlow<Int>
val isSecurityScanning: StateFlow<Boolean>
val lastSecurityScanTime: StateFlow<Long>
fun startSecurityScan()
```

**Data Usage:**
```kotlin
val dataUsageStats: StateFlow<DataUsageStats>
val selectedTimeRange: StateFlow<TimeRange>
fun setTimeRange(range: TimeRange)
```

**Connected Devices:**
```kotlin
val connectedDevicesCount: StateFlow<Int>
```

## Update Frequencies

| Component | Update Interval | Configurable |
|-----------|----------------|--------------|
| Network Speed | 0.5 seconds | Yes (PowerManager) |
| Ping | 0.5 seconds | Yes (PowerManager) |
| Security Score | 5 seconds | No |
| Data Usage | 60 seconds | No |
| Connected Devices | 10 seconds | No |

## Animations

All widgets feature smooth animations:
- **Spring animations** for counts (devices, security score)
- **Tween animations** for values (speed, data usage)
- **Infinite animations** for live indicator (pulsing dot)
- **Progress animations** for security gauge

## Color Coding

### Security Score
- **Green (80-100):** Excellent security
- **Orange (60-79):** Good security
- **Orange (40-59):** Fair security
- **Red (0-39):** Poor security - action needed

### Ping Quality
- **Green (< 50ms):** Excellent connection
- **Orange (50-99ms):** Good connection
- **Red (≥ 100ms):** Poor connection

### Data Usage
- **Blue:** WiFi data
- **Orange:** Mobile data

## Error Handling

All components gracefully handle errors:
- Network unavailable → Shows "N/A" or -1
- Permission denied → Falls back to TrafficStats
- Scan failure → Keeps previous score
- Device scan timeout → Shows estimate

## Permissions Required

- `INTERNET` - Already required
- `ACCESS_NETWORK_STATE` - Already required
- `READ_PHONE_STATE` - For NetworkStatsManager on some devices (optional)
- `PACKAGE_USAGE_STATS` - For NetworkStatsManager (optional)

## Integration Example

```kotlin
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    LazyColumn {
        // Speed widget
        item {
            val speedUnit by viewModel.speedUnit.collectAsState()
            SpeedTestWidget(
                networkSpeed = networkSpeed,
                ping = ping,
                speedUnit = speedUnit,
                onToggleUnit = { viewModel.toggleSpeedUnit() }
            )
        }
        
        // Security widget
        item {
            val score by viewModel.securityScore.collectAsState()
            val isScanning by viewModel.isSecurityScanning.collectAsState()
            val lastScan by viewModel.lastSecurityScanTime.collectAsState()
            SecurityOverviewWidget(
                securityScore = score,
                isScanning = isScanning,
                lastScanTime = lastScan,
                onScanClick = { viewModel.startSecurityScan() }
            )
        }
        
        // Data usage widget
        item {
            val dataUsage by viewModel.dataUsageStats.collectAsState()
            val timeRange by viewModel.selectedTimeRange.collectAsState()
            DataUsageWidget(
                dataUsage = dataUsage,
                selectedTimeRange = timeRange,
                onTimeRangeChange = { viewModel.setTimeRange(it) }
            )
        }
        
        // Devices widget
        item {
            val count by viewModel.connectedDevicesCount.collectAsState()
            ConnectedDevicesWidget(devicesCount = count)
        }
    }
}
```

## Troubleshooting

### Speed shows 0
- Check internet connection
- Verify NetworkMonitor is started
- Wait for initial measurement (takes 0.5s)

### Security scan doesn't work
- Check app permissions
- Verify PackageManager access
- Try manual refresh

### Data usage incorrect
- On API < 23: Shows total since boot (TrafficStats limitation)
- On API ≥ 23: Requires usage stats permission
- Check selected time range

### No devices detected
- Verify WiFi is enabled
- Check network allows ping
- Some networks block ICMP
- Try waiting for estimate (10s)

## Best Practices

1. **Speed Unit**: Choose based on connection speed
   - Fast (> 10 Mbps): Use Mbps
   - Moderate (1-10 Mbps): Use MB/s or Mbps
   - Slow (< 1 Mbps): Use Kbps

2. **Security Scanning**: 
   - Run scan after installing new apps
   - Scan periodically (weekly recommended)
   - Review suspicious apps immediately

3. **Data Usage**:
   - Use TODAY for daily monitoring
   - Use THIS_WEEK for weekly budget
   - Use THIS_MONTH for monthly caps

4. **Connected Devices**:
   - Note initial count as baseline
   - Investigate unexpected increases
   - Use for network security monitoring
