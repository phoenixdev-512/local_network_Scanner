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

// SENET Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = SenetColors.NavyLight,           // Primary action buttons, FABs
    onPrimary = SenetColors.WhitePure,
    primaryContainer = SenetColors.NavyBase,
    onPrimaryContainer = SenetColors.NavyLight,
    
    secondary = SenetColors.NavyBase,
    onSecondary = SenetColors.WhitePure,
    
    surface = SenetColors.DarkGray,
    onSurface = SenetColors.WhitePure,
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF2A2A2A),
    onSurfaceVariant = SenetColors.MediumGray,
    
    background = SenetColors.NavyDark,
    onBackground = SenetColors.WhitePure,
    
    error = SenetColors.Error,
    onError = SenetColors.WhitePure,
    
    outline = SenetColors.NavyLight
)

// SENET Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = SenetColors.NavyBase,            // Primary action buttons, FABs
    onPrimary = SenetColors.WhitePure,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFEBF2FF),
    onPrimaryContainer = SenetColors.NavyDark,
    
    secondary = SenetColors.NavyLight,
    onSecondary = SenetColors.WhitePure,
    
    surface = SenetColors.WhitePure,
    onSurface = SenetColors.Black,
    surfaceVariant = SenetColors.LightGray,
    onSurfaceVariant = SenetColors.MediumGray,
    
    background = SenetColors.WhitePure,
    onBackground = SenetColors.Black,
    
    error = SenetColors.Error,
    onError = SenetColors.WhitePure,
    
    outline = SenetColors.NavyBase
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
            window.statusBarColor = SenetColors.NavyDark.toArgb()
            window.navigationBarColor = SenetColors.DarkGray.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SenetTypography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp)
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
