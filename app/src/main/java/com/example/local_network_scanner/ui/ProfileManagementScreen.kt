package com.example.local_network_scanner.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.local_network_scanner.R
import com.example.local_network_scanner.data.db.UserProfile
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.ui.components.ProfileFormDialog
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.ProfileUiState
import com.example.local_network_scanner.ui.viewmodel.ProfileViewModel

/**
 * Profile Management Screen
 * Displays all user profiles with create, edit, delete, and switch functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileManagementScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    val context = LocalContext.current
    val profiles by viewModel.profiles.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val profileToDelete by viewModel.profileToDelete.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var profileToEdit by remember { mutableStateOf<UserProfile?>(null) }
    
    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileUiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                showCreateDialog = false
                profileToEdit = null
                viewModel.resetUiState()
            }
            is ProfileUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetUiState()
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Management", color = TextPrimary) },
                navigationIcon = {
                    navController?.let {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDarkGray
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = ElectricBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Profile", tint = TrueBlack)
            }
        },
        containerColor = DeepNavy
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (profiles.isEmpty()) {
                // Empty state
                EmptyProfilesState(onCreateClick = { showCreateDialog = true })
            } else {
                // Profile list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(profiles) { profile ->
                        ProfileCard(
                            profile = profile,
                            isActive = profile.id == activeProfile?.id,
                            onEditClick = { profileToEdit = profile },
                            onDeleteClick = { viewModel.requestDeleteProfile(profile) },
                            onSwitchClick = { viewModel.switchProfile(profile.id) }
                        )
                    }
                }
            }
            
            // Loading overlay
            if (uiState is ProfileUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ElectricBlue)
                }
            }
        }
    }
    
    // Create/Edit Dialog
    if (showCreateDialog || profileToEdit != null) {
        ProfileFormDialog(
            profile = profileToEdit,
            onDismiss = {
                showCreateDialog = false
                profileToEdit = null
            },
            onSave = { name, email, role, avatarUri, autoStartVpn, notifications, dns ->
                if (profileToEdit != null) {
                    viewModel.updateProfile(
                        profileToEdit!!.id, name, email, role,
                        avatarUri, autoStartVpn, notifications, dns
                    )
                } else {
                    viewModel.createProfile(
                        name, email, role, avatarUri,
                        autoStartVpn, notifications, dns
                    )
                }
            }
        )
    }
    
    // Delete Confirmation Dialog
    if (showDeleteDialog && profileToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelDeleteProfile() },
            title = { Text("Delete Profile", color = TextPrimary) },
            text = { 
                Text(
                    "Delete ${profileToDelete!!.name}? This action cannot be undone.",
                    color = TextSecondary
                ) 
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDeleteProfile() }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDeleteProfile() }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceDarkGray
        )
    }
}

/**
 * Profile Card Component
 * Displays individual profile information with actions
 */
@Composable
fun ProfileCard(
    profile: UserProfile,
    isActive: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSwitchClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isActive) { if (!isActive) onSwitchClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) 
                ElectricBlue.copy(alpha = 0.2f)
            else 
                SurfaceDarkGray
        ),
        border = if (isActive) 
            BorderStroke(2.dp, ElectricBlue) 
        else null,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            AsyncImage(
                model = profile.avatarUri ?: R.drawable.ic_default_avatar,
                contentDescription = "Profile avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, if (isActive) ElectricBlue else TextTertiary, CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Profile info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RoleBadge(role = profile.role)
                }
                
                Text(
                    text = profile.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                
                Text(
                    text = "Last active: ${formatRelativeTime(profile.lastActiveAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
            
            // Actions menu
            var expanded by remember { mutableStateOf(false) }
            
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = TextPrimary)
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(SurfaceDarkGray)
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit", color = TextPrimary) },
                        onClick = {
                            expanded = false
                            onEditClick()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = ElectricBlue)
                        }
                    )
                    
                    if (!isActive) {
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                expanded = false
                                onDeleteClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete, 
                                    contentDescription = null, 
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Role Badge Component
 * Displays user role (Admin/User) with appropriate styling
 */
@Composable
fun RoleBadge(role: UserRole) {
    val (text, color) = when (role) {
        UserRole.ADMIN -> "ADMIN" to Color(0xFFD32F2F)
        UserRole.STANDARD -> "USER" to Color(0xFF1E88E5)
    }
    
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Empty State Component
 * Shown when no profiles exist
 */
@Composable
fun EmptyProfilesState(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = TextTertiary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No profiles yet",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Create your first profile to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Profile", color = TrueBlack)
        }
    }
}

/**
 * Format timestamp as relative time
 */
fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} min ago"
        diff < 86400_000 -> "${diff / 3600_000} hours ago"
        else -> "${diff / 86400_000} days ago"
    }
}
