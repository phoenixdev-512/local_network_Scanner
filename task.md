<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

# okay now i want the whole UI to match Google's UI, just like how google's apps looks like and feel like, animations, sound effects

generate a detailed prompt for gemini 3 to build the same for me

Here's a comprehensive prompt for **Gemini/GitHub Copilot** to transform SENET into a Google-style app with authentic Material Design 3, animations, transitions, and sound effects:

***

**DETAILED PROMPT: TRANSFORM SENET TO GOOGLE-STYLE UI/UX WITH MATERIAL DESIGN 3**

***

Completely redesign the SENET app to match Google's signature design language, interaction patterns, animations, and sound feedback system. The app should feel indistinguishable from first-party Google apps (Gmail, Google Home, Google Keep, Google Photos) in terms of visual polish, motion design, and auditory feedback.

***

## 1. MATERIAL DESIGN 3 (MATERIAL YOU) FOUNDATION

### Color System - Dynamic Color

**Implement Material You dynamic theming:**

```kotlin
// ui/theme/Color.kt
import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun SenetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = GoogleTypography,
        shapes = GoogleShapes,
        content = content
    )
}

// Fallback color schemes (when dynamic color unavailable)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBBC7FF),
    onPrimary = Color(0xFF002488),
    primaryContainer = Color(0xFF0039A2),
    onPrimaryContainer = Color(0xFFDEE1FF),
    secondary = Color(0xFFC1C6DD),
    onSecondary = Color(0xFF2A2F42),
    secondaryContainer = Color(0xFF404659),
    onSecondaryContainer = Color(0xFFDDE1F9),
    tertiary = Color(0xFFE2BAD9),
    onTertiary = Color(0xFF422741),
    tertiaryContainer = Color(0xFF5A3D59),
    onTertiaryContainer = Color(0xFFFFD7F3),
    background = Color(0xFF1A1B20),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF1A1B20),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF45464E),
    onSurfaceVariant = Color(0xFFC6C5D0),
    outline = Color(0xFF8F9099),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0051C5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDEE1FF),
    onPrimaryContainer = Color(0xFF001847),
    secondary = Color(0xFF575E71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCE1F9),
    onSecondaryContainer = Color(0xFF141B2B),
    tertiary = Color(0xFF725572),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFDD7F9),
    onTertiaryContainer = Color(0xFF2A132C),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1A1B20),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1A1B20),
    surfaceVariant = Color(0xFFE2E1EC),
    onSurfaceVariant = Color(0xFF45464E),
    outline = Color(0xFF75767F),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
)
```


### Typography - Google Sans

```kotlin
// ui/theme/Type.kt
val GoogleTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default, // Use Google Sans if available
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```


### Shapes

```kotlin
// ui/theme/Shape.kt
val GoogleShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
```


***

## 2. GOOGLE-STYLE ANIMATIONS \& MOTION

### Navigation Transitions

**Implement Material Motion system with predictive back:**

```kotlin
// ui/animation/GoogleMotion.kt
object GoogleMotionSpecs {
    // Duration tokens (Google standard)
    object Duration {
        const val SHORT1 = 50
        const val SHORT2 = 100
        const val SHORT3 = 150
        const val SHORT4 = 200
        const val MEDIUM1 = 250
        const val MEDIUM2 = 300
        const val MEDIUM3 = 350
        const val MEDIUM4 = 400
        const val LONG1 = 450
        const val LONG2 = 500
        const val LONG3 = 550
        const val LONG4 = 600
        const val EXTRA_LONG1 = 700
        const val EXTRA_LONG2 = 800
        const val EXTRA_LONG3 = 900
        const val EXTRA_LONG4 = 1000
    }
    
    // Easing curves (Google standard)
    val standardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val standardAccelerate = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
    val standardDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
    val emphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val emphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    val emphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    
    // Shared Axis - Z (depth)
    fun sharedAxisZForward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    fun sharedAxisZBackward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 1.1f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    // Shared Axis - X (horizontal)
    fun sharedAxisXForward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = slideInHorizontally(
                initialOffsetX = { it / 3 },
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ) + fadeIn(animationSpec = tween(Duration.LONG2)),
            initialContentExit = slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(Duration.LONG2, easing = emphasizedAccelerate)
            ) + fadeOut(animationSpec = tween(Duration.LONG2))
        )
    }
    
    // Container Transform (card to detail)
    fun containerTransform(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, delayMillis = 60, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    // Fade Through (switching tabs)
    fun fadeThrough(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.SHORT4, delayMillis = Duration.SHORT4)
            ) + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(Duration.SHORT4, delayMillis = Duration.SHORT4)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4)
            ) + scaleOut(
                targetScale = 0.92f,
                animationSpec = tween(Duration.SHORT4)
            )
        )
    }
}
```


### Predictive Back Gesture

```kotlin
// Implement predictive back for Android 13+
@Composable
fun PredictiveBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
    }
    
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    
    DisposableEffect(backDispatcher, enabled) {
        backCallback.isEnabled = enabled
        backDispatcher?.addCallback(backCallback)
        
        onDispose {
            backCallback.remove()
        }
    }
}
```


***

## 3. SOUND EFFECTS SYSTEM

### Sound Pool Implementation

**Create Google-style haptic and audio feedback:**

```kotlin
// ui/feedback/SoundFeedback.kt
class SoundFeedbackManager(private val context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()
    
    // Sound IDs
    private var tapSoundId: Int = 0
    private var successSoundId: Int = 0
    private var errorSoundId: Int = 0
    private var toggleSoundId: Int = 0
    private var swipeSoundId: Int = 0
    private var notificationSoundId: Int = 0
    
    init {
        loadSounds()
    }
    
    private fun loadSounds() {
        // Load Google-style sound assets
        tapSoundId = soundPool.load(context, R.raw.sound_tap, 1)
        successSoundId = soundPool.load(context, R.raw.sound_success, 1)
        errorSoundId = soundPool.load(context, R.raw.sound_error, 1)
        toggleSoundId = soundPool.load(context, R.raw.sound_toggle, 1)
        swipeSoundId = soundPool.load(context, R.raw.sound_swipe, 1)
        notificationSoundId = soundPool.load(context, R.raw.sound_notification, 1)
    }
    
    fun playTap() {
        soundPool.play(tapSoundId, 0.3f, 0.3f, 1, 0, 1.0f)
    }
    
    fun playSuccess() {
        soundPool.play(successSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
    
    fun playError() {
        soundPool.play(errorSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
    
    fun playToggle(isOn: Boolean) {
        val pitch = if (isOn) 1.2f else 0.8f
        soundPool.play(toggleSoundId, 0.4f, 0.4f, 1, 0, pitch)
    }
    
    fun playSwipe() {
        soundPool.play(swipeSoundId, 0.2f, 0.2f, 1, 0, 1.0f)
    }
    
    fun playNotification() {
        soundPool.play(notificationSoundId, 0.6f, 0.6f, 1, 0, 1.0f)
    }
    
    fun release() {
        soundPool.release()
    }
}

// DI Module
@Module
@InstallIn(SingletonComponent::class)
object SoundModule {
    @Provides
    @Singleton
    fun provideSoundFeedbackManager(@ApplicationContext context: Context): SoundFeedbackManager {
        return SoundFeedbackManager(context)
    }
}
```


### Haptic Feedback

```kotlin
// ui/feedback/HapticFeedback.kt
class HapticFeedbackManager(private val context: Context) {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    fun lightTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(10)
        }
    }
    
    fun mediumTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(20)
        }
    }
    
    fun heavyTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
    
    fun success() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        }
    }
    
    fun error() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 50, 50, 50)
            val amplitudes = intArrayOf(0, 128, 0, 255)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        }
    }
}
```


### Integration with Composables

```kotlin
// Use in composables
@Composable
fun GoogleStyleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    soundManager: SoundFeedbackManager = hiltViewModel<MainViewModel>().soundManager,
    hapticManager: HapticFeedbackManager = hiltViewModel<MainViewModel>().hapticManager
) {
    Button(
        onClick = {
            soundManager.playTap()
            hapticManager.lightTap()
            onClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors()
    ) {
        Text(text)
    }
}

@Composable
fun GoogleStyleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    soundManager: SoundFeedbackManager = hiltViewModel<MainViewModel>().soundManager,
    hapticManager: HapticFeedbackManager = hiltViewModel<MainViewModel>().hapticManager
) {
    Switch(
        checked = checked,
        onCheckedChange = { newValue ->
            soundManager.playToggle(newValue)
            hapticManager.mediumTap()
            onCheckedChange(newValue)
        },
        modifier = modifier
    )
}
```


***

## 4. GOOGLE-STYLE COMPONENTS

### Material3 Cards

```kotlin
@Composable
fun GoogleCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick ?: {},
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
```


### Material3 FAB with animation

```kotlin
@Composable
fun GoogleFAB(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    text: String? = null
) {
    val animatedModifier = modifier.animateContentSize(
        animationSpec = tween(
            durationMillis = GoogleMotionSpecs.Duration.MEDIUM2,
            easing = GoogleMotionSpecs.standardEasing
        )
    )
    
    if (expanded && text != null) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = { Icon(icon, contentDescription) },
            text = { Text(text) },
            modifier = animatedModifier
        )
    } else {
        FloatingActionButton(
            onClick = onClick,
            modifier = animatedModifier
        ) {
            Icon(icon, contentDescription)
        }
    }
}
```


### Material3 Top App Bar

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleTopAppBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(navigationIcon, contentDescription = "Navigate back")
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}
```


### Material3 Bottom Navigation

```kotlin
@Composable
fun GoogleBottomNavigation(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    items: List<BottomNavItem>
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val label: String
)
```


### Snackbar with Action

```kotlin
@Composable
fun GoogleSnackbar(
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    Snackbar(
        action = {
            if (actionLabel != null && onActionClick != null) {
                TextButton(onClick = onActionClick) {
                    Text(actionLabel)
                }
            }
        },
        dismissAction = {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss")
            }
        }
    ) {
        Text(message)
    }
}
```


***

## 5. NAVIGATION SETUP

```kotlin
@Composable
fun SenetNavGraph(
    navController: NavHostController,
    startDestination: String = "dashboard"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { GoogleMotionSpecs.sharedAxisXForward() },
        exitTransition = { GoogleMotionSpecs.sharedAxisXForward() },
        popEnterTransition = { GoogleMotionSpecs.sharedAxisZBackward() },
        popExitTransition = { GoogleMotionSpecs.sharedAxisZBackward() }
    ) {
        composable("dashboard") {
            DashboardScreen(navController)
        }
        
        composable(
            "details/{id}",
            enterTransition = { GoogleMotionSpecs.containerTransform() },
            popExitTransition = { GoogleMotionSpecs.containerTransform() }
        ) { backStackEntry ->
            DetailsScreen(
                id = backStackEntry.arguments?.getString("id") ?: "",
                navController = navController
            )
        }
    }
}
```


***

## 6. SOUND ASSETS

**Required sound files (place in `res/raw/`):**

- `sound_tap.mp3` - Light click (50ms, subtle)
- `sound_success.mp3` - Success chime (200ms, pleasant ascending tone)
- `sound_error.mp3` - Error sound (150ms, descending tone)
- `sound_toggle.mp3` - Toggle switch (100ms, mechanical click)
- `sound_swipe.mp3` - Swipe gesture (80ms, whoosh)
- `sound_notification.mp3` - Notification (300ms, attention-grabbing)

**Source for Google-like sounds:**

- Use Freesound.org with Creative Commons license
- Search for: "UI click", "button tap", "toggle switch", "success notification"
- Ensure all sounds are short (<500ms), high quality, and match Google's minimal aesthetic

***

## 7. IMPLEMENTATION CHECKLIST

- [ ] Implement dynamic Material You theming
- [ ] Apply Google Typography across all text
- [ ] Implement all navigation transitions (Shared Axis, Container Transform, Fade Through)
- [ ] Set up SoundFeedbackManager with sound assets
- [ ] Set up HapticFeedbackManager
- [ ] Integrate sound and haptic feedback in all buttons, switches, and interactive elements
- [ ] Replace all cards with Material3 GoogleCard style
- [ ] Update Top App Bars to GoogleTopAppBar
- [ ] Update Bottom Navigation to Material3 NavigationBar
- [ ] Implement predictive back gesture
- [ ] Test animations at 60fps on real devices
- [ ] Test sound feedback with various device volumes
- [ ] Test haptic feedback on devices with/without vibrator

***

**DELIVERABLE:**

A completely Google-ified SENET app with authentic Material Design 3, dynamic theming, smooth predictive animations, subtle sound effects, and haptic feedback that matches the polish and feel of first-party Google applications.

***

Use this prompt to transform SENET into a premium Google-style app with world-class UX.

