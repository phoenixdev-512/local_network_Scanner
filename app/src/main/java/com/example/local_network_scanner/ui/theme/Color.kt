package com.example.local_network_scanner.ui.theme

import androidx.compose.ui.graphics.Color

// SENET Premium Color Palette System
object SenetColors {
    // Primary Navy Blue Palette
    val NavyDark = Color(0xFF0A1931)      // Deep Navy
    val NavyBase = Color(0xFF1E3A8A)      // Base Navy
    val NavyLight = Color(0xFF3B82F6)     // Light Navy
    
    // Neutral Palette
    val Black = Color(0xFF000000)
    val WhitePure = Color(0xFFFFFFFF)
    val DarkGray = Color(0xFF1A1A1A)
    val LightGray = Color(0xFFF5F5F5)
    val MediumGray = Color(0xFF8C8C8C)
    
    // Semantic Colors
    val Success = Color(0xFF00C853)
    val Warning = Color(0xFFFFD600)
    val Error = Color(0xFFD32F2F)
    val Info = Color(0xFF1E88E5)
    
    // Gradients
    val darkGradient = listOf(NavyDark, NavyBase)
    val lightGradient = listOf(WhitePure, LightGray)
}

// Legacy colors (kept for compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Legacy colors maintained for backward compatibility
val DeepNavy = SenetColors.NavyDark
val ElectricBlue = SenetColors.NavyLight
val PrimaryBlue = SenetColors.Info

val VibrантGreen = SenetColors.Success
val WarningOrange = SenetColors.Warning
val ThreatRed = SenetColors.Error
val InfoCyan = SenetColors.Info

val TrueBlack = SenetColors.Black
val DarkBackground = Color(0xFF0F1419)
val SurfaceDarkGray = SenetColors.DarkGray
val CardBackground = Color(0xFF252B36)

val TextPrimary = SenetColors.WhitePure
val TextSecondary = Color(0xFFB8C1D0)
val TextTertiary = SenetColors.MediumGray

val StatusExcellent = SenetColors.Success
val StatusGood = Color(0xFF76FF03)
val StatusFair = SenetColors.Warning
val StatusPoor = SenetColors.Error

val DangerRed = Color(0xFFD32F2F)

val GradientStart = SenetColors.NavyDark
val GradientMiddle = Color(0xFF1A2332)
val GradientEnd = SenetColors.Black