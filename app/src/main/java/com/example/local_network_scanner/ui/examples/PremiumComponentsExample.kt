package com.example.local_network_scanner.ui.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.local_network_scanner.ui.animation.AnimatedContainer
import com.example.local_network_scanner.ui.components.*
import com.example.local_network_scanner.ui.theme.SenetColors

/**
 * Example Screen demonstrating SENET Premium Components
 * 
 * This is a reference implementation showing how to use:
 * - PremiumCard
 * - PremiumButton
 * - PremiumFAB
 * - EmptyStateWithBranding
 * - SenetLogo
 * - Animations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumComponentsExample() {
    var isLoading by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var isConfirmed by remember { mutableStateOf(false) }
    var showEmpty by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Premium Components Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SenetColors.NavyBase,
                    titleContentColor = SenetColors.WhitePure
                )
            )
        },
        floatingActionButton = {
            PremiumFAB(
                icon = Icons.Default.Add,
                onClick = { showBottomSheet = true },
                contentDescription = "Add Item",
                isExpanded = false
            )
        }
    ) { paddingValues ->
        if (showEmpty) {
            // Example: Empty State
            EmptyStateWithBranding(
                title = "No Data Available",
                description = "Get started by adding your first item using the button below",
                actionText = "Add Item",
                onAction = { showEmpty = false }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo Example
                item {
                    PremiumCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SenetLogo(size = 120.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "SENET Logo Example",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                // Premium Button Examples
                item {
                    PremiumCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Premium Buttons",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            
                            PremiumButton(
                                text = "Start Action",
                                onClick = { isLoading = !isLoading },
                                isLoading = isLoading
                            )
                            
                            PremiumButton(
                                text = "Disabled Button",
                                onClick = { },
                                enabled = false
                            )
                            
                            NavyRippleButton(
                                onClick = { isConfirmed = !isConfirmed }
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Navy Ripple Button")
                            }
                        }
                    }
                }
                
                // Morphing Icon Example
                item {
                    PremiumCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Morphing Icon",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Click to toggle",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            IconButton(onClick = { isConfirmed = !isConfirmed }) {
                                MorphingIcon(
                                    sourceIcon = Icons.Default.Favorite,
                                    targetIcon = Icons.Default.FavoriteBorder,
                                    isTransformed = isConfirmed
                                )
                            }
                        }
                    }
                }
                
                // Animated Container Example
                item {
                    AnimatedContainer(
                        content = {
                            PremiumCard {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        "Animated Container",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "This container animates its size changes smoothly",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                if (isConfirmed) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Extra content appears with animation!",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SenetColors.Success
                                    )
                                }
                            }
                        }
                        }
                    )
                }
                
                // Confirmation Ripple Example
                item {
                    PremiumCard {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ConfirmationRipple(isConfirmed = isConfirmed)
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Confirmation Ripple",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    if (isConfirmed) "Active!" else "Inactive",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isConfirmed) SenetColors.Success else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                // Empty State Toggle
                item {
                    PremiumCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Show Empty State",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = showEmpty,
                                onCheckedChange = { showEmpty = it }
                            )
                        }
                    }
                }
            }
        }
        
        // Premium Bottom Sheet Example
        PremiumBottomSheet(
            isVisible = showBottomSheet,
            onDismiss = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Bottom Sheet",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "This is a premium bottom sheet with rounded corners and smooth animations.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                PremiumButton(
                    text = "Close",
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
