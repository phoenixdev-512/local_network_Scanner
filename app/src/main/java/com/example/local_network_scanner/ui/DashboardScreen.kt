package com.example.local_network_scanner.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.components.*
import com.example.local_network_scanner.ui.viewmodel.DashboardViewModel
import com.example.local_network_scanner.util.PermissionHelper

/**
 * Neo-Glassmorphism Dashboard Screen
 * Premium smart dashboard with frosted glass cards, glowing gauges, and animated metrics
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    navController: NavController? = null,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isMonitoring by viewModel.isMonitoring.collectAsState()
    val networkStats by viewModel.networkStats.collectAsState()
    val networkSpeed by viewModel.networkSpeed.collectAsState()
    val ping by viewModel.ping.collectAsState()
    
    // Check if usage stats permission is granted
    val hasUsageStatsPermission = remember { 
        mutableStateOf(PermissionHelper.hasUsageStatsPermission(context)) 
    }
    
    // Recheck permission when screen becomes visible
    LaunchedEffect(Unit) {
        hasUsageStatsPermission.value = PermissionHelper.hasUsageStatsPermission(context)
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
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Glassmorphic Header
            item {
                DashboardHeader(
                    userName = "Network User",
                    greeting = "Hello",
                    trailingContent = {
                        IconButton(onClick = { 
                            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Settings",
                                tint = SenetColors.TextPrimary
                            )
                        }
                    }
                )
            }
            
            // Permission banner if not granted
            if (!hasUsageStatsPermission.value) {
                item {
                    PermissionBanner(
                        onRequestPermission = {
                            if (context is Activity) {
                                PermissionHelper.requestUsageStatsPermission(context)
                            }
                        },
                        onDismiss = {
                            hasUsageStatsPermission.value = 
                                PermissionHelper.hasUsageStatsPermission(context)
                        }
                    )
                }
            }
            
            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = "Download",
                        value = "${String.format("%.1f", networkSpeed.downloadMbps)} Mbps",
                        modifier = Modifier.weight(1f),
                        valueColor = SenetColors.SuccessGlow
                    )
                    StatCard(
                        label = "Upload",
                        value = "${String.format("%.1f", networkSpeed.uploadMbps)} Mbps",
                        modifier = Modifier.weight(1f),
                        valueColor = SenetColors.ElectricBlue
                    )
                    StatCard(
                        label = "Ping",
                        value = if (ping >= 0) "$ping ms" else "--",
                        modifier = Modifier.weight(1f),
                        valueColor = when {
                            ping < 0 -> SenetColors.MediumGray
                            ping < 50 -> SenetColors.SuccessGlow
                            ping < 100 -> SenetColors.WarningGlow
                            else -> SenetColors.ErrorGlow
                        }
                    )
                }
            }
            
            // Main Network Speed Gauge
            item { 
                SpeedGaugeWidget(
                    downloadSpeed = networkSpeed.downloadMbps,
                    uploadSpeed = networkSpeed.uploadMbps,
                    isMonitoring = isMonitoring
                ) 
            }
            
            // Security Score Gauge
            item { 
                SecurityGaugeWidget(
                    securityScore = networkStats.securityScore,
                    onClick = { navController?.navigate("security") }
                )
            }
            
            // Data Usage Widget
            item { DataUsageWidget(networkStats) }
            
            // Connected Devices Widget
            item { ConnectedDevicesWidget(networkStats) }
            
            // Quick Actions
            item { QuickActionsWidget(navController, viewModel) }
        }
    }
}

/**
 * Speed Gauge Widget - Main glassmorphic gauge showing network speed
 */
@Composable
private fun SpeedGaugeWidget(
    downloadSpeed: Double,
    uploadSpeed: Double,
    isMonitoring: Boolean
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        withBorder = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Live indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Network Speed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.TextPrimary
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(
                            color = SenetColors.SuccessGlow.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    GlowingDot(isActive = isMonitoring, color = SenetColors.SuccessGlow)
                    Text(
                        "LIVE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenetColors.SuccessGlow,
                        letterSpacing = 1.2.sp
                    )
                }
            }
            
            // Main gauge
            GlowingGauge(
                value = downloadSpeed.toFloat(),
                maxValue = 100f,
                label = "Download Speed",
                unit = "Mbps",
                size = 200.dp
            )
        }
    }
}

/**
 * Security Gauge Widget - Glassmorphic security score display
 */
@Composable
private fun SecurityGaugeWidget(
    securityScore: Int,
    onClick: () -> Unit = {}
) {
    val scoreColor = when {
        securityScore >= 80 -> SenetColors.SuccessGlow
        securityScore >= 60 -> SenetColors.WarningGlow
        else -> SenetColors.ErrorGlow
    }
    
    val scoreStatus = when {
        securityScore >= 80 -> "Secure"
        securityScore >= 60 -> "Moderate"
        else -> "At Risk"
    }
    
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        withBorder = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Security Score",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.TextPrimary
                )
                Icon(
                    Icons.Filled.Security,
                    contentDescription = null,
                    tint = scoreColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            GlowingGauge(
                value = securityScore.toFloat(),
                maxValue = 100f,
                label = scoreStatus,
                unit = "/100",
                size = 180.dp
            )
            
            Text(
                text = "Tap for details",
                fontSize = 12.sp,
                color = SenetColors.TextTertiary
            )
        }
    }
}

@Composable
private fun DashboardHeader() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Network Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Real-time monitoring",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        IconButton(onClick = { 
            Toast.makeText(context, "Settings - Under Development", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = TextPrimary
            )
        }
    }
}

@Composable
private fun SpeedTestWidget(
    isMonitoring: Boolean,
    networkStats: NetworkStats,
    downloadSpeed: Double,
    uploadSpeed: Double,
    ping: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Network Speed",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                // Live indicator with enhanced animation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = VibrантGreen.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = VibrантGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Speed gauge with enhanced animated value
            val animatedDownloadSpeed by animateFloatAsState(
                targetValue = downloadSpeed.toFloat(),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "downloadSpeed"
            )
            
            Box(
                modifier = Modifier
                    .size(170.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { (animatedDownloadSpeed / 100.0).coerceIn(0.0, 1.0).toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = ElectricBlue,
                    strokeWidth = 16.dp,
                    trackColor = CardBackground,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.2f".format(animatedDownloadSpeed),
                        style = MaterialTheme.typography.displayMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mbps",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Upload/Download/Ping row with enhanced live updates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimatedMetricItem(
                    label = "Download",
                    value = downloadSpeed,
                    unit = "Mbps",
                    color = VibrантGreen,
                    icon = Icons.Default.ArrowDownward
                )
                AnimatedMetricItem(
                    label = "Upload",
                    value = uploadSpeed,
                    unit = "Mbps",
                    color = InfoCyan,
                    icon = Icons.Default.ArrowUpward
                )
                AnimatedMetricItem(
                    label = "Ping",
                    value = ping.toDouble(),
                    unit = "ms",
                    color = when {
                        ping < 0 -> Color.Gray
                        ping < 50 -> VibrантGreen
                        ping < 100 -> WarningOrange
                        else -> ThreatRed
                    },
                    icon = Icons.Default.Speed
                )
            }
        }
    }
}

@Composable
private fun AnimatedMetricItem(
    label: String,
    value: Double,
    unit: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "metricValue"
    )
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (value < 1) "%.2f".format(animatedValue) else "%.1f".format(animatedValue),
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun PulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .alpha(alpha)
            .background(VibrантGreen, CircleShape)
    )
}

@Composable
private fun SecurityOverviewWidget(networkStats: NetworkStats) {
    val securityScore = networkStats.securityScore
    val scoreColor = when {
        securityScore >= 80 -> VibrантGreen
        securityScore >= 60 -> WarningOrange
        else -> ThreatRed
    }
    
    val scoreStatus = when {
        securityScore >= 80 -> "Secure"
        securityScore >= 60 -> "Moderate"
        else -> "At Risk"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Security Overview",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    Icons.Filled.Security,
                    contentDescription = null,
                    tint = scoreColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Security Score with color coding and animation
            val animatedScore by animateIntAsState(
                targetValue = securityScore,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "securityScore"
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Security Score",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = scoreStatus,
                        style = MaterialTheme.typography.bodyMedium,
                        color = scoreColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "$animatedScore/100",
                    style = MaterialTheme.typography.displaySmall,
                    color = scoreColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Security metrics with enhanced spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecurityMetric("Threats Blocked", networkStats.threatsBlocked.toString(), ThreatRed)
                SecurityMetric("Active Connections", networkStats.activeConnections.toString(), ElectricBlue)
            }
        }
    }
}

@Composable
private fun SecurityMetric(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun DataUsageWidget(networkStats: NetworkStats) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        withBorder = true
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Data Usage Today",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.TextPrimary
                )
                Icon(
                    Icons.Filled.DataUsage,
                    contentDescription = null,
                    tint = SenetColors.ElectricBlue,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Animated progress bar
            AnimatedProgressBar(
                progress = (networkStats.dataUsedMB / networkStats.dataTotalMB).coerceIn(0f, 1f),
                height = 14.dp
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "%.2f MB used".format(networkStats.dataUsedMB),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SenetColors.TextSecondary
                )
                Text(
                    text = "%.2f MB total".format(networkStats.dataTotalMB),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SenetColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ConnectedDevicesWidget(networkStats: NetworkStats) {
    val context = LocalContext.current
    val animatedCount by animateIntAsState(
        targetValue = networkStats.connectedDevices,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "deviceCount"
    )
    
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        withBorder = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Connected Devices",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenetColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "On your local network",
                    fontSize = 14.sp,
                    color = SenetColors.TextTertiary
                )
            }
            
            // Glowing device count
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SenetColors.NeonBlue.copy(alpha = 0.25f),
                                SenetColors.NeonBlue.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = animatedCount.toString(),
                    fontSize = 48.sp,
                    color = SenetColors.NeonBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickActionsWidget(navController: NavController?, viewModel: DashboardViewModel) {
    val context = LocalContext.current
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        withBorder = true
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SenetColors.TextPrimary
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Filled.Wifi,
                    label = "Scan Network",
                    onClick = { 
                        navController?.navigate("network")
                        viewModel.triggerWiFiScan()
                    }
                )
                QuickActionButton(
                    icon = Icons.Filled.Block,
                    label = "Block App",
                    onClick = { 
                        navController?.navigate("security")
                    }
                )
                QuickActionButton(
                    icon = Icons.Filled.History,
                    label = "View Logs",
                    onClick = { 
                        navController?.navigate("activity")
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            SenetColors.NeonBlue.copy(alpha = 0.2f),
                            SenetColors.NeonBlue.copy(alpha = 0.05f)
                        )
                    ),
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = SenetColors.NeonBlue,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = SenetColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PermissionBanner(
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRequestPermission() }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        SenetColors.WarningGlow.copy(alpha = 0.15f),
                        SenetColors.WarningGlow.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                width = 1.dp,
                color = SenetColors.WarningGlow.copy(alpha = 0.4f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = SenetColors.WarningGlow,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "Permission Required",
                        fontSize = 16.sp,
                        color = SenetColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Grant usage access for accurate data statistics",
                        fontSize = 13.sp,
                        color = SenetColors.TextSecondary
                    )
                }
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Open settings",
                tint = SenetColors.WarningGlow,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Data classes for network statistics
data class NetworkStats(
    val downloadSpeed: Double = 0.0,
    val uploadSpeed: Double = 0.0,
    val ping: Int = 0,
    val threatsBlocked: Int = 0,
    val activeConnections: Int = 0,
    val securityScore: Int = 85,
    val dataUsedMB: Float = 0f,
    val dataTotalMB: Float = 1024f,
    val connectedDevices: Int = 0
)
