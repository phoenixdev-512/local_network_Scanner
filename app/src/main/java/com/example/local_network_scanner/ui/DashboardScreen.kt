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
                SpeedTestWidget(
                    isMonitoring = isMonitoring,
                    networkStats = networkStats,
                    downloadSpeed = networkSpeed.downloadMbps,
                    uploadSpeed = networkSpeed.uploadMbps,
                    ping = ping
                ) 
            }
            item { SecurityOverviewWidget(networkStats) }
            item { DataUsageWidget(networkStats) }
            item { ConnectedDevicesWidget(networkStats) }
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
            .height(320.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
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
                
                // Live indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = VibrантGreen
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Speed gauge with animated value
            val animatedDownloadSpeed by animateFloatAsState(
                targetValue = downloadSpeed.toFloat(),
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "downloadSpeed"
            )
            
            Box(
                modifier = Modifier
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { (animatedDownloadSpeed / 100.0).coerceIn(0.0, 1.0).toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = ElectricBlue,
                    strokeWidth = 14.dp,
                    trackColor = CardBackground,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.2f".format(animatedDownloadSpeed),
                        style = MaterialTheme.typography.displaySmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mbps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Upload/Download/Ping row with live updates
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
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
                    tint = VibrантGreen
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Security metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecurityMetric("Threats Blocked", networkStats.threatsBlocked.toString(), ThreatRed)
                SecurityMetric("Active Connections", networkStats.activeConnections.toString(), ElectricBlue)
                SecurityMetric("Security Score", "${networkStats.securityScore}/100", VibrантGreen)
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
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
                    tint = InfoCyan
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Data usage bar
            LinearProgressIndicator(
                progress = { (networkStats.dataUsedMB / networkStats.dataTotalMB).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = ElectricBlue,
                trackColor = CardBackground,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "%.2f MB used".format(networkStats.dataUsedMB),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "%.2f MB total".format(networkStats.dataTotalMB),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ConnectedDevicesWidget(networkStats: NetworkStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Connected Devices",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = networkStats.connectedDevices.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = ElectricBlue,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Tap to view detailed device information",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}

@Composable
private fun QuickActionsWidget(navController: NavController?, viewModel: DashboardViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
