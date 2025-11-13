package com.example.local_network_scanner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.ui.viewmodel.AppListViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.text.CharacterIterator
import java.text.StringCharacterIterator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(viewModel: AppListViewModel = hiltViewModel()) {
    val apps by viewModel.apps.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("App Rules") })
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(apps) { app ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberDrawablePainter(drawable = app.icon),
                            contentDescription = app.appName,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.appName)
                            Text(text = app.packageName, style = MaterialTheme.typography.bodySmall)
                            Text(text = "Data Today: ${formatBytes(app.dataUsage)}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Button(
                                onClick = {
                                    viewModel.setRule(app.packageName, false)
                                    if (app.isBypassed) viewModel.toggleBypass(app.packageName)
                                },
                                colors = if (!app.isBypassed && !app.isBlocked) {
                                    ButtonDefaults.buttonColors()
                                } else {
                                    ButtonDefaults.outlinedButtonColors()
                                }
                            ) {
                                Text("Allow")
                            }
                            Button(
                                onClick = {
                                    viewModel.setRule(app.packageName, true)
                                    if (app.isBypassed) viewModel.toggleBypass(app.packageName)
                                },
                                colors = if (!app.isBypassed && app.isBlocked) {
                                    ButtonDefaults.buttonColors()
                                } else {
                                    ButtonDefaults.outlinedButtonColors()
                                }
                            ) {
                                Text("Block")
                            }
                            Button(
                                onClick = { viewModel.toggleBypass(app.packageName) },
                                colors = if (app.isBypassed) {
                                    ButtonDefaults.buttonColors()
                                } else {
                                    ButtonDefaults.outlinedButtonColors()
                                }
                            ) {
                                Text("Bypass")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatBytes(bytes: Long): String {
    var b = bytes
    if (-1000 < b && b < 1000) {
        return "$b B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (b <= -999950 || b >= 999950) {
        b /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", b / 1000.0, ci.current())
}