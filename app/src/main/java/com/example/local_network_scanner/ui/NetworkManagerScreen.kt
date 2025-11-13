package com.example.local_network_scanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.NetworkManagerViewModel

/**
 * Network Manager screen
 * Manages saved WiFi networks, policies, and analytics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkManagerScreen(
    viewModel: NetworkManagerViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val savedNetworks by viewModel.savedNetworks.collectAsState()
    val policies by viewModel.policies.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Network Manager", color = TextPrimary) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            actions = {
                IconButton(onClick = { /* Add network */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = TextPrimary)
                }
            }
        )
        
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            contentColor = TextPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Saved Networks") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Policies") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Analytics") }
            )
        }
        
        // Content
        when (selectedTab) {
            0 -> SavedNetworksTab(savedNetworks)
            1 -> NetworkPoliciesTab(policies)
            2 -> NetworkAnalyticsTab()
        }
    }
}

@Composable
private fun SavedNetworksTab(networks: List<SavedNetworkDisplay>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(networks) { network ->
            NetworkCard(network)
        }
        
        if (networks.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Wifi,
                    message = "No saved networks"
                )
            }
        }
    }
}

@Composable
private fun NetworkCard(network: SavedNetworkDisplay) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Wifi,
                    contentDescription = null,
                    tint = if (network.isTrusted) VibrантGreen else ElectricBlue,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = network.ssid,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = network.securityType,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    if (network.isTrusted) {
                        Text(
                            text = "Trusted Network",
                            style = MaterialTheme.typography.bodySmall,
                            color = VibrантGreen
                        )
                    }
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%.1f Mbps".format(network.averageSpeed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Avg Speed",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun NetworkPoliciesTab(policies: List<PolicyDisplay>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(policies) { policy ->
            PolicyCard(policy)
        }
        
        if (policies.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Policy,
                    message = "No network policies"
                )
            }
        }
    }
}

@Composable
private fun PolicyCard(policy: PolicyDisplay) {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Policy,
                        contentDescription = null,
                        tint = InfoCyan,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = policy.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = policy.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = { /* Edit policy */ }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (policy.enableAdBlocking) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Ad Blocking", style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = VibrантGreen.copy(alpha = 0.2f),
                            labelColor = VibrантGreen
                        )
                    )
                }
                if (policy.enableMalwareProtection) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Malware Protection", style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = ElectricBlue.copy(alpha = 0.2f),
                            labelColor = ElectricBlue
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkAnalyticsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SpeedHistoryCard() }
        item { NetworkComparisonCard() }
        item { DataUsageHistoryCard() }
    }
}

@Composable
private fun SpeedHistoryCard() {
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
                text = "Speed History",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Placeholder for chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(CardBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Speed chart will be displayed here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun NetworkComparisonCard() {
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
                text = "Network Comparison",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NetworkComparisonItem("Home", 85.0, StatusExcellent)
                NetworkComparisonItem("Work", 65.0, StatusGood)
                NetworkComparisonItem("Public", 35.0, StatusFair)
            }
        }
    }
}

@Composable
private fun NetworkComparisonItem(name: String, speed: Double, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "%.1f".format(speed),
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Mbps",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary
        )
    }
}

@Composable
private fun DataUsageHistoryCard() {
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
                text = "Data Usage History",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Placeholder for chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(CardBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Usage chart will be displayed here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = TextTertiary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = TextTertiary
        )
    }
}

// Display data classes
data class SavedNetworkDisplay(
    val ssid: String,
    val securityType: String,
    val isTrusted: Boolean,
    val averageSpeed: Double
)

data class PolicyDisplay(
    val name: String,
    val description: String,
    val enableAdBlocking: Boolean,
    val enableMalwareProtection: Boolean
)
