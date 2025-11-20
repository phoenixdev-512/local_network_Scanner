package com.example.local_network_scanner.ui

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.data.db.NetworkPolicy
import com.example.local_network_scanner.data.db.SavedNetwork
import com.example.local_network_scanner.data.repository.TimeRange
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.NetworkManagerViewModel
import com.example.local_network_scanner.ui.viewmodel.NetworkManagerUiState
import kotlinx.serialization.json.Json

/**
 * Network Manager screen
 * Manages saved WiFi networks, policies, and analytics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkManagerScreen(
    viewModel: NetworkManagerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val savedNetworks by viewModel.savedNetworks.collectAsState()
    val policies by viewModel.policies.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    // Handle UI state messages
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is NetworkManagerUiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetUiState()
            }
            is NetworkManagerUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetUiState()
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Network Manager", color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    if (selectedTab == 0) {
                        IconButton(onClick = { viewModel.scanNetworks() }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Scan", tint = TextPrimary)
                        }
                    } else if (selectedTab == 1) {
                        IconButton(onClick = { viewModel.showPolicyDialog() }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Policy", tint = TextPrimary)
                        }
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                    )
                )
                .padding(padding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
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
                0 -> SavedNetworksTab(savedNetworks, viewModel)
                1 -> NetworkPoliciesTab(policies, viewModel)
                2 -> NetworkAnalyticsTab(viewModel)
            }
        }
    }
}

@Composable
private fun SavedNetworksTab(
    networks: List<SavedNetwork>,
    viewModel: NetworkManagerViewModel
) {
    val isScanning by viewModel.isScanning.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isScanning) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ElectricBlue)
                    }
                }
            }
            
            items(networks) { network ->
                NetworkCard(
                    network = network,
                    onEditClick = { viewModel.showEditDialog(network) },
                    onForgetClick = { viewModel.forgetNetwork(network.ssid) },
                    onConnectClick = { viewModel.connectToNetwork(network.ssid) }
                )
            }
            
            if (networks.isEmpty() && !isScanning) {
                item {
                    EmptyState(
                        icon = Icons.Filled.Wifi,
                        message = "No saved networks"
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkCard(
    network: SavedNetwork,
    onEditClick: () -> Unit,
    onForgetClick: () -> Unit,
    onConnectClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: SSID + Signal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = network.ssid,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (network.isTrusted) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Verified,
                                "Trusted",
                                tint = VibrантGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        text = network.securityType.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                SignalStrengthIndicator(network.averageSignalStrength)
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Data Used",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                    Text(
                        formatBytes(network.totalDataUsed),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Column {
                    Text(
                        "Last Connected",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                    Text(
                        network.lastConnectedAt?.let { formatRelativeTime(it) } ?: "Never",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onConnectClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ElectricBlue
                    )
                ) {
                    Icon(Icons.Default.Wifi, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Connect")
                }
                OutlinedButton(
                    onClick = onEditClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary
                    )
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                }
                OutlinedButton(
                    onClick = onForgetClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = WarningOrange
                    )
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun SignalStrengthIndicator(strength: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            val barHeight = 4.dp * (index + 1)
            val isActive = (strength / 20) > index
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(barHeight)
                    .background(
                        color = if (isActive) {
                            when {
                                strength >= 80 -> VibrантGreen
                                strength >= 60 -> StatusGood
                                strength >= 40 -> WarningOrange
                                else -> DangerRed
                            }
                        } else Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}

@Composable
private fun NetworkPoliciesTab(
    policies: List<NetworkPolicy>,
    viewModel: NetworkManagerViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(policies) { policy ->
            PolicyCard(
                policy = policy,
                onEditClick = { viewModel.showPolicyDialog(policy) },
                onDeleteClick = { viewModel.deletePolicy(policy.id) },
                onToggleEnabled = { isActive -> viewModel.updatePolicyStatus(policy.id, isActive) }
            )
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
private fun PolicyCard(
    policy: NetworkPolicy,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit
) {
    var isEnabled by remember { mutableStateOf(policy.isActive) }
    
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            policy.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (policy.isDefault) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = ElectricBlue.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    "DEFAULT",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricBlue
                                )
                            }
                        }
                    }
                    Text(
                        policy.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = {
                        isEnabled = it
                        onToggleEnabled(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = VibrантGreen,
                        checkedTrackColor = VibrантGreen.copy(alpha = 0.5f)
                    )
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Stats
            val allowedApps = try {
                Json.decodeFromString<List<String>>(policy.allowedAppsJson)
            } catch (e: Exception) {
                emptyList()
            }
            val blockedDomains = try {
                Json.decodeFromString<List<String>>(policy.blockedDomainsJson)
            } catch (e: Exception) {
                emptyList()
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PolicyStat(Icons.Default.Apps, "Allowed Apps", allowedApps.size.toString())
                PolicyStat(Icons.Default.Block, "Blocked Domains", blockedDomains.size.toString())
                PolicyStat(Icons.Default.Dns, "DNS", policy.dnsProvider.name)
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Feature chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (policy.enableAdBlocking) {
                    FeatureChip("Ad Blocking", VibrантGreen)
                }
                if (policy.enableMalwareProtection) {
                    FeatureChip("Malware Protection", DangerRed)
                }
                if (policy.enableTrackerBlocking) {
                    FeatureChip("Tracker Blocking", InfoCyan)
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ElectricBlue
                    )
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
                if (!policy.isDefault) {
                    OutlinedButton(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DangerRed
                        )
                    ) {
                        Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicyStat(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }
}

@Composable
private fun FeatureChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun NetworkAnalyticsTab(viewModel: NetworkManagerViewModel) {
    val analyticsData by viewModel.analyticsData.collectAsState()
    val currentNetwork by viewModel.currentNetwork.collectAsState()
    val selectedTimeRange by viewModel.selectedTimeRange.collectAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Time range selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeRange.entries.forEach { range ->
                    FilterChip(
                        selected = selectedTimeRange == range,
                        onClick = { viewModel.setTimeRange(range) },
                        label = { Text(range.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue.copy(alpha = 0.3f),
                            selectedLabelColor = ElectricBlue,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }
        
        // Current network card
        item {
            currentNetwork?.let { network ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Current Network: ${network.ssid}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatColumn("Speed", "${network.currentSpeed} Mbps")
                            StatColumn("Connections", network.activeConnections.toString())
                            StatColumn("Threats", network.blockedThreats.toString())
                        }
                    }
                }
            }
        }
        
        item { SpeedHistoryCard(analyticsData) }
        item { NetworkComparisonCard() }
        item { DataUsageHistoryCard(analyticsData) }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary
        )
    }
}

@Composable
private fun SpeedHistoryCard(analyticsData: List<com.example.local_network_scanner.data.db.NetworkAnalytics>) {
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
                if (analyticsData.isEmpty()) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary
                    )
                } else {
                    // Show summary stats for now
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Average Download: %.1f Mbps".format(
                                analyticsData.map { it.downloadSpeed }.average()
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Average Upload: %.1f Mbps".format(
                                analyticsData.map { it.uploadSpeed }.average()
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
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
private fun DataUsageHistoryCard(analyticsData: List<com.example.local_network_scanner.data.db.NetworkAnalytics>) {
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
                if (analyticsData.isEmpty()) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary
                    )
                } else {
                    Text(
                        text = "Total: ${formatBytes(analyticsData.sumOf { it.dataUsed })}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
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
