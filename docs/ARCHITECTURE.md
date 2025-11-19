# NetSentry Architecture Guide

## Overview

NetSentry is built using modern Android development practices with a focus on maintainability, testability, and performance. This document provides a comprehensive overview of the application's architecture.

## Architectural Pattern

### MVVM (Model-View-ViewModel)

NetSentry follows the MVVM architectural pattern, which provides clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                         View Layer                       │
│              (Jetpack Compose UI Components)             │
└─────────────────┬───────────────────────────────────────┘
                  │
                  │ Observes StateFlow
                  ▼
┌─────────────────────────────────────────────────────────┐
│                    ViewModel Layer                       │
│     (Business Logic, State Management, Use Cases)        │
└─────────────────┬───────────────────────────────────────┘
                  │
                  │ Calls Methods
                  ▼
┌─────────────────────────────────────────────────────────┐
│                      Model Layer                         │
│    (Repositories, Services, Data Sources, Entities)      │
└─────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### View Layer (UI)
- **Location**: `app/src/main/java/com/example/local_network_scanner/ui/`
- **Technology**: Jetpack Compose
- **Responsibilities**:
  - Render UI based on ViewModel state
  - Handle user interactions
  - Navigate between screens
  - Display data reactively

#### ViewModel Layer
- **Location**: `app/src/main/java/com/example/local_network_scanner/ui/viewmodel/`
- **Technology**: Android ViewModel + StateFlow
- **Responsibilities**:
  - Manage UI state
  - Execute business logic
  - Coordinate between services
  - Expose state via StateFlow
  - Handle lifecycle events

#### Model Layer
- **Location**: `app/src/main/java/com/example/local_network_scanner/{data,services}/`
- **Technology**: Kotlin, Android Services
- **Responsibilities**:
  - Data persistence (DataStore)
  - Network operations
  - Security analysis
  - Device scanning
  - Data transformation

## Core Components

### 1. Services

#### NetworkMonitor
**Location**: `services/NetworkMonitor.kt`

Singleton service responsible for real-time network monitoring:

```kotlin
@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _networkSpeed = MutableStateFlow(NetworkSpeed())
    val networkSpeed: StateFlow<NetworkSpeed> = _networkSpeed
    
    private val _ping = MutableStateFlow(0)
    val ping: StateFlow<Int> = _ping
}
```

**Features**:
- 0.5-second update interval (configurable)
- Battery-aware monitoring
- TrafficStats integration
- Ping measurement

#### SecurityAnalyzer
**Location**: `services/SecurityAnalyzer.kt`

Analyzes installed applications for security threats:

```kotlin
@Singleton
class SecurityAnalyzer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun scanForSuspiciousApps(): List<SuspiciousApp>
    suspend fun calculateSecurityScore(): Int
    suspend fun countAppsWithNetworkAccess(): Int
}
```

**Features**:
- Permission combination analysis
- Network usage monitoring
- Risk level classification
- Multi-factor security scoring

#### DeviceScanner
**Location**: `services/DeviceScanner.kt`

Discovers devices on the local network:

```kotlin
@Singleton
class DeviceScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun scanNetwork(): Int
    suspend fun estimateDeviceCount(): Int
}
```

**Features**:
- Optimized IP range scanning
- Gateway detection
- Reachability testing
- Device counting

### 2. ViewModels

#### DashboardViewModel
Manages dashboard state and coordinates multiple services:

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val securityAnalyzer: SecurityAnalyzer,
    private val deviceScanner: DeviceScanner
) : ViewModel()
```

**State Management**:
- Network speed metrics
- Security score
- Connected devices count
- Periodic updates

#### SecurityViewModel
Handles security scanning operations:

```kotlin
@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityAnalyzer: SecurityAnalyzer
) : ViewModel()
```

**Features**:
- Deep scan orchestration
- Progress tracking
- Suspicious app management

#### WifiViewModel
Manages WiFi network operations:

```kotlin
@HiltViewModel
class WifiViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel()
```

**Features**:
- WiFi scanning
- Network connection
- Permission handling

### 3. Data Layer

#### DataStore
**Location**: `data/datastore/`

Persistent storage for user preferences:

```kotlin
interface SettingsRepository {
    fun blockAllByDefault(): Flow<Boolean>
    fun getDnsSettings(): Flow<DnsSettings>
    suspend fun setBlockAllByDefault(block: Boolean)
}
```

#### Models
**Location**: `data/model/`

Data classes representing domain entities:

- `NetworkSpeed` - Network speed metrics
- `AppNetworkActivity` - Per-app network usage
- `DataUsageStats` - Aggregate usage statistics
- `ConnectionLog` - Network connection records

## Dependency Injection

### Hilt Configuration

NetSentry uses Hilt for dependency injection, providing:

- **Scoped Instances**: Singleton services for network monitoring
- **ViewModel Injection**: Automatic ViewModel creation
- **Testing Support**: Easy mocking for unit tests

#### AppModule
**Location**: `di/AppModule.kt`

Defines application-wide dependencies:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitor(context)
}
```

## State Management

### StateFlow Pattern

All ViewModels expose state using Kotlin's StateFlow:

```kotlin
private val _isScanning = MutableStateFlow(false)
val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
```

**Benefits**:
- Type-safe state updates
- Lifecycle-aware observations
- Automatic UI updates
- Thread-safe operations

### Reactive UI

Compose UI observes StateFlow and recomposes automatically:

```kotlin
@Composable
fun SecurityScreen(viewModel: SecurityViewModel = hiltViewModel()) {
    val isScanning by viewModel.isScanning.collectAsState()
    
    // UI updates automatically when isScanning changes
}
```

## Navigation

### Navigation Compose

Type-safe navigation using Jetpack Navigation Compose:

```kotlin
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Network : Screen("network")
    object Security : Screen("security")
    object Activity : Screen("activity")
}
```

**Features**:
- Route-based navigation
- Bottom navigation bar
- Navigation drawer
- Smooth transitions

## Background Processing

### Coroutines

All asynchronous operations use Kotlin Coroutines:

```kotlin
viewModelScope.launch {
    val result = withContext(Dispatchers.IO) {
        performNetworkScan()
    }
    _scanResults.value = result
}
```

**Dispatchers**:
- `Dispatchers.Main` - UI updates
- `Dispatchers.IO` - Network/disk operations
- `Dispatchers.Default` - CPU-intensive tasks

### WorkManager

Scheduled background tasks use WorkManager:

- Periodic security scans
- Network monitoring
- Log cleanup

## Security Considerations

### Permission Handling

Runtime permission requests with graceful degradation:

```kotlin
val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        viewModel.startScan()
    }
}
```

### Data Privacy

- All data processed locally
- No external transmission
- Encrypted preferences (DataStore)
- No third-party analytics

## Performance Optimizations

### Battery Awareness

Monitoring intervals adjust based on battery state:

```kotlin
private fun getUpdateInterval(): Long {
    return if (powerManager.isPowerSaveMode) {
        2000L // 2 seconds in power save mode
    } else {
        500L // 0.5 seconds in normal mode
    }
}
```

### Memory Management

- Lazy loading for large lists
- Pagination support
- Efficient StateFlow usage
- Proper lifecycle handling

### Network Efficiency

- Optimized scan ranges
- Connection timeouts
- Background thread execution

## Testing Strategy

### Unit Tests

Test ViewModels and business logic:

```kotlin
@Test
fun `networkSpeed converts bytes to Mbps correctly`() {
    val speed = NetworkSpeed(
        downloadBytesPerSecond = 1_000_000
    )
    assertEquals(8.0, speed.downloadMbps, 0.1)
}
```

### Integration Tests

Test service interactions and data flow.

### UI Tests

Compose testing for UI components.

## Build Configuration

### Gradle Structure

```
project/
├── build.gradle.kts (root)
├── settings.gradle.kts
└── app/
    └── build.gradle.kts
```

### ProGuard

Code obfuscation for release builds:

- Minification enabled
- Resource shrinking
- Custom ProGuard rules

## Future Architecture Improvements

### Planned Enhancements

1. **Repository Pattern**: Introduce repository layer for data sources
2. **Use Cases**: Extract business logic into use case classes
3. **Clean Architecture**: Further separate domain and data layers
4. **Database**: Add Room database for persistent logs
5. **Offline Support**: Cache network data for offline viewing

## Conclusion

NetSentry's architecture provides a solid foundation for a maintainable, testable, and performant Android application. The use of modern Android development practices ensures the codebase remains scalable and easy to extend with new features.
