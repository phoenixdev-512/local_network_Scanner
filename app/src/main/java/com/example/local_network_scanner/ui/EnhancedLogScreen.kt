package com.example.local_network_scanner.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.LogViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Enhanced Log Screen
 * Shows recent hour connection logs with filtering and searching
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EnhancedLogScreen(
    viewModel: LogViewModel = hiltViewModel()
) {
    val logs by viewModel.logs.collectAsState()
    val filteredLogs by viewModel.filteredLogs.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(LogFilter.ALL) }
    var selectedTimeRange by remember { mutableStateOf(TimeRange.LAST_HOUR) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    
    // Apply filters
    LaunchedEffect(searchQuery, selectedFilter, selectedTimeRange) {
        viewModel.applyFilters(searchQuery, selectedFilter, selectedTimeRange)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Header
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Connection Logs",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredLogs.size} logs",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            actions = {
                IconButton(onClick = { viewModel.refreshLogs() }) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Refresh",
                        tint = TextPrimary
                    )
                }
                IconButton(onClick = { showClearDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Clear",
                        tint = TextPrimary
                    )
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search bar
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClearQuery = { searchQuery = "" }
                )
            }
            
            // Filter chips
            item {
                FilterChipsRow(
                    selectedFilter = selectedFilter,
                    selectedTimeRange = selectedTimeRange,
                    onFilterChange = { selectedFilter = it },
                    onTimeRangeChange = { selectedTimeRange = it }
                )
            }
            
            // Statistics card
            item {
                LogStatisticsCard(
                    total = filteredLogs.size,
                    allowed = filteredLogs.count { it.status == "ALLOWED" },
                    blocked = filteredLogs.count { it.status == "BLOCKED" },
                    unencrypted = filteredLogs.count { it.isUnencrypted }
                )
            }
            
            // Logs list
            if (filteredLogs.isEmpty()) {
                item {
                    EmptyLogsCard()
                }
            } else {
                items(filteredLogs) { log ->
                    LogItemCard(log)
                }
            }
        }
    }
    
    // Clear dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Logs?") },
            text = { Text("This will permanently delete all connection logs. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearLogs()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThreatRed)
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by app name, IP, or port...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearQuery) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = ElectricBlue,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: LogFilter,
    selectedTimeRange: TimeRange,
    onFilterChange: (LogFilter) -> Unit,
    onTimeRangeChange: (TimeRange) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Filter",
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Status filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == LogFilter.ALL,
                    onClick = { onFilterChange(LogFilter.ALL) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == LogFilter.ALLOWED,
                    onClick = { onFilterChange(LogFilter.ALLOWED) },
                    label = { Text("Allowed") },
                    leadingIcon = { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp)) }
                )
                FilterChip(
                    selected = selectedFilter == LogFilter.BLOCKED,
                    onClick = { onFilterChange(LogFilter.BLOCKED) },
                    label = { Text("Blocked") },
                    leadingIcon = { Icon(Icons.Default.Block, null, modifier = Modifier.size(18.dp)) }
                )
                FilterChip(
                    selected = selectedFilter == LogFilter.UNENCRYPTED,
                    onClick = { onFilterChange(LogFilter.UNENCRYPTED) },
                    label = { Text("Unencrypted") },
                    leadingIcon = { Icon(Icons.Default.Warning, null, modifier = Modifier.size(18.dp)) }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Time range filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTimeRange == TimeRange.LAST_HOUR,
                    onClick = { onTimeRangeChange(TimeRange.LAST_HOUR) },
                    label = { Text("1 Hour") }
                )
                FilterChip(
                    selected = selectedTimeRange == TimeRange.LAST_6_HOURS,
                    onClick = { onTimeRangeChange(TimeRange.LAST_6_HOURS) },
                    label = { Text("6 Hours") }
                )
                FilterChip(
                    selected = selectedTimeRange == TimeRange.LAST_24_HOURS,
                    onClick = { onTimeRangeChange(TimeRange.LAST_24_HOURS) },
                    label = { Text("24 Hours") }
                )
            }
        }
    }
}

@Composable
private fun LogStatisticsCard(
    total: Int,
    allowed: Int,
    blocked: Int,
    unencrypted: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = total.toString(),
                    label = "Total",
                    color = InfoCyan,
                    icon = Icons.Default.List
                )
                StatisticItem(
                    value = allowed.toString(),
                    label = "Allowed",
                    color = VibrантGreen,
                    icon = Icons.Default.CheckCircle
                )
                StatisticItem(
                    value = blocked.toString(),
                    label = "Blocked",
                    color = ThreatRed,
                    icon = Icons.Default.Block
                )
                StatisticItem(
                    value = unencrypted.toString(),
                    label = "Unencrypted",
                    color = WarningOrange,
                    icon = Icons.Default.Warning
                )
            }
        }
    }
}

@Composable
private fun StatisticItem(
    value: String,
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun LogItemCard(log: com.example.local_network_scanner.data.model.ConnectionLog) {
    val statusColor = when (log.status) {
        "ALLOWED" -> VibrантGreen
        "BLOCKED" -> ThreatRed
        else -> WarningOrange
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(statusColor, shape = RoundedCornerShape(4.dp))
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = log.appName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        if (log.isUnencrypted) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Unencrypted",
                                tint = WarningOrange,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${log.destinationIp}:${log.destinationPort}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    
                    Text(
                        text = formatTimestamp(log.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
            }
            
            // Status badge
            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = log.status,
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EmptyLogsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.HistoryToggleOff,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Logs Found",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Connection logs will appear here",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Helper functions
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Enums
enum class LogFilter {
    ALL, ALLOWED, BLOCKED, UNENCRYPTED
}

enum class TimeRange(val hours: Int) {
    LAST_HOUR(1),
    LAST_6_HOURS(6),
    LAST_24_HOURS(24)
}
