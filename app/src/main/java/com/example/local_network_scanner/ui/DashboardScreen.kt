package com.example.local_network_scanner.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.DashboardViewModel

/**
 * Dashboard screen with Speedtest-inspired design
 * Features real-time metrics, network monitoring, and quick actions
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Header
        DashboardHeader()
        
        // Widgets
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Use new SpeedTestWidget with unit toggle
                val speedUnit by viewModel.speedUnit.collectAsState()
                com.example.local_network_scanner.ui.components.SpeedTestWidget(
                    networkSpeed = networkSpeed,
                    ping = ping,
                    speedUnit = speedUnit,
                    onToggleUnit = { viewModel.toggleSpeedUnit() }
                )
            }
            item {
                // Use new SecurityOverviewWidget with scan button
                val securityScore by viewModel.securityScore.collectAsState()
                val isScanning by viewModel.isSecurityScanning.collectAsState()
                val lastScanTime by viewModel.lastSecurityScanTime.collectAsState()
                com.example.local_network_scanner.ui.components.SecurityOverviewWidget(
                    securityScore = securityScore,
                    isScanning = isScanning,
                    lastScanTime = lastScanTime,
                    onScanClick = { viewModel.startSecurityScan() }
                )
            }
            item {
                // Use new DataUsageWidget with time range
                val dataUsage by viewModel.dataUsageStats.collectAsState()
                val timeRange by viewModel.selectedTimeRange.collectAsState()
                com.example.local_network_scanner.ui.components.DataUsageWidget(
                    dataUsage = dataUsage,
                    selectedTimeRange = timeRange,
                    onTimeRangeChange = { viewModel.setTimeRange(it) }
                )
            }
            item {
                // Use new ConnectedDevicesWidget
                val devicesCount by viewModel.connectedDevicesCount.collectAsState()
                com.example.local_network_scanner.ui.components.ConnectedDevicesWidget(
                    devicesCount = devicesCount
                )
            }
            item { QuickActionsWidget(navController, viewModel) }
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
                    text = "Data Usage Today",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    Icons.Filled.DataUsage,
                    contentDescription = null,
                    tint = InfoCyan,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Data usage bar with animation
            val animatedProgress by animateFloatAsState(
                targetValue = (networkStats.dataUsedMB / networkStats.dataTotalMB).coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                label = "dataProgress"
            )
            
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp),
                color = ElectricBlue,
                trackColor = CardBackground,
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "%.2f MB used".format(networkStats.dataUsedMB),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "%.2f MB total".format(networkStats.dataTotalMB),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ConnectedDevicesWidget(networkStats: NetworkStats) {
    val context = LocalContext.current
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
                Column {
                    Text(
                        text = "Connected Devices",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "On your local network",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary
                    )
                }
                
                // Prominent device count with animation
                val animatedCount by animateIntAsState(
                    targetValue = networkStats.connectedDevices,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "deviceCount"
                )
                
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ElectricBlue.copy(alpha = 0.25f),
                                    ElectricBlue.copy(alpha = 0.05f),
                                    androidx.compose.ui.graphics.Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = animatedCount.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tap to view detailed device information",
                style = MaterialTheme.typography.bodyMedium,
                color = TextTertiary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QuickActionsWidget(navController: NavController?, viewModel: DashboardViewModel) {
    val context = LocalContext.current
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
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
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
        modifier = Modifier.width(80.dp)
    ) {
        FilledIconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = CardBackground
            )
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = ElectricBlue
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
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
