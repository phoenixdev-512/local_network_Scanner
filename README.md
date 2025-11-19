# NetSentry - Network Security & Monitoring App

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.jpg" width="200" alt="NetSentry Logo">
</p>

## Overview

NetSentry is a comprehensive Android network security and monitoring application that provides real-time network analysis, security scanning, and device management capabilities. Built with Jetpack Compose and Material 3 design principles, NetSentry offers a modern, intuitive interface for monitoring your network activity and protecting your device.

## Features

### 🏠 Dashboard
- **Real-time Network Speed Monitoring**
  - Live download/upload speeds updated every 0.5 seconds
  - Ping monitoring with color-coded indicators
  - Animated metrics with smooth transitions
  - Network speed gauge with visual feedback

- **Security Overview**
  - Dynamic security score (0-100) based on real-time analysis
  - Threat detection and counting
  - Active connection monitoring
  - Connected devices counter

- **Data Usage Tracking**
  - Visual representation of data consumption
  - Total usage statistics
  - Real-time updates using TrafficStats API

- **Quick Actions**
  - Scan Network - Instantly navigate to network scanner
  - Block App - Quick access to security features
  - View Logs - Access recent activity logs

### 🔍 Network Scanner
- **WiFi Network Discovery**
  - Scan for nearby WiFi networks
  - Display SSID, signal strength, and security type
  - Sort by signal strength, name, or security
  - Filter by open or secured networks
  - Current connection information (SSID, IP, signal)

- **Device Discovery**
  - Scan local network for connected devices
  - Display IP addresses for discovered devices
  - Show gateway/router information
  - Real-time device count updates
  - Smart scanning algorithm for performance

### 🛡️ Security
- **Deep Security Scanning**
  - Comprehensive app security analysis
  - Detection of suspicious permission combinations
  - Network usage anomaly detection
  - Progress indicator with multi-phase scanning

- **Threat Detection**
  - Flag apps with dangerous permission sets
  - Identify apps with excessive network usage
  - Risk level classification (Low, Medium, High)
  - Detailed reason listing for each flagged app

- **Security Metrics**
  - Total installed apps count
  - Apps with network access count
  - Active connections monitoring
  - Real-time threat counter

- **App Management**
  - View flagged apps with icons and names
  - Direct uninstall capability
  - Detailed app information access

### 📊 Activity & Logs
- **Per-App Network Activity**
  - Network usage tracking for last 5 minutes
  - Download/upload bytes per app
  - Active connection count per app
  - Live updates every second

- **Search & Filter**
  - Search by app name or package
  - Sort by data usage, app name, or connections
  - Real-time filtering

- **App Information**
  - Display app icons and names
  - Formatted data usage (KB, MB, GB)
  - Connection statistics

### ⚙️ Settings
- **Firewall Controls**
  - Block all by default toggle
  - Ad blocking enable/disable
  - Malware blocking toggle

- **DNS Configuration**
  - DNS mode selection
  - Custom DNS IP configuration
  - Secure DNS (DNS over HTTPS) option

- **Notifications**
  - Threat detection alerts
  - New app installation notifications
  - Weekly summary reports

- **Preference Persistence**
  - Settings saved using DataStore
  - Automatic restoration on app restart

### 🎨 UI/UX Features
- **Material 3 Design**
  - Modern, minimalist visual language
  - Dark theme optimized
  - Electric blue accent colors
  - Gradient backgrounds

- **Animations**
  - Smooth transitions between screens
  - Animated metric updates
  - Skeleton loaders during data fetch
  - Pulsing live indicators
  - Spring-based value animations

- **Navigation**
  - Bottom navigation bar (4 main sections)
  - Navigation drawer with profile header
  - Smooth screen transitions
  - Deep linking support

## Technical Stack

### Architecture
- **MVVM Pattern** - ViewModel-based architecture
- **Dependency Injection** - Hilt for DI
- **Reactive Programming** - Kotlin Coroutines and Flow
- **State Management** - StateFlow for UI state

### Core Technologies
- **Jetpack Compose** - Modern declarative UI
- **Material 3** - Latest Material Design components
- **Navigation Compose** - Type-safe navigation
- **Hilt** - Dependency injection
- **DataStore** - Preferences persistence
- **WorkManager** - Background task scheduling

### Services & Utilities
- **NetworkMonitor** - Real-time network metrics with 0.5s updates
- **SecurityAnalyzer** - App security scanning and threat detection
- **DeviceScanner** - Network device discovery
- **GeoIpService** - Geographic IP lookup
- **VPN Service** - Network traffic monitoring

## Permissions

The app requires the following permissions:

- `INTERNET` - Network communication
- `ACCESS_NETWORK_STATE` - Network status monitoring
- `ACCESS_WIFI_STATE` - WiFi information access
- `CHANGE_WIFI_STATE` - WiFi scanning
- `ACCESS_FINE_LOCATION` - WiFi network details (required by Android)
- `ACCESS_COARSE_LOCATION` - Network location
- `POST_NOTIFICATIONS` - Security alerts
- `QUERY_ALL_PACKAGES` - App security scanning (Android 11+)

## Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher (minimum)
- Android SDK 34 (target)
- Kotlin 1.9.22
- Gradle 8.13

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/phoenixdev-512/local_network_Scanner.git
cd local_network_Scanner
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run:
```bash
./gradlew assembleDebug
```

Or use Android Studio's "Run" button.

### Release Build

For release builds, configure your signing key in `app/build.gradle.kts` and run:
```bash
./gradlew assembleRelease
```

## Project Structure

```
app/src/main/
├── java/com/example/local_network_scanner/
│   ├── data/                   # Data layer
│   │   ├── db/                # Database entities
│   │   ├── datastore/         # Settings persistence
│   │   └── model/             # Data models
│   ├── di/                     # Dependency injection
│   ├── proxy/                  # VPN proxy services
│   ├── receivers/              # Broadcast receivers
│   ├── services/               # Core services
│   │   ├── DeviceScanner.kt   # Network device discovery
│   │   ├── NetworkMonitor.kt  # Real-time network stats
│   │   ├── SecurityAnalyzer.kt # Security scanning
│   │   └── GeoIpService.kt    # IP geolocation
│   ├── ui/                     # UI layer
│   │   ├── components/        # Reusable UI components
│   │   ├── theme/             # Theme configuration
│   │   ├── viewmodel/         # ViewModels
│   │   ├── DashboardScreen.kt
│   │   ├── SecurityScreen.kt
│   │   ├── NetworkScannerScreen.kt
│   │   ├── ActivityScreen.kt
│   │   └── SettingsScreen.kt
│   ├── util/                   # Utility classes
│   ├── vpn/                    # VPN service
│   └── workers/                # Background workers
└── res/                        # Resources
    ├── mipmap-*/              # App icons
    ├── drawable/              # Vector graphics
    ├── values/                # Strings, colors, themes
    └── xml/                   # XML configs
```

## Testing

### Unit Tests
Run unit tests with:
```bash
./gradlew test
```

### Instrumented Tests
Run on device/emulator:
```bash
./gradlew connectedAndroidTest
```

## Performance Considerations

- **Battery Optimization**: Monitoring intervals adapt to power save mode
- **Memory Efficient**: Lazy loading and pagination for large datasets
- **Background Tasks**: WorkManager for scheduled operations
- **Network Efficiency**: Optimized scanning algorithms with timeout controls

## Security Features

- **App Scanning**: QUERY_ALL_PACKAGES permission for comprehensive security analysis
- **Permission Analysis**: Detection of dangerous permission combinations
- **Network Monitoring**: Real-time traffic analysis
- **Threat Detection**: Multi-factor security scoring system
- **Privacy**: All data processed locally, no external data transmission

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Known Issues

- Build system requires specific Android Gradle Plugin configuration
- Some devices may not support TrafficStats API
- Network scanning may be limited on some Android versions due to permission restrictions

## Future Enhancements

- [ ] Add splash screen with branding
- [ ] Create About page with app information
- [ ] Implement onboarding flow for new users
- [ ] Add more detailed device information (MAC, vendor)
- [ ] Enhance device type detection (mobile, PC, IoT)
- [ ] Export logs functionality
- [ ] Advanced firewall rules
- [ ] Network packet analysis
- [ ] Integration with router APIs

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Credits

- **Developer**: phoenixdev-512
- **Design**: Material 3 Design System
- **Icons**: Material Icons

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: [phoenixdev-512](https://github.com/phoenixdev-512)

## Changelog

### Version 1.0 (Current)
- ✅ Launcher icon branding with custom logo
- ✅ Real-time network speed monitoring (0.5s updates)
- ✅ Comprehensive security scanning
- ✅ WiFi network discovery
- ✅ Device detection on local network
- ✅ Per-app network activity tracking
- ✅ Dynamic security score calculation
- ✅ Settings with persistence
- ✅ Material 3 UI with animations
- ✅ Quick actions for common tasks
- ✅ Navigation drawer with app branding

---

Made with ❤️ by phoenixdev-512
