package com.example.local_network_scanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.ProfileViewModel

/**
 * Profile screen with role-based access control
 * Shows user information, preferences, and settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentProfile by viewModel.currentProfile.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Profile", color = TextPrimary) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            actions = {
                IconButton(onClick = { /* Edit profile */ }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = TextPrimary)
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProfileHeader(currentProfile?.name ?: "User", currentProfile?.email ?: "", currentProfile?.role ?: UserRole.STANDARD) }
            item { UserStatisticsRow() }
            item { AccountSection() }
            item { SecuritySection() }
            item { NotificationSection() }
            item { AppearanceSection() }
            
            // Admin-only section
            if (currentProfile?.role == UserRole.ADMIN) {
                item { AdminToolsSection() }
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String, role: UserRole) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(ElectricBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(48.dp),
                    tint = TextPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Role badge
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = role.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingIcon = {
                    Icon(
                        if (role == UserRole.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (role == UserRole.ADMIN) VibrантGreen else InfoCyan,
                    labelColor = TrueBlack,
                    leadingIconContentColor = TrueBlack
                )
            )
        }
    }
}

@Composable
private fun UserStatisticsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard("Networks", "12", modifier = Modifier.weight(1f))
        StatCard("Data Saved", "2.4 GB", modifier = Modifier.weight(1f))
        StatCard("Threats", "847", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun AccountSection() {
    SettingsSection(
        title = "Account",
        items = listOf(
            ProfileSettingItem("Personal Information", Icons.Filled.Person),
            ProfileSettingItem("Change Password", Icons.Filled.Lock),
            ProfileSettingItem("Email Preferences", Icons.Filled.Email)
        )
    )
}

@Composable
private fun SecuritySection() {
    SettingsSection(
        title = "Security & Privacy",
        items = listOf(
            ProfileSettingItem("VPN Auto-Start", Icons.Filled.VpnKey),
            ProfileSettingItem("Default Blocking", Icons.Filled.Block),
            ProfileSettingItem("Privacy Mode", Icons.Filled.PrivacyTip)
        )
    )
}

@Composable
private fun NotificationSection() {
    SettingsSection(
        title = "Notifications",
        items = listOf(
            ProfileSettingItem("Threat Alerts", Icons.Filled.Notifications),
            ProfileSettingItem("Connection Logs", Icons.Filled.NotificationsActive),
            ProfileSettingItem("Speed Test Reminders", Icons.Filled.Speed)
        )
    )
}

@Composable
private fun AppearanceSection() {
    SettingsSection(
        title = "Appearance",
        items = listOf(
            ProfileSettingItem("Theme Selection", Icons.Filled.Palette),
            ProfileSettingItem("Color Accent", Icons.Filled.ColorLens),
            ProfileSettingItem("Font Size", Icons.Filled.FormatSize)
        )
    )
}

@Composable
private fun AdminToolsSection() {
    SettingsSection(
        title = "Admin Tools",
        items = listOf(
            ProfileSettingItem("User Management", Icons.Filled.SupervisorAccount),
            ProfileSettingItem("Global Policies", Icons.Filled.Policy),
            ProfileSettingItem("Audit Logs", Icons.Filled.Assessment),
            ProfileSettingItem("System Diagnostics", Icons.Filled.BugReport)
        )
    )
}

@Composable
private fun SettingsSection(title: String, items: List<ProfileSettingItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            items.forEach { item ->
                SettingItemRow(item)
                if (item != items.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CardBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingItemRow(item: ProfileSettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = TextSecondary
            )
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

private data class ProfileSettingItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
