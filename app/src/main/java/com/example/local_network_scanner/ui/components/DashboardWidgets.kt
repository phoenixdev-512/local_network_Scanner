package com.example.local_network_scanner.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.local_network_scanner.data.model.DataUsageStats
import com.example.local_network_scanner.data.model.NetworkSpeed
import com.example.local_network_scanner.data.model.SpeedUnit
import com.example.local_network_scanner.data.model.TimeRange
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.util.FormatUtils

/**
 * Widget showing network speed with unit toggle functionality
 */
@Composable
fun SpeedTestWidget(
    networkSpeed: NetworkSpeed,
    ping: Int,
    speedUnit: SpeedUnit,
    onToggleUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    text = "Network Speed",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                OutlinedButton(
                    onClick = onToggleUnit,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        speedUnit.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.SwapHoriz,
                        contentDescription = "Toggle unit",
                        modifier = Modifier.size(16.dp),
                        tint = TextPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SpeedMetric(
                    value = networkSpeed.getDownloadSpeed(speedUnit),
                    unit = speedUnit.label,
                    label = "Download",
                    icon = Icons.Default.ArrowDownward,
                    color = VibrантGreen
                )
                SpeedMetric(
                    value = networkSpeed.getUploadSpeed(speedUnit),
                    unit = speedUnit.label,
                    label = "Upload",
                    icon = Icons.Default.ArrowUpward,
                    color = InfoCyan
                )
                SpeedMetric(
                    value = ping.toDouble(),
                    unit = "ms",
                    label = "Ping",
                    icon = Icons.Default.Speed,
                    color = when {
                        ping < 0 -> Color.Gray
                        ping < 50 -> VibrантGreen
                        ping < 100 -> WarningOrange
                        else -> ThreatRed
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PulsingDot()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "LIVE • Updates every 0.5s",
                    style = MaterialTheme.typography.labelSmall,
                    color = VibrантGreen
                )
            }
        }
    }
}

@Composable
fun SpeedMetric(
    value: Double,
    unit: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(durationMillis = 400),
        label = "speedValue"
    )
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = String.format("%.2f", animatedValue),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(unit, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
    }
}

@Composable
fun PulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(VibrантGreen.copy(alpha = alpha), shape = CircleShape)
    )
}

/**
 * Widget showing security overview with scan button
 */
@Composable
fun SecurityOverviewWidget(
    securityScore: Int,
    isScanning: Boolean,
    lastScanTime: Long,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scoreColor = when {
        securityScore >= 80 -> VibrантGreen
        securityScore >= 60 -> WarningOrange
        else -> ThreatRed
    }
    
    val scoreLabel = when {
        securityScore >= 80 -> "Excellent"
        securityScore >= 60 -> "Good"
        securityScore >= 40 -> "Fair"
        else -> "Poor"
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Security Overview",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Button(
                    onClick = onScanClick,
                    enabled = !isScanning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricBlue,
                        disabledContainerColor = ElectricBlue.copy(alpha = 0.5f)
                    )
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scanning...")
                    } else {
                        Icon(Icons.Default.Security, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan Now")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Security Score Gauge
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val animatedScore by animateFloatAsState(
                    targetValue = securityScore / 100f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "securityScore"
                )
                
                CircularProgressIndicator(
                    progress = { animatedScore },
                    modifier = Modifier.fillMaxSize(),
                    color = scoreColor,
                    strokeWidth = 12.dp,
                    trackColor = CardBackground
                )
                
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$securityScore",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    Text(
                        scoreLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Last scanned: ${FormatUtils.formatTimestamp(lastScanTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            if (isScanning) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = ElectricBlue,
                    trackColor = CardBackground
                )
            }
        }
    }
}

/**
 * Widget showing data usage with time range selector
 */
@Composable
fun DataUsageWidget(
    dataUsage: DataUsageStats,
    selectedTimeRange: TimeRange,
    onTimeRangeChange: (TimeRange) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Data Usage", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                
                // Time range selector
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(selectedTimeRange.name, color = TextPrimary)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextPrimary)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        TimeRange.values().forEach { range ->
                            DropdownMenuItem(
                                text = { Text(range.name) },
                                onClick = {
                                    onTimeRangeChange(range)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total usage with animation
            val animatedTotal by animateLongAsState(
                targetValue = dataUsage.total,
                animationSpec = tween(durationMillis = 500),
                label = "totalUsage"
            )
            
            Text(
                text = FormatUtils.formatBytes(animatedTotal),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text("Total data used", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // WiFi vs Mobile breakdown
            Row(modifier = Modifier.fillMaxWidth()) {
                DataTypeCard("WiFi", dataUsage.wifi, ElectricBlue, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                DataTypeCard("Mobile", dataUsage.mobile, WarningOrange, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DataTypeCard(label: String, bytes: Long, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = color)
            Text(
                FormatUtils.formatBytes(bytes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

/**
 * Widget showing connected devices count
 */
@Composable
fun ConnectedDevicesWidget(
    devicesCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Devices,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = ElectricBlue
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                val animatedCount by animateIntAsState(
                    targetValue = devicesCount,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "deviceCount"
                )
                Text(
                    text = "$animatedCount",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text("Connected Devices", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}
