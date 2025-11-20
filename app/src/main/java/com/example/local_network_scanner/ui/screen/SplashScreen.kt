package com.example.local_network_scanner.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.local_network_scanner.ui.animation.SenetMotionSpecs
import com.example.local_network_scanner.ui.components.SenetLogo
import com.example.local_network_scanner.ui.theme.SenetColors
import kotlinx.coroutines.delay

/**
 * SENET Neo-Glassmorphism Splash Screen
 * 
 * Premium splash screen with SENET branding, glowing animations,
 * and deep navy gradient background.
 */
@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit
) {
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var glowVisible by remember { mutableStateOf(false) }
    
    // Logo scale animation with spring physics
    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )
    
    // Logo alpha animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(800),
        label = "logo_alpha"
    )
    
    // Text alpha animation
    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "text_alpha"
    )
    
    // Glow pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Launch animations and navigation
    LaunchedEffect(Unit) {
        delay(100)
        logoVisible = true
        delay(300)
        glowVisible = true
        delay(400)
        textVisible = true
        delay(1500)
        onNavigateToHome()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SenetColors.darkGradientStart,
                        SenetColors.darkGradientMid,
                        SenetColors.darkGradientEnd
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing background circle
        if (glowVisible) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .alpha(glowAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SenetColors.NeonBlue.copy(alpha = 0.3f),
                                SenetColors.NeonBlue.copy(alpha = 0.1f),
                                androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    )
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(logoAlpha)
                .scale(logoScale)
        ) {
            SenetLogo(size = 200.dp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Text(
                    text = "SENET",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.NeonBlue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Security Network Scanner",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SenetColors.TextSecondary
                )
            }
        }
    }
}
