# UI Module

## Overview

The UI module contains all user interface components built with Jetpack Compose and Material 3 design. The module follows a screen-based organization with shared components and theming.

## Screens

### DashboardScreen

**File**: `DashboardScreen.kt`  
**Route**: `dashboard`

Main dashboard with real-time network metrics and quick actions.

**Components**:
- SpeedTestWidget - Network speed gauge
- SecurityOverviewWidget - Security score and threats
- DataUsageWidget - Data consumption visualization
- ConnectedDevicesWidget - Device count display
- QuickActionsWidget - Common action buttons

### SecurityScreen

**File**: `SecurityScreen.kt`  
**Route**: `security`

Security scanning and threat detection interface.

**Features**:
- Deep security scan button
- Progress indicator
- Suspicious apps list
- Security metrics cards
- Uninstall actions

### NetworkScannerScreen

**File**: `NetworkScannerScreen.kt`  
**Route**: `network`

WiFi network and device discovery interface.

**Features**:
- WiFi network list with signal strength
- Sort and filter options
- Device discovery results
- Connection information
- Network selection

### ActivityScreen

**File**: `ActivityScreen.kt`  
**Route**: `activity`

Per-app network activity tracking.

**Features**:
- Last 5 minutes activity
- Search and filter
- Sort options
- App icons and names
- Data usage display

### SettingsScreen

**File**: `EnhancedSettingsScreen.kt`  
**Route**: `settings`

Application settings and configuration.

**Categories**:
- Firewall controls
- DNS configuration
- Notification preferences
- Appearance options

## ViewModels

### DashboardViewModel

Manages dashboard state and coordinates services.

**State**:
- Network speed
- Security score
- Connected devices
- Data usage statistics

### SecurityViewModel

Handles security scanning operations.

**State**:
- Scan progress
- Suspicious apps
- Security metrics
- Scan completion status

### WifiViewModel

Manages WiFi operations and network scanning.

**State**:
- Current SSID
- Signal strength
- Scan results
- Permission status

### ActivityViewModel

Tracks network activity per application.

**State**:
- Recent activity (5 minutes)
- Data usage stats
- Loading state

### SettingsViewModel

Manages application settings and preferences.

**State**:
- Firewall settings
- DNS configuration
- Notification preferences

## Components

### LoadingSkeletons

**File**: `components/LoadingSkeletons.kt`

Skeleton loading states for async content.

**Variants**:
- Card skeleton
- List item skeleton
- Metric skeleton

### Custom Components

Reusable UI components:
- Animated metric displays
- Security score indicators
- Network strength bars
- Device cards

## Theme

### Colors

**File**: `theme/Color.kt`

Color palette based on Material 3:
- Electric Blue (#00D4FF)
- Deep Navy (#0A0E27)
- Vibrant Green (#00FF88)
- Warning Orange (#FFB800)
- Threat Red (#FF3D00)

### Typography

**File**: `theme/Type.kt`

Typography scale following Material 3 guidelines.

### Spacing

**File**: `theme/Spacing.kt`

Consistent spacing values:
- ExtraSmall: 4dp
- Small: 8dp
- Medium: 16dp
- Large: 24dp
- ExtraLarge: 32dp

### Theme

**File**: `theme/Theme.kt`

Main theme composable with dark mode optimization.

## Navigation

Navigation structure using Navigation Compose:

```kotlin
NavHost(navController, startDestination = "dashboard") {
    composable("dashboard") { DashboardScreen() }
    composable("network") { NetworkScannerScreen() }
    composable("security") { SecurityScreen() }
    composable("activity") { ActivityScreen() }
    composable("settings") { EnhancedSettingsScreen() }
}
```

## Animations

### Screen Transitions

- Fade in/out
- Slide horizontal
- Crossfade

### Value Animations

- Spring animations for metrics
- Tween animations for smooth updates
- Infinite animations for live indicators

### Progress Animations

- Linear progress indicators
- Circular progress for scanning
- Pulsing dots for live status

## Best Practices

### Composable Structure

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    MyScreenContent(
        state = state,
        onAction = viewModel::handleAction
    )
}

@Composable
private fun MyScreenContent(
    state: MyState,
    onAction: (Action) -> Unit
) {
    // UI implementation
}
```

### State Management

- Use `collectAsState()` for StateFlow
- Hoist state when possible
- Separate stateful and stateless composables

### Performance

- Use `remember` for expensive calculations
- Avoid unnecessary recompositions
- Use `key` for list items

## Testing

UI tests are located in `app/src/androidTest/`

Run UI tests:
```bash
./gradlew connectedAndroidTest
```

## Material 3 Guidelines

All UI components follow Material 3 design:
- Use Material 3 components
- Follow elevation system
- Implement proper touch targets
- Support dynamic color (future)

## Accessibility

- Content descriptions for all interactive elements
- Proper semantic structure
- Minimum touch target size (48dp)
- Color contrast compliance

## Future Enhancements

- Splash screen with branding
- About page
- Onboarding flow
- More detailed charts
- Export functionality
- Widget support
