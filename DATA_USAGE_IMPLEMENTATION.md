# Data Usage Monitoring Implementation

## Overview
This implementation adds accurate data usage monitoring to the SENET Network Scanner app using Android's NetworkStatsManager API. It replaces placeholder data with real system statistics on the Dashboard screen.

## Key Components

### 1. DataUsageMonitor.kt
**Location:** `app/src/main/java/com/example/local_network_scanner/services/DataUsageMonitor.kt`

**Features:**
- Real-time data usage statistics using NetworkStatsManager API
- Separate tracking for Mobile and WiFi data
- Support for multiple time ranges (Today, Week, Month)
- Per-app data usage tracking
- Graceful error handling for API restrictions

**Main Methods:**
- `getDataUsageStats(timeRange: TimeRange)`: Returns overall data usage stats
- `getPerAppDataUsage(timeRange: TimeRange)`: Returns data usage per application

**Data Classes:**
- `DataUsageStats`: Contains total, mobile, and WiFi usage statistics
- `AppDataUsage`: Contains per-app data usage information
- `TimeRange`: Enum for time range selection (TODAY, WEEK, MONTH)

### 2. PermissionHelper.kt
**Location:** `app/src/main/java/com/example/local_network_scanner/util/PermissionHelper.kt`

**Features:**
- Checks if PACKAGE_USAGE_STATS permission is granted
- Requests permission by opening Android settings
- Graceful error handling

**Main Methods:**
- `hasUsageStatsPermission(context: Context)`: Boolean check for permission
- `requestUsageStatsPermission(activity: Activity)`: Opens settings for permission grant

### 3. DashboardViewModel.kt
**Updates:**
- Injected `DataUsageMonitor` dependency
- Updated `updateNetworkStats()` to use real data from NetworkStatsManager (API 23+)
- Maintains backward compatibility with older Android versions
- Graceful fallback when permission is not granted

### 4. DashboardScreen.kt
**Updates:**
- Added permission check on screen load
- New `PermissionBanner` composable for permission requests
- Banner appears when PACKAGE_USAGE_STATS permission is not granted
- Tapping banner opens Android settings for permission grant
- Real-time permission status updates

### 5. AndroidManifest.xml
**Added Permissions:**
```xml
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## API Requirements

- **Minimum SDK:** API 23 (Android 6.0 Marshmallow)
- **Required Permission:** PACKAGE_USAGE_STATS (special permission)
- **Fallback:** Gracefully handles older versions and missing permissions

## User Experience

1. **First Launch:**
   - User sees permission banner on Dashboard
   - Tapping banner opens Android settings
   - User grants "Usage Access" permission
   - Banner disappears, real data appears

2. **Subsequent Launches:**
   - If permission granted: Real data usage statistics displayed
   - If permission denied: Banner remains, generic data shown

3. **Data Display:**
   - Real-time network speed
   - Accurate daily data usage (Today view)
   - Visual progress bar showing usage vs total
   - Live updates every 2 seconds

## Testing

### Unit Tests
**Location:** `app/src/test/java/com/example/local_network_scanner/services/DataUsageMonitorTest.kt`

**Coverage:**
- DataUsageStats calculation tests
- AppDataUsage calculation tests
- TimeRange enum validation
- Edge case handling (zero values, large values)

**Location:** `app/src/test/java/com/example/local_network_scanner/util/PermissionHelperTest.kt`

**Coverage:**
- Permission helper utility tests
- Intent action validation

### Manual Testing Checklist
- [ ] Install app and verify permission banner appears
- [ ] Grant PACKAGE_USAGE_STATS permission
- [ ] Verify permission banner disappears
- [ ] Verify real data usage appears on Dashboard
- [ ] Use some data and verify Dashboard updates
- [ ] Test on Android 6.0+ devices
- [ ] Test backward compatibility on older devices

## Security Considerations

1. **Permission Handling:**
   - PACKAGE_USAGE_STATS is a protected permission
   - Requires explicit user consent via Settings
   - Cannot be requested via runtime permission dialog

2. **Privacy:**
   - Data stays on device
   - No data is transmitted to external servers
   - Per-app usage is only for display purposes

3. **Error Handling:**
   - All NetworkStatsManager calls are wrapped in try-catch
   - Graceful degradation when permission denied
   - No crashes or data leaks on errors

## Performance Considerations

1. **Async Operations:**
   - All data queries run on IO dispatcher
   - No UI thread blocking
   - Efficient coroutine usage

2. **Update Frequency:**
   - Dashboard stats update every 2 seconds
   - Lightweight queries using NetworkStatsManager
   - Minimal battery impact

3. **Memory:**
   - Singleton pattern for DataUsageMonitor
   - Efficient data structures
   - No memory leaks

## Future Enhancements

1. **Data Export:**
   - Export usage statistics to CSV/JSON
   - Share data usage reports

2. **Usage Alerts:**
   - Set data usage limits
   - Notify user when approaching limit

3. **Historical Trends:**
   - Graph data usage over time
   - Identify usage patterns

4. **App-Level Controls:**
   - Block specific apps from using data
   - Set per-app data limits

## Migration Notes

This implementation is **non-breaking**:
- Existing functionality remains unchanged
- New features are additive only
- Graceful fallback maintains compatibility
- No database migrations required

## Dependencies

All dependencies are already present in the project:
- Hilt for dependency injection
- Kotlin Coroutines for async operations
- Jetpack Compose for UI
- Material3 for design components

## Code Quality

- **Lint:** Clean, no new warnings
- **Security:** CodeQL passed, no vulnerabilities
- **Testing:** Unit tests added and passing
- **Documentation:** Comprehensive inline documentation
- **Best Practices:** Follows Android development guidelines
