# Local Network Scanner - Deployment Guide

## Prerequisites

1. **Android Studio** (latest version recommended)
2. **JDK 17** or higher
3. **Android SDK** with API Level 34
4. **Google Maps API Key** (required for map features)

## Setup Instructions

### 1. Configure Google Maps API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable **Maps SDK for Android**
4. Create credentials → API Key
5. (Optional) Restrict the API key to Android apps with your package name
6. Copy your API key
7. Open `local.properties` in the project root
8. Replace `YOUR_MAPS_API_KEY_HERE` with your actual API key:
   ```
   MAPS_API_KEY=AIza...your-actual-key-here
   ```

### 2. Build the Project

#### Debug Build (for testing)
```bash
./gradlew assembleDebug
```
The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

#### Release Build (for production)
```bash
./gradlew assembleRelease
```
The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

**Note:** The current release build uses debug signing. For production, you should:
1. Create a keystore file
2. Configure signing in `app/build.gradle.kts`

### 3. Creating a Keystore for Production (Recommended)

Generate a keystore:
```bash
keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
```

Add to `app/build.gradle.kts`:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../my-release-key.keystore")
            storePassword = "your-password"
            keyAlias = "my-key-alias"
            keyPassword = "your-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... rest of config
        }
    }
}
```

**Security Note:** Never commit your keystore or passwords to version control!

### 4. Install on Device

Via ADB:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or drag and drop the APK to your Android device and install.

## Required Permissions

The app requires the following permissions:
- **INTERNET** - Network scanning and VPN functionality
- **ACCESS_NETWORK_STATE** - Network state monitoring
- **ACCESS_WIFI_STATE** - WiFi scanning
- **CHANGE_WIFI_STATE** - WiFi management
- **ACCESS_FINE_LOCATION** - Required for WiFi scanning (Android requirement)
- **ACCESS_COARSE_LOCATION** - Location-based features
- **POST_NOTIFICATIONS** - Notification alerts
- **BIND_VPN_SERVICE** - VPN functionality

## Play Store Deployment

### 1. Prepare Release Build
- Ensure all API keys are configured
- Test thoroughly on multiple devices
- Create signed release APK or AAB

### 2. Generate AAB (Android App Bundle - Recommended)
```bash
./gradlew bundleRelease
```

### 3. Play Store Console
1. Create a new app in Google Play Console
2. Complete store listing (descriptions, screenshots, etc.)
3. Upload the signed AAB
4. Complete content rating questionnaire
5. Set pricing and distribution
6. Submit for review

## Privacy & Compliance

Since the app uses:
- **VPN Service**: Declare VPN usage in Play Store listing
- **Location**: Provide privacy policy explaining location usage
- **Network Scanning**: Explain network monitoring features

Create a privacy policy and link it in the Play Store listing.

## Testing Checklist

- [ ] VPN service starts and stops correctly
- [ ] Network scanning works
- [ ] WiFi scanning functions properly
- [ ] Map features load correctly
- [ ] Notifications appear
- [ ] Background workers function
- [ ] App works on different Android versions (API 24+)
- [ ] No crashes on rotation
- [ ] Permissions are requested properly

## Troubleshooting

### Build Fails
- Run `./gradlew clean build`
- Check that Maps API key is configured
- Verify JDK version

### Maps Don't Load
- Verify API key is correct
- Check that Maps SDK for Android is enabled in Google Cloud Console
- Ensure the API key is not restricted or is properly configured for your package

### VPN Service Issues
- Ensure VPN permission is granted
- Check that no other VPN is active

## App Features

1. **Network Scanning** - Scan local network for devices
2. **VPN Service** - Monitor and filter network traffic
3. **WiFi Analysis** - Analyze WiFi networks
4. **Geolocation** - Map view of network locations
5. **Quick Settings Tile** - Toggle VPN from quick settings
6. **Background Monitoring** - Weekly summaries and alerts

## Support

For issues or questions, check the logs:
```bash
adb logcat | grep NetSentry
```

## Version Information

- **Version Code**: 1
- **Version Name**: 1.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
