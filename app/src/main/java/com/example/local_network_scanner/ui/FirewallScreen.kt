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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.Screen
import com.example.local_network_scanner.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirewallScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isVpnActive by viewModel.isVpnActive.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val profiles by viewModel.profiles.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    
    // VPN permission launcher
    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startVpn()
        } else {
            Toast.makeText(context, "VPN permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A1931), Color.Black)
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                TextField(
                    value = activeProfile?.name ?: "No Profile",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    profiles.forEach { profile ->
                        DropdownMenuItem(
                            text = { Text(text = profile.name) },
                            onClick = {
                                viewModel.setActiveProfile(profile)
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isVpnActive) "PROTECTING: Your Network" else "INACTIVE",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isVpnActive) Color(0xFF00C853) else Color.Gray
                )
                
                // Scanning progress indicator
                AnimatedVisibility(visible = isScanning) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Scanning Network...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { scanProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(4.dp),
                            color = Color(0xFF00C853)
                        )
                        Text(
                            text = "${(scanProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(if (isScanning) 32.dp else 128.dp))
                
                Button(
                    onClick = {
                        if (isVpnActive) {
                            viewModel.stopVpn()
                        } else {
                            // Request VPN permission
                            val intent = VpnService.prepare(context)
                            if (intent != null) {
                                vpnPermissionLauncher.launch(intent)
                            } else {
                                // Permission already granted
                                viewModel.startVpn()
                            }
                        }
                    },
                    modifier = Modifier.size(200.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVpnActive) Color.Red else Color(0xFF00C853)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isVpnActive) Icons.Default.Security else Icons.Default.PowerSettingsNew,
                            contentDescription = if (isVpnActive) "Stop VPN" else "Start VPN",
                            modifier = Modifier.size(80.dp),
                            tint = Color.White
                        )
                        Text(
                            text = if (isVpnActive) "STOP" else "START",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "5", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    Text(text = "Apps", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "1,482", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    Text(text = "Connections", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "87", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    Text(text = "Threats", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}