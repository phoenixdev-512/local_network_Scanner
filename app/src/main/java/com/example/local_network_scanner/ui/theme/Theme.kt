package com.example.local_network_scanner.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// SENET Neo-Glassmorphism Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = SenetColors.NeonBlue,           // Primary action buttons, FABs - Neon Blue
    onPrimary = SenetColors.WhitePure,
    primaryContainer = SenetColors.NavyBase,
    onPrimaryContainer = SenetColors.NeonBlue,
    
    secondary = SenetColors.ElectricBlue,
    onSecondary = SenetColors.WhitePure,
    
    surface = SenetColors.GlassSurface,
    onSurface = SenetColors.TextPrimary,
    surfaceVariant = SenetColors.GlassSurfaceLight,
    onSurfaceVariant = SenetColors.TextSecondary,
    
    background = SenetColors.NavyDark,
    onBackground = SenetColors.TextPrimary,
    
    error = SenetColors.Error,
    onError = SenetColors.WhitePure,
    
    outline = SenetColors.NeonBlue.copy(alpha = 0.5f)
)

// SENET Neo-Glassmorphism Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = SenetColors.NeonBlue,            // Primary action buttons, FABs - Neon Blue
    onPrimary = SenetColors.WhitePure,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFE3F2FD),
    onPrimaryContainer = SenetColors.NavyDark,
    
    secondary = SenetColors.ElectricBlue,
    onSecondary = SenetColors.WhitePure,
    
    surface = SenetColors.WhitePure.copy(alpha = 0.95f),
    onSurface = SenetColors.NavyDark,
    surfaceVariant = SenetColors.LightGray.copy(alpha = 0.8f),
    onSurfaceVariant = SenetColors.TextTertiary,
    
    background = androidx.compose.ui.graphics.Color(0xFFF8FAFB),
    onBackground = SenetColors.NavyDark,
    
    error = SenetColors.Error,
    onError = SenetColors.WhitePure,
    
    outline = SenetColors.NeonBlue.copy(alpha = 0.4f)
)

// Legacy color scheme for backward compatibility
private val SpeedtestDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = TextPrimary,
    primaryContainer = DeepNavy,
    onPrimaryContainer = TextPrimary,
    
    secondary = InfoCyan,
    onSecondary = TrueBlack,
    secondaryContainer = CardBackground,
    onSecondaryContainer = TextPrimary,
    
    tertiary = VibrантGreen,
    onTertiary = TrueBlack,
    tertiaryContainer = CardBackground,
    onTertiaryContainer = TextPrimary,
    
    error = ThreatRed,
    onError = TextPrimary,
    errorContainer = ThreatRed.copy(alpha = 0.2f),
    onErrorContainer = ThreatRed,
    
    background = TrueBlack,
    onBackground = TextPrimary,
    
    surface = SurfaceDarkGray,
    onSurface = TextPrimary,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary,
    
    outline = TextTertiary,
    outlineVariant = CardBackground,
    
    inverseSurface = TextPrimary,
    inverseOnSurface = TrueBlack,
    inversePrimary = DeepNavy,
)

private val SpeedtestLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = TextPrimary,
    primaryContainer = ElectricBlue.copy(alpha = 0.1f),
    onPrimaryContainer = DeepNavy,
    
    secondary = InfoCyan,
    onSecondary = TextPrimary,
    secondaryContainer = InfoCyan.copy(alpha = 0.1f),
    onSecondaryContainer = DeepNavy,
    
    tertiary = VibrантGreen,
    onTertiary = TextPrimary,
    tertiaryContainer = VibrантGreen.copy(alpha = 0.1f),
    onTertiaryContainer = DeepNavy,
    
    error = ThreatRed,
    onError = TextPrimary,
    errorContainer = ThreatRed.copy(alpha = 0.1f),
    onErrorContainer = ThreatRed,
    
    background = androidx.compose.ui.graphics.Color.White,
    onBackground = DeepNavy,
    
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = DeepNavy,
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
    onSurfaceVariant = TextTertiary,
    
    outline = TextTertiary,
)

@Composable
fun SenetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Use deep navy gradient background for status bar
            window.statusBarColor = SenetColors.NavyDark.toArgb()
            window.navigationBarColor = SenetColors.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SenetTypography,
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(18.dp),
            large = RoundedCornerShape(22.dp)
        ),
        content = content
    )
}

@Composable
fun NetSentryTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> SpeedtestDarkColorScheme
        else -> SpeedtestLightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = TrueBlack.toArgb()
            window.navigationBarColor = SurfaceDarkGray.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
