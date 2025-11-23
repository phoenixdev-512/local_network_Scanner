package com.example.local_network_scanner.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.data.model.AppNetworkActivity
import com.example.local_network_scanner.ui.theme.AppSpacing
import com.example.local_network_scanner.ui.viewmodel.ActivityViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ActivityScreen(viewModel: ActivityViewModel = hiltViewModel()) {
    val recentActivity by viewModel.last5MinutesActivity.collectAsState()
    val dataUsageStats by viewModel.dataUsageStats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf(ActivitySortOption.DATA_USAGE) }
    
    // Apply filtering and sorting
    val processedActivity = remember(recentActivity, searchQuery, sortBy) {
        var filtered = recentActivity
        
        // Apply search filter
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.appName.contains(searchQuery, ignoreCase = true) ||
                it.packageName.contains(searchQuery, ignoreCase = true)
            }
        }
        
        // Apply sorting
        when (sortBy) {
            ActivitySortOption.DATA_USAGE -> filtered.sortedByDescending { it.uploadBytes + it.downloadBytes }
            ActivitySortOption.APP_NAME -> filtered.sortedBy { it.appName }
            ActivitySortOption.CONNECTIONS -> filtered.sortedByDescending { it.connectionCount }
        }
    }
    
    Scaffold(
        topBar = {
            GoogleTopAppBar(
                title = "Network Activity",
                actions = {
                    IconButton(onClick = { viewModel.refreshActivity() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) with
                fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.padding(paddingValues)
        ) { loading ->
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Time Range Header
                    TimeRangeHeader()
                    
                    // Search and Filter
                    SearchAndFilterSection(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        sortBy = sortBy,
                        onSortChange = { sortBy = it }
                    )
                    
                    // Data Usage Summary Card
                    DataUsageSummaryCard(
                        totalUpload = dataUsageStats.totalUpload,
                        totalDownload = dataUsageStats.totalDownload,
                        activeAppsCount = dataUsageStats.activeAppsCount
                    )
                    
                    // Per-App Statistics
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(AppSpacing.medium),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.small)
                    ) {
                        items(processedActivity) { appActivity ->
                            AppActivityCard(appActivity)
                        }
                        
                        if (processedActivity.isEmpty()) {
                            item {
                                EmptyActivityCard()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeRangeHeader() {
    GoogleCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.medium),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(AppSpacing.small))
                Text(
                    "Last 5 Minutes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // Live indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                PulsingDot()
                Spacer(modifier = Modifier.width(AppSpacing.small))
                Text(
                    "LIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DataUsageSummaryCard(
    totalUpload: Long,
    totalDownload: Long,
    activeAppsCount: Int
) {
    GoogleCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.medium)
    ) {
        Column(modifier = Modifier.padding(AppSpacing.medium)) {
            Text(
                "Data Usage Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.medium))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataUsageMetric(
                    value = formatBytes(totalDownload),
                    label = "Downloaded",
                    icon = Icons.Default.ArrowDownward,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                DataUsageMetric(
                    value = formatBytes(totalUpload),
                    label = "Uploaded",
                    icon = Icons.Default.ArrowUpward,
                    color = MaterialTheme.colorScheme.primary
                )
                
                DataUsageMetric(
                    value = activeAppsCount.toString(),
                    label = "Active Apps",
                    icon = Icons.Default.Apps,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun DataUsageMetric(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(AppSpacing.small)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(AppSpacing.extraSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AppActivityCard(activity: AppNetworkActivity) {
    GoogleCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App Icon
                if (activity.appIcon != null) {
                    Image(
                        bitmap = activity.appIcon.toBitmap().asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.Apps,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = activity.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${activity.connectionCount} connections",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Upload",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatBytes(activity.uploadBytes),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Download",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatBytes(activity.downloadBytes),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAndFilterSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    sortBy: ActivitySortOption,
    onSortChange: (ActivitySortOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.medium, vertical = AppSpacing.small)
    ) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search apps...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.small))
        
        // Sort chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = sortBy == ActivitySortOption.DATA_USAGE,
                onClick = { onSortChange(ActivitySortOption.DATA_USAGE) },
                label = { Text("Data Usage") }
            )
            FilterChip(
                selected = sortBy == ActivitySortOption.APP_NAME,
                onClick = { onSortChange(ActivitySortOption.APP_NAME) },
                label = { Text("Name") }
            )
            FilterChip(
                selected = sortBy == ActivitySortOption.CONNECTIONS,
                onClick = { onSortChange(ActivitySortOption.CONNECTIONS) },
                label = { Text("Connections") }
            )
        }
    }
}

@Composable
private fun EmptyActivityCard() {
    GoogleCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.SignalCellularOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Activity Found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No apps match your search",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

enum class ActivitySortOption {
    DATA_USAGE, APP_NAME, CONNECTIONS
}
