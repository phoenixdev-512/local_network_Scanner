package com.example.local_network_scanner.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.local_network_scanner.R
import com.example.local_network_scanner.data.db.UserProfile
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.ui.theme.*

/**
 * Profile Form Dialog
 * Full-screen dialog for creating and editing user profiles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormDialog(
    profile: UserProfile? = null,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        email: String,
        role: UserRole,
        avatarUri: Uri?,
        autoStartVpn: Boolean,
        notificationsEnabled: Boolean,
        customDnsServer: String?
    ) -> Unit
) {
    val isEditMode = profile != null
    
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var email by remember { mutableStateOf(profile?.email ?: "") }
    var selectedRole by remember { mutableStateOf(profile?.role ?: UserRole.STANDARD) }
    var avatarUri by remember { mutableStateOf<Uri?>(profile?.avatarUri?.let { Uri.parse(it) }) }
    var autoStartVpn by remember { mutableStateOf(profile?.autoStartVpn ?: false) }
    var notificationsEnabled by remember { mutableStateOf(profile?.notificationsEnabled ?: true) }
    var customDnsServer by remember { mutableStateOf(profile?.customDnsServer ?: "") }
    var showPreferences by remember { mutableStateOf(false) }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        avatarUri = uri
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            if (isEditMode) "Edit Profile" else "Create Profile",
                            color = TextPrimary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SurfaceDarkGray
                    )
                )
            },
            containerColor = DeepNavy
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Avatar Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Preview
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(3.dp, ElectricBlue, CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") }
                    ) {
                        AsyncImage(
                            model = avatarUri ?: R.drawable.ic_default_avatar,
                            contentDescription = "Profile avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Camera icon overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Change photo",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Photo")
                    }
                }
                
                // Form Fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            if (it.length <= 30) {
                                name = it
                                nameError = when {
                                    it.isBlank() -> "Name is required"
                                    !it.matches(Regex("^[a-zA-Z\\s-]*$")) -> "Only letters, spaces, and hyphens allowed"
                                    else -> null
                                }
                            }
                        },
                        label = { Text("Full Name") },
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (nameError != null) {
                                    Text(nameError!!, color = MaterialTheme.colorScheme.error)
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                Text("${name.length}/30")
                            }
                        },
                        isError = nameError != null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextTertiary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = if (!it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) && it.isNotBlank()) {
                                "Invalid email format"
                            } else if (it.isBlank()) {
                                "Email is required"
                            } else {
                                null
                            }
                        },
                        label = { Text("Email Address") },
                        supportingText = {
                            if (emailError != null) {
                                Text(emailError!!, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        isError = emailError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TextTertiary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    
                    // Role Selection
                    var expanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = if (selectedRole == UserRole.ADMIN) "Administrator" else "Standard User",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("User Role") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = TextTertiary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(SurfaceDarkGray)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Standard User", color = TextPrimary) },
                                onClick = {
                                    selectedRole = UserRole.STANDARD
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Administrator", color = TextPrimary) },
                                onClick = {
                                    selectedRole = UserRole.ADMIN
                                    expanded = false
                                }
                            )
                        }
                    }
                    
                    Text(
                        text = "Administrators have full access to all features",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Profile Preferences (Expandable)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showPreferences = !showPreferences },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Profile Preferences",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    if (showPreferences) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                            
                            if (showPreferences) {
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Auto-start VPN Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Auto-start VPN", color = TextPrimary)
                                        Text(
                                            "Start VPN on profile switch",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Switch(
                                        checked = autoStartVpn,
                                        onCheckedChange = { autoStartVpn = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = ElectricBlue,
                                            checkedTrackColor = ElectricBlue.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Enable Notifications Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Enable Notifications", color = TextPrimary)
                                        Text(
                                            "Receive alerts and updates",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Switch(
                                        checked = notificationsEnabled,
                                        onCheckedChange = { notificationsEnabled = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = ElectricBlue,
                                            checkedTrackColor = ElectricBlue.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Custom DNS Server
                                OutlinedTextField(
                                    value = customDnsServer,
                                    onValueChange = { customDnsServer = it },
                                    label = { Text("Custom DNS Server (Optional)") },
                                    placeholder = { Text("e.g., 8.8.8.8") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ElectricBlue,
                                        unfocusedBorderColor = TextTertiary,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondary
                        )
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            if (nameError == null && emailError == null && name.isNotBlank() && email.isNotBlank()) {
                                onSave(
                                    name,
                                    email,
                                    selectedRole,
                                    avatarUri,
                                    autoStartVpn,
                                    notificationsEnabled,
                                    customDnsServer.takeIf { it.isNotBlank() }
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = nameError == null && emailError == null && name.isNotBlank() && email.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue,
                            contentColor = TrueBlack
                        )
                    ) {
                        Text("Save Profile")
                    }
                }
            }
        }
    }
}
