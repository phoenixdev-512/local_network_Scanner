package com.example.local_network_scanner.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.local_network_scanner.ui.theme.SenetColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * GlassCard - Core glassmorphism card component
 * 
 * Frosted, semi-transparent card with blur effect, rounded corners, and optional neon border
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    withBorder: Boolean = true,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(22.dp),
                ambientColor = SenetColors.NeonBlue.copy(alpha = 0.1f),
                spotColor = SenetColors.NeonBlue.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SenetColors.GlassSurfaceLight,
                        SenetColors.GlassSurface
                    )
                )
            )
            .then(
                if (withBorder) {
                    Modifier.border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SenetColors.NeonBlue.copy(alpha = 0.5f),
                                SenetColors.NeonBlue.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
                } else Modifier
            )
            .padding(16.dp)
    ) {
        content()
    }
}

/**
 * GlowingGauge - Animated circular progress dial with glowing effect
 * 
 * Displays a value as a circular arc with glow and center text
 */
@Composable
fun GlowingGauge(
    value: Float,
    maxValue: Float = 100f,
    modifier: Modifier = Modifier,
    label: String = "",
    unit: String = "",
    size: Dp = 160.dp
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "gauge_value"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Background arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val radius = (this.size.minDimension - strokeWidth) / 2
            val center = Offset(this.size.width / 2, this.size.height / 2)
            
            // Background track
            drawArc(
                color = SenetColors.DarkGray.copy(alpha = 0.3f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            // Progress arc with glow
            val sweepAngle = (animatedValue / maxValue) * 270f
            
            // Outer glow layer
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        SenetColors.NeonBlue.copy(alpha = 0.3f),
                        SenetColors.ElectricBlue.copy(alpha = 0.5f),
                        SenetColors.NeonBlue.copy(alpha = 0.3f)
                    )
                ),
                startAngle = 135f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth + 8.dp.toPx(), cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius - 4.dp.toPx(), center.y - radius - 4.dp.toPx()),
                size = Size((radius + 4.dp.toPx()) * 2, (radius + 4.dp.toPx()) * 2),
                alpha = 0.4f
            )
            
            // Main progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        SenetColors.NeonBlue,
                        SenetColors.ElectricBlue,
                        SenetColors.NeonBlue
                    )
                ),
                startAngle = 135f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
        }
        
        // Center text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${animatedValue.toInt()}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = SenetColors.TextPrimary
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SenetColors.TextSecondary
                )
            }
            if (label.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = SenetColors.TextTertiary
                )
            }
        }
    }
}

/**
 * NeoButton - Rounded pill button with neon gradient and glow effect
 */
@Composable
fun NeoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .shadow(
                elevation = if (enabled) 12.dp else 4.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = SenetColors.NeonBlue.copy(alpha = 0.3f),
                spotColor = SenetColors.NeonBlue.copy(alpha = 0.5f)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = SenetColors.TextPrimary,
            disabledContainerColor = SenetColors.DarkGray,
            disabledContentColor = SenetColors.MediumGray
        ),
        shape = RoundedCornerShape(26.dp),
        enabled = enabled && !isLoading,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) {
                            listOf(
                                SenetColors.NeonBlue,
                                SenetColors.ElectricBlue,
                                SenetColors.NeonBlue
                            )
                        } else {
                            listOf(SenetColors.DarkGray, SenetColors.DarkGray)
                        }
                    )
                )
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = SenetColors.WhitePure,
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * AccentSwitch - Pill-style animated toggle with glow effect
 */
@Composable
fun AccentSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = SenetColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SenetColors.WhitePure,
                checkedTrackColor = SenetColors.NeonBlue,
                checkedBorderColor = SenetColors.NeonBlue,
                uncheckedThumbColor = SenetColors.MediumGray,
                uncheckedTrackColor = SenetColors.DarkGray,
                uncheckedBorderColor = SenetColors.DarkGray
            )
        )
    }
}

/**
 * NeoChartCard - Glass card wrapper for charts with glowing values
 */
@Composable
fun NeoChartCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    GlassCard(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SenetColors.TextPrimary
            )
            content()
        }
    }
}

/**
 * DashboardHeader - Profile header with glass background
 */
@Composable
fun DashboardHeader(
    userName: String = "User",
    greeting: String = "Hello",
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {}
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        withBorder = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = greeting,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = SenetColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.TextPrimary
                )
            }
            
            trailingContent()
        }
    }
}

/**
 * StatCard - Compact info card for dashboard metrics
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = SenetColors.NeonBlue
) {
    GlassCard(
        modifier = modifier,
        withBorder = false
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = SenetColors.TextTertiary,
                letterSpacing = 1.2.sp
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

/**
 * GlowingDot - Animated pulsing indicator dot
 */
@Composable
fun GlowingDot(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    color: Color = SenetColors.SuccessGlow
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )
    
    Box(
        modifier = modifier
            .size(12.dp)
            .background(
                color = if (isActive) color.copy(alpha = alpha) else SenetColors.DarkGray,
                shape = CircleShape
            )
    )
}

/**
 * AnimatedProgressBar - Linear progress with glow effect
 */
@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "progress"
    )
    
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(SenetColors.DarkGray.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            SenetColors.NeonBlue,
                            SenetColors.ElectricBlue
                        )
                    )
                )
        )
    }
}
