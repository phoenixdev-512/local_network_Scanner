package com.example.local_network_scanner.ui

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.Screen
import com.example.local_network_scanner.services.RiskLevel
        ) {
            // Security Score Card
            item {
                SecurityScoreCard(securityScore)
            }
            
            // Real-time Status Card
            item {
                RealTimeStatusCard(
                    appsWithNetworkAccess = appsWithNetworkAccess,
                    activeConnections = activeConnections,
                    threatsDetected = threatsDetected
                )
            }
            
            // Profile Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Active Profile",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = activeProfile?.name ?: "No Profile",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { 
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) 
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                profiles.forEach { profile ->
                                    DropdownMenuItem(
                                        text = { Text(text = profile.name) },
                                        onClick = {
                                            mainViewModel.setActiveProfile(profile)
                                            expanded = false
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = { Text("Add new profile...") },
                                    onClick = { 
                                        Toast.makeText(context, "Add Profile - Under Development", Toast.LENGTH_SHORT).show()
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Deep Scan Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isVpnActive) "VPN Active - Protected" else "VPN Inactive",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isVpnActive) VibrантGreen else TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Scanning progress indicator
                        AnimatedVisibility(visible = isScanning) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Deep Scanning Device...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { scanProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    color = ElectricBlue,
                                    trackColor = CardBackground,
                                )
                                Text(
                                    text = "${(scanProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Main action button
                        Button(
                            onClick = {
                                if (!isScanning) {
                                    securityViewModel.performDeepScan()
                                }
                            },
                            modifier = Modifier.size(180.dp),
                            enabled = !isScanning,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricBlue,
                                disabledContainerColor = CardBackground
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Deep Scan",
                                    modifier = Modifier.size(70.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = if (isScanning) "SCANNING" else "DEEP SCAN",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Scan for suspicious apps and security threats",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                    }
                }
            }
            
            // Suspicious Apps Results
            if (scanComplete && suspiciousApps.isNotEmpty()) {
                item {
                    Text(
                        text = "Suspicious Apps Detected (${suspiciousApps.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = ThreatRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(suspiciousApps) { app ->
                    SuspiciousAppCard(app)
                }
            } else if (scanComplete && suspiciousApps.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = VibrантGreen,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Suspicious Apps Found",
                                style = MaterialTheme.typography.titleMedium,
                                color = VibrантGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Your device appears to be secure",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SecurityScoreCard(score: Int) {
    val scoreColor = when {
        score >= 80 -> VibrантGreen
        score >= 60 -> WarningOrange
        else -> ThreatRed
    }
    
    val scoreText = when {
        score >= 80 -> "Secure"
        score >= 60 -> "Moderate"
        else -> "At Risk"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Security Score",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = scoreColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = scoreText,
                style = MaterialTheme.typography.titleMedium,
                color = scoreColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = scoreColor,
                trackColor = CardBackground,
            )
        }
    }
}

@Composable
private fun RealTimeStatusCard(
    appsWithNetworkAccess: Int,
    activeConnections: Int,
    threatsDetected: Int
) {
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
                text = "Real-Time Status",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusMetric(
                    value = appsWithNetworkAccess.toString(),
                    label = "Apps with\nNetwork Access",
                    color = InfoCyan
                )
                StatusMetric(
                    value = activeConnections.toString(),
                    label = "Active\nConnections",
                    color = ElectricBlue
                )
                StatusMetric(
                    value = threatsDetected.toString(),
                    label = "Threats\nDetected",
                    color = if (threatsDetected > 0) ThreatRed else VibrантGreen
                )
            }
        }
    }
}

@Composable
private fun StatusMetric(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun SuspiciousAppCard(app: com.example.local_network_scanner.services.SuspiciousApp) {
    val riskColor = when (app.riskLevel) {
        RiskLevel.HIGH -> ThreatRed
        RiskLevel.MEDIUM -> WarningOrange
        RiskLevel.LOW -> InfoCyan
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
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
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = riskColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Risk Level: ${app.riskLevel}",
                style = MaterialTheme.typography.bodyMedium,
                color = riskColor,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            app.reasons.forEach { reason ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("• ", color = TextSecondary)
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val context = LocalContext.current
                IconButton(onClick = { 
                    Toast.makeText(context, "View app details", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = ElectricBlue)
                }
                IconButton(onClick = { 
                    Toast.makeText(context, "Uninstall functionality requires system permissions", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Uninstall", tint = ThreatRed)
                }
            }
        }
    }
}