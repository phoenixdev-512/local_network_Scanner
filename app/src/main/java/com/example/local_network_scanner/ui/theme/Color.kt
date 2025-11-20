package com.example.local_network_scanner.ui.theme

import androidx.compose.ui.graphics.Color

// SENET Neo-Glassmorphism Color Palette System
object SenetColors {
    // Primary Navy Blue Palette - Deep navy to black gradients
    val NavyDark = Color(0xFF0A1931)      // Deep Navy (primary background start)
    val NavyMid = Color(0xFF232946)       // Mid Navy (background transition)
    val NavyBase = Color(0xFF1E3A8A)      // Base Navy
    val NavyLight = Color(0xFF3B82F6)     // Light Navy / Neon Blue accent
    
    // Neon Blue Accent Palette - For highlights, buttons, and glow effects
    val NeonBlue = Color(0xFF2CCEF0)      // Bright neon blue primary
    val NeonBlueAlt = Color(0xFF3B82F6)   // Alternative neon blue
    val ElectricBlue = Color(0xFF0EA5E9)  // Electric blue for active states
    
    // Glassmorphism Surface Colors - Semi-transparent with blur
    val GlassSurface = Color(0x0FFFFFFF)  // White with 6% opacity for glass cards
    val GlassSurfaceLight = Color(0x1AFFFFFF) // White with 10% opacity for elevated glass
    val GlassStroke = Color(0x80)         // Neon blue stroke at 50% for glass borders
    
    // Neutral Palette
    val Black = Color(0xFF000000)
    val TrueBlack = Color(0xFF000000)
    val WhitePure = Color(0xFFFFFFFF)
    val DarkGray = Color(0xFF1A1A1A)
    val LightGray = Color(0xFFF5F5F5)
    val MediumGray = Color(0xFF8C8C8C)
    
    // Text Colors - High contrast for readability
    val TextPrimary = Color(0xFFFFFFFF)   // Pure white for primary text
    val TextSecondary = Color(0xFFB8C1D0) // Soft white-blue for secondary text
    val TextTertiary = Color(0xFF8C8C8C)  // Gray for tertiary text
    
    // Semantic Colors with Glow variants
    val Success = Color(0xFF00C853)
    val SuccessGlow = Color(0xFF00E676)   // Brighter for glow effects
    val Warning = Color(0xFFFFD600)
    val WarningGlow = Color(0xFFFFEA00)
    val Error = Color(0xFFD32F2F)
    val ErrorGlow = Color(0xFFF44336)
    val Info = Color(0xFF1E88E5)
    
    // Gradients for backgrounds
    val darkGradientStart = NavyDark
    val darkGradientMid = Color(0xFF1A2332)
    val darkGradientEnd = Black
    val lightGradient = listOf(WhitePure, LightGray)
    
    // Legacy support
    val darkGradient = listOf(NavyDark, NavyMid)
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