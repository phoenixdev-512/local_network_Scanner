package com.example.local_network_scanner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.Screen
import com.example.local_network_scanner.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val blockAllByDefault by viewModel.blockAllByDefault.collectAsState()
    val dnsSettings by viewModel.dnsSettings.collectAsState()
    val enableWeeklySummary by viewModel.enableWeeklySummary.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            // General Settings
            Text("General", style = androidx.compose.material3.MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Block all traffic by default", modifier = Modifier.weight(1f))
                Switch(
                    checked = blockAllByDefault,
                    onCheckedChange = { block -> viewModel.setBlockAllByDefault(block) }
                )
            }
            
            HorizontalDivider()

            // DNS Settings
            Text("DNS Settings", style = androidx.compose.material3.MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                 // ... (DNS options code from before)
            }
            if (dnsSettings.dnsMode == "CUSTOM") {
                TextField(
                    value = dnsSettings.customDnsIp,
                    onValueChange = { ip -> viewModel.setCustomDnsIp(ip) },
                    label = { Text("Custom DNS IP") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Secure DNS (DoH)", modifier = Modifier.weight(1f))
                Switch(
                    checked = dnsSettings.enableSecureDns,
                    onCheckedChange = { enable -> viewModel.setEnableSecureDns(enable) }
                )
            }
            
            HorizontalDivider()
            
            // Notifications
            Text("Notifications", style = androidx.compose.material3.MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Weekly Summary", modifier = Modifier.weight(1f))
                Switch(
                    checked = enableWeeklySummary,
                    onCheckedChange = { enable -> viewModel.setEnableWeeklySummary(enable) }
                )
            }
        }
    }
}
