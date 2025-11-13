package com.example.local_network_scanner.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.ui.viewmodel.WifiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiScreen(viewModel: WifiViewModel = hiltViewModel()) {
    val currentSsid by viewModel.currentSsid.collectAsState()
    val signalStrength by viewModel.signalStrength.collectAsState()
    val localIp by viewModel.localIp.collectAsState()
    val scanResults by viewModel.scanResults.collectAsState()
    val permissionGranted by viewModel.permissionGranted.collectAsState()
    var showPasswordDialog by remember { mutableStateOf<android.net.wifi.ScanResult?>(null) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startScan()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wi-Fi") },
                actions = {
                    IconButton(onClick = { viewModel.startScan() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Scan")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Current Connection", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    Row {
                        Text("SSID: $currentSsid")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (signalStrength > -70) Icons.Default.Wifi else Icons.Default.WifiOff,
                            contentDescription = "Signal Strength"
                        )
                    }
                    Text("IP Address: $localIp")
                }
            }

            if (permissionGranted) {
                Button(onClick = { viewModel.startScan() }) {
                    Text("Scan for Networks")
                }

                LazyColumn {
                    items(scanResults) { result ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clickable { 
                                    if (result.capabilities.contains("WPA") || result.capabilities.contains("WEP")) {
                                        showPasswordDialog = result
                                    } else {
                                        viewModel.connectToNetwork(result)
                                    }
                                }
                        ) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(result.SSID)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("${result.level} dBm")
                            }
                        }
                    }
                }
            } else {
                Text("Location permission is required to scan for Wi-Fi networks.")
                Button(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                    Text("Grant Permission")
                }
                Button(onClick = { 
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            }

            if (showPasswordDialog != null) {
                var password by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { showPasswordDialog = null },
                    title = { Text("Enter Password") },
                    text = { TextField(value = password, onValueChange = { password = it }) },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.connectToNetwork(showPasswordDialog!!, password)
                                showPasswordDialog = null
                            }
                        ) {
                            Text("Connect")
                        }
                    }
                )
            }

            Button(onClick = { context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }) {
                Text("Manage Saved Networks")
            }
        }
    }
}
