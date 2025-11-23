package com.example.local_network_scanner.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.local_network_scanner.services.RiskLevel
import com.example.local_network_scanner.services.SuspiciousApp
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.components.*
import com.example.local_network_scanner.ui.viewmodel.SecurityViewModel

/**
 * Neo-Glassmorphism Security Screen
 * Premium security dashboard with glowing gauges and animated threat detection
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val scanComplete by viewModel.scanComplete.collectAsState()
    val suspiciousApps by viewModel.suspiciousApps.collectAsState()
    val securityScore by viewModel.securityScore.collectAsState()
    val threatsDetected by viewModel.threatsDetected.collectAsState()
    val appsWithNetworkAccess by viewModel.appsWithNetworkAccess.collectAsState()
    val activeConnections by viewModel.activeConnections.collectAsState()
    
    Scaffold(
        topBar = {
            GoogleTopAppBar(
                title = "Security Scan",
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Security Score Gauge
            item {
                SecurityScoreGauge(securityScore)
            }
            
            // Security metrics card
            item {
                SecurityMetricsCard(
                    appsWithNetworkAccess = appsWithNetworkAccess,
                    activeConnections = activeConnections,
                    threatsDetected = threatsDetected
                )
            }
            
            // Scan button
            item {
                SecurityScanButton(
                    isScanning = isScanning,
                    scanProgress = scanProgress,
                    scanComplete = scanComplete,
                    onScanClick = { 
                        if (!isScanning) {
                            viewModel.performDeepScan()
                        }
                    },
                    onResetClick = { viewModel.resetScan() }
                )
            }
            
            // Suspicious apps list
            if (suspiciousApps.isNotEmpty()) {
                item {
                    Text(
                        text = "Suspicious Apps Detected (${suspiciousApps.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(suspiciousApps) { app ->
                    SuspiciousAppCard(
                        app = app,
                        onUninstall = {
                            val intent = Intent(Intent.ACTION_DELETE).apply {
                                data = Uri.parse("package:${app.packageName}")
                            }
                            context.startActivity(intent)
                        },
                        onViewDetails = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${app.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            } else if (scanComplete) {
                item {
                    NoThreatsCard()
                }
            }
        }
    }
}

/**
 * Security Score Gauge - Large glowing security score display
 */
@Composable
private fun SecurityScoreGauge(securityScore: Int) {
    val scoreStatus = when {
        securityScore >= 80 -> "Secure"
        securityScore >= 60 -> "Moderate"
        else -> "At Risk"
    }
    
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Security Status",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            
            // Reusing GlowingGauge but might need to check if it needs updates or if we should replace it with a standard progress indicator
            // For now, let's assume GlowingGauge is compatible or we can replace it with a CircularProgressIndicator
            
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                CircularProgressIndicator(
                    progress = { securityScore / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = when {
                        securityScore >= 80 -> MaterialTheme.colorScheme.primary
                        securityScore >= 60 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                    strokeWidth = 12.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$securityScore",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/100",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = scoreStatus,
                        style = MaterialTheme.typography.titleMedium,
                        color = when {
                            securityScore >= 80 -> MaterialTheme.colorScheme.primary
                            securityScore >= 60 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.error
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SecurityHeader(securityScore: Int) {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Security Status",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Animated security score
            val animatedScore by animateIntAsState(
                targetValue = securityScore,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "securityScore"
            )
            
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedScore / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = scoreColor,
                    strokeWidth = 16.dp,
                    trackColor = CardBackground,
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$animatedScore",
                        style = MaterialTheme.typography.displayLarge,
                        color = scoreColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = scoreStatus,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun SecurityMetricsCard(
    appsWithNetworkAccess: Int,
    activeConnections: Int,
    threatsDetected: Int
) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Real-Time Device Status",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SecurityMetricItem(
                    value = appsWithNetworkAccess.toString(),
                    label = "Apps with\nNetwork Access",
                    icon = Icons.Default.Apps,
                    color = MaterialTheme.colorScheme.primary
                )
                
                SecurityMetricItem(
                    value = activeConnections.toString(),
                    label = "Active\nConnections",
                    icon = Icons.Default.Cloud,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                SecurityMetricItem(
                    value = threatsDetected.toString(),
                    label = "Detected\nThreats",
                    icon = Icons.Default.Warning,
                    color = if (threatsDetected > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SecurityMetricItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun SecurityScanButton(
    isScanning: Boolean,
    scanProgress: Float,
    scanComplete: Boolean,
    onScanClick: () -> Unit,
    onResetClick: () -> Unit
) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isScanning) {
                Text(
                    text = "Scanning Device...",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                
                LinearProgressIndicator(
                    progress = { scanProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                
                Text(
                    text = "${(scanProgress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (scanComplete) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                
                Text(
                    text = "Scan Complete!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                
                GoogleStyleButton(
                    text = "Scan Again",
                    onClick = onResetClick,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                
                Text(
                    text = "Full Security Scan",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Scan all installed apps for suspicious behavior",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                GoogleStyleButton(
                    text = "START SECURITY SCAN",
                    onClick = onScanClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SuspiciousAppCard(
    app: SuspiciousApp,
    onUninstall: () -> Unit,
    onViewDetails: () -> Unit
) {
    val riskColor = when (app.riskLevel) {
        RiskLevel.HIGH -> MaterialTheme.colorScheme.error
        RiskLevel.MEDIUM -> MaterialTheme.colorScheme.errorContainer
        RiskLevel.LOW -> MaterialTheme.colorScheme.tertiary
    }
    
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Risk badge
                Box(
                    modifier = Modifier
                        .background(riskColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = app.riskLevel.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = riskColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Reasons
            app.reasons.forEach { reason ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = riskColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Details")
                }
                
                Button(
                    onClick = onUninstall,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Uninstall")
                }
            }
        }
    }
}

@Composable
private fun NoThreatsCard() {
    GoogleCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Threats Detected",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Your device is secure. All apps passed the security scan.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
