package com.example.local_network_scanner.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.net.wifi.ScanResult
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.WifiViewModel

/**
 * Network Scanner Screen
 * Comprehensive network scanning with WiFi networks and device discovery
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NetworkScannerScreen(
    navController: androidx.navigation.NavController? = null,
    viewModel: WifiViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentSsid by viewModel.currentSsid.collectAsState()
    val signalStrength by viewModel.signalStrength.collectAsState()
    val localIp by viewModel.localIp.collectAsState()
    val scanResults by viewModel.scanResults.collectAsState()
    val permissionGranted by viewModel.permissionGranted.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    
    var sortBy by remember { mutableStateOf(SortOption.SIGNAL_STRENGTH) }
    var filterType by remember { mutableStateOf(FilterType.ALL) }
    var showPasswordDialog by remember { mutableStateOf<ScanResult?>(null) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startScan()
        }
    }
    
    // Sort and filter networks
    val processedResults = remember(scanResults, sortBy, filterType) {
        var filtered = when (filterType) {
            FilterType.ALL -> scanResults
            FilterType.OPEN -> scanResults.filter { !it.capabilities.contains("WPA") && !it.capabilities.contains("WEP") }
            FilterType.SECURED -> scanResults.filter { it.capabilities.contains("WPA") || it.capabilities.contains("WEP") }
        }
        
        when (sortBy) {
            SortOption.SIGNAL_STRENGTH -> filtered.sortedByDescending { it.level }
            SortOption.NAME -> filtered.sortedBy { it.SSID }
            SortOption.SECURITY -> filtered.sortedBy { it.capabilities }
        }
    }
    
    Scaffold(
        topBar = {
            GoogleTopAppBar(
                title = "Network Scanner",
                actions = {
                    IconButton(
                        onClick = {
                            if (permissionGranted) {
                                viewModel.startScan()
                            } else {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        enabled = !isScanning
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = "Scan"
                            )
                        }
                    }
                },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Current connection card
            item {
                CurrentConnectionCard(
                    ssid = currentSsid,
                    signalStrength = signalStrength,
                    localIp = localIp
                )
            }
            
            if (!permissionGranted) {
                item {
                    PermissionRequiredCard(
                        onGrantPermission = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        onOpenSettings = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            } else {
                // Filter and sort controls
                item {
                    FilterSortControls(
                        sortBy = sortBy,
                        filterType = filterType,
                        onSortChange = { sortBy = it },
                        onFilterChange = { filterType = it }
                    )
                }
                
                // Networks list header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Available Networks (${processedResults.size})",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                // Networks list
                items(processedResults) { network ->
                    NetworkCard(
                        network = network,
                        onClick = {
                            if (network.capabilities.contains("WPA") || network.capabilities.contains("WEP")) {
                                showPasswordDialog = network
                            } else {
                                viewModel.connectToNetwork(network)
                            }
                        }
                    )
                }
                
                if (processedResults.isEmpty() && !isScanning) {
                    item {
                        EmptyNetworksCard()
                    }
                }
            }
        }
    }
    
    // Password dialog
    if (showPasswordDialog != null) {
        var password by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPasswordDialog = null },
            title = {
                Text(
                    text = "Connect to Network",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = showPasswordDialog!!.SSID,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.connectToNetwork(showPasswordDialog!!, password)
                        showPasswordDialog = null
                        password = ""
                    },
                    enabled = password.isNotEmpty()
                ) {
                    Text("Connect")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showPasswordDialog = null
                    password = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CurrentConnectionCard(
    ssid: String,
    signalStrength: Int,
    localIp: String
) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Connection",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                
                Icon(
                    imageVector = getWifiIcon(signalStrength),
                    contentDescription = "Signal",
                    tint = getSignalColor(signalStrength),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "SSID",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ssid,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "IP Address",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = localIp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            if (signalStrength < 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Signal: $signalStrength dBm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FilterSortControls(
    sortBy: SortOption,
    filterType: FilterType,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterType) -> Unit
) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Filter & Sort",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Sort options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = sortBy == SortOption.SIGNAL_STRENGTH,
                    onClick = { onSortChange(SortOption.SIGNAL_STRENGTH) },
                    label = { Text("Signal") },
                    leadingIcon = { Icon(Icons.Default.SignalCellularAlt, null, modifier = Modifier.size(18.dp)) }
                )
                FilterChip(
                    selected = sortBy == SortOption.NAME,
                    onClick = { onSortChange(SortOption.NAME) },
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Default.SortByAlpha, null, modifier = Modifier.size(18.dp)) }
                )
                FilterChip(
                    selected = sortBy == SortOption.SECURITY,
                    onClick = { onSortChange(SortOption.SECURITY) },
                    label = { Text("Security") },
                    leadingIcon = { Icon(Icons.Default.Security, null, modifier = Modifier.size(18.dp)) }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Filter options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterType == FilterType.ALL,
                    onClick = { onFilterChange(FilterType.ALL) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = filterType == FilterType.OPEN,
                    onClick = { onFilterChange(FilterType.OPEN) },
                    label = { Text("Open") }
                )
                FilterChip(
                    selected = filterType == FilterType.SECURED,
                    onClick = { onFilterChange(FilterType.SECURED) },
                    label = { Text("Secured") }
                )
            }
        }
    }
}

@Composable
private fun NetworkCard(
    network: ScanResult,
    onClick: () -> Unit
) {
    val isSecured = network.capabilities.contains("WPA") || network.capabilities.contains("WEP")
    val signalColor = getSignalColor(network.level)
    
    GoogleCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = network.SSID,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSecured) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = if (isSecured) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSecured) "Secured" else "Open",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Text(
                        text = "${network.level} dBm",
                        style = MaterialTheme.typography.bodySmall,
                        color = signalColor
                    )
                }
            }
            
            Icon(
                imageVector = getWifiIcon(network.level),
                contentDescription = "Signal",
                tint = signalColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun PermissionRequiredCard(
    onGrantPermission: () -> Unit,
    onOpenSettings: () -> Unit
) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.LocationOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Location Permission Required",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Location permission is required to scan for WiFi networks",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(
                onClick = onGrantPermission,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Grant Permission", color = MaterialTheme.colorScheme.onError)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = onOpenSettings) {
                Text("Open Settings", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun EmptyNetworksCard() {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.WifiOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Networks Found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Tap the refresh button to scan again",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Helper functions
private fun getWifiIcon(signalStrength: Int) = when {
    signalStrength > -50 -> Icons.Default.Wifi
    signalStrength > -70 -> Icons.Default.Wifi
    signalStrength > -80 -> Icons.Default.Wifi
    else -> Icons.Default.WifiOff
}

private fun getSignalColor(signalStrength: Int): Color = when {
    signalStrength > -50 -> Color(0xFF00C853) // Excellent
    signalStrength > -70 -> Color(0xFF1E88E5) // Good
    signalStrength > -80 -> Color(0xFFFFA726) // Fair
    else -> Color(0xFFD32F2F) // Poor
}

enum class SortOption {
    SIGNAL_STRENGTH, NAME, SECURITY
}

enum class FilterType {
    ALL, OPEN, SECURED
}
