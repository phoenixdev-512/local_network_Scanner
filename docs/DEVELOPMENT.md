# NetSentry Development Guide

## Getting Started

This guide will help you set up your development environment and start contributing to NetSentry.

## Prerequisites

### Required Software

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **JDK**: Version 11 or later
- **Android SDK**: 
  - Minimum SDK: 24 (Android 7.0)
  - Target SDK: 34 (Android 14)
  - Compile SDK: 34
- **Gradle**: 8.13 (included in wrapper)
- **Kotlin**: 1.9.22

### Recommended Tools

- **Git**: For version control
- **ADB**: Android Debug Bridge for device testing
- **Scrcpy**: For device screen mirroring during development

## Environment Setup

### 1. Clone the Repository

```bash
git clone https://github.com/phoenixdev-512/local_network_Scanner.git
cd local_network_Scanner
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository
4. Click "OK"

### 3. Sync Gradle

Android Studio will automatically prompt to sync Gradle files. If not:

1. Click "File" > "Sync Project with Gradle Files"
2. Wait for dependencies to download

### 4. Configure SDK

Ensure Android SDK is properly configured:

1. Go to "File" > "Project Structure"
2. Under "SDK Location", verify Android SDK path
3. Install required SDK platforms and tools if prompted

## Project Structure

```
local_network_Scanner/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/local_network_scanner/
│   │   │   │   ├── data/           # Data layer
│   │   │   │   ├── di/             # Dependency injection
│   │   │   │   ├── services/       # Core services
│   │   │   │   ├── ui/             # UI components
│   │   │   │   ├── util/           # Utilities
│   │   │   │   └── vpn/            # VPN service
│   │   │   ├── res/                # Resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                   # Unit tests
│   │   └── androidTest/            # Instrumented tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── docs/                           # Documentation
├── gradle/                         # Gradle wrapper
├── build.gradle.kts               # Root build file
├── settings.gradle.kts
└── README.md
```

## Building the Project

### Debug Build

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

Note: Release builds require signing configuration.

### Install on Device

```bash
./gradlew installDebug
```

## Running Tests

### Unit Tests

Execute all unit tests:

```bash
./gradlew test
```

Execute tests for specific build variant:

```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests

Run on connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

### Code Coverage

Generate coverage report:

```bash
./gradlew jacocoTestReport
```

Report location: `app/build/reports/jacoco/`

## Code Style

### Kotlin Style Guide

NetSentry follows the official Kotlin coding conventions:

- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use descriptive variable names
- Document public APIs

### Formatting

Android Studio configuration includes:

1. Go to "File" > "Settings" > "Editor" > "Code Style" > "Kotlin"
2. Select "Set from..." > "Kotlin style guide"

Auto-format code:
- Windows/Linux: `Ctrl + Alt + L`
- macOS: `Cmd + Option + L`

### Linting

Run lint checks:

```bash
./gradlew lint
```

View lint report: `app/build/reports/lint-results.html`

## Dependency Management

### Adding Dependencies

Edit `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    
    // For testing
    testImplementation("junit:junit:4.13.2")
}
```

Always use specific versions (avoid `+` wildcards).

### Updating Dependencies

1. Check for updates in `build.gradle.kts`
2. Update version numbers
3. Sync Gradle
4. Run tests to ensure compatibility

## Architecture Guidelines

### MVVM Pattern

Follow the MVVM architecture:

```kotlin
// ViewModel
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state: StateFlow<MyState> = _state.asStateFlow()
}

// UI
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    // Render UI based on state
}
```

### Dependency Injection

Use Hilt for dependency injection:

```kotlin
@Singleton
class MyService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Implementation
}
```

### StateFlow

Expose state reactively:

```kotlin
private val _data = MutableStateFlow<List<Item>>(emptyList())
val data: StateFlow<List<Item>> = _data.asStateFlow()
```

## UI Development

### Jetpack Compose

All UI is built with Compose:

```kotlin
@Composable
fun MyComponent() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Hello NetSentry")
    }
}
```

### Material 3

Use Material 3 components:

```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    // Content
}
```

### Theme

Extend the app theme in `ui/theme/`:

```kotlin
// Color.kt
val ElectricBlue = Color(0xFF00D4FF)

// Theme.kt
@Composable
fun NetSentryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme,
        content = content
    )
}
```

## Testing Guidelines

### Unit Tests

Test ViewModels and business logic:

```kotlin
class MyViewModelTest {
    @Test
    fun `test state updates correctly`() {
        val viewModel = MyViewModel(FakeRepository())
        // Assert initial state
        // Trigger action
        // Assert new state
    }
}
```

### UI Tests

Test Compose UI:

```kotlin
class MyScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `displays content correctly`() {
        composeTestRule.setContent {
            MyScreen()
        }
        composeTestRule.onNodeWithText("Expected Text").assertExists()
    }
}
```

## Debugging

### Logcat

Use Android's logging:

```kotlin
import android.util.Log

private const val TAG = "MyClass"

Log.d(TAG, "Debug message")
Log.e(TAG, "Error message", exception)
```

### Breakpoints

1. Click line number gutter to set breakpoint
2. Run in debug mode
3. Inspect variables and call stack

### Layout Inspector

View UI hierarchy:

1. Run app on device
2. Tools > Layout Inspector
3. Select process

## Performance Optimization

### Memory Profiling

1. Run app
2. View > Tool Windows > Profiler
3. Select Memory profiler
4. Analyze allocations and leaks

### Network Profiling

Monitor network calls:

1. View > Tool Windows > Profiler
2. Select Network profiler
3. Inspect requests and responses

## Version Control

### Git Workflow

1. Create feature branch: `git checkout -b feature/my-feature`
2. Make changes and commit: `git commit -m "Add feature"`
3. Push branch: `git push origin feature/my-feature`
4. Create pull request on GitHub

### Commit Messages

Follow conventional commits:

```
feat: add network device filtering
fix: correct ping calculation
docs: update API documentation
test: add unit tests for SecurityAnalyzer
refactor: simplify NetworkMonitor logic
```

## Continuous Integration

### GitHub Actions

Automated workflows run on:
- Pull requests
- Pushes to main branch

Workflows include:
- Build verification
- Unit tests
- Lint checks

## Release Process

### Version Management

Update version in `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        versionCode = 2
        versionName = "1.1.0"
    }
}
```

### Signing Configuration

Configure signing for release:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/keystore.jks")
            storePassword = "password"
            keyAlias = "key-alias"
            keyPassword = "password"
        }
    }
}
```

Note: Never commit keystore or passwords to repository.

### Release Checklist

- [ ] Update version code and name
- [ ] Update CHANGELOG.md
- [ ] Run all tests
- [ ] Generate signed APK
- [ ] Test APK on multiple devices
- [ ] Create GitHub release
- [ ] Update documentation

## Common Issues

### Gradle Sync Fails

1. Check internet connection
2. Invalidate caches: File > Invalidate Caches
3. Delete `.gradle` folder and re-sync

### Build Errors

1. Clean project: Build > Clean Project
2. Rebuild: Build > Rebuild Project
3. Update Gradle wrapper if needed

### Permission Errors

1. Check AndroidManifest.xml for required permissions
2. Ensure runtime permission requests in code
3. Test on different Android versions

## Getting Help

### Resources

- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io)

### Community

- Open an issue on GitHub
- Contact: phoenixdev-512

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed contribution guidelines.

## License

NetSentry is licensed under the MIT License. See LICENSE file for details.
