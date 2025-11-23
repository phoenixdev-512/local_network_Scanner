package com.example.local_network_scanner.ui.theme

import android.os.Build
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable

// Material 3 Color Schemes
val DarkColorScheme = darkColorScheme(
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

val LightColorScheme = lightColorScheme(
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