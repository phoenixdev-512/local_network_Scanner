package com.example.local_network_scanner.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.local_network_scanner.R
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.AboutViewModel

/**
 * About Screen - Displays app information, developer details, and links
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About", color = TextPrimary) },
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
        containerColor = DeepNavy
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App branding
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.mipmap.ic_launcher),
                        contentDescription = "SENET Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "SENET",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "Security Network Scanner",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    
                    Text(
                        text = viewModel.getVersionInfo(),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
            }
            
            // Developer card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.openDeveloperProfile() },
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = "https://github.com/phoenixdev-512.png",
                            contentDescription = "Developer",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, ElectricBlue, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Developed by",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextTertiary
                            )
                            Text(
                                "phoenixdev-512",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextTertiary
                        )
                    }
                }
            }
            
            // Action buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.openPlayStore() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Icon(Icons.Default.Star, null, tint = TrueBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rate on Play Store", color = TrueBlack)
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.shareApp() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = BorderStroke(1.dp, TextTertiary)
                    ) {
                        Icon(Icons.Default.Share, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share SENET")
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.checkForUpdates() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = BorderStroke(1.dp, TextTertiary)
                    ) {
                        Icon(Icons.Default.Update, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Check for Updates")
                    }
                }
            }
            
            // Links section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Links",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        AboutLink("Open Source Licenses", Icons.Default.Article) {
                            // In a real app, navigate to licenses screen
                            viewModel.openGitHubRepo()
                        }
                        AboutLink("Privacy Policy", Icons.Default.PrivacyTip) {
                            viewModel.openPrivacyPolicy()
                        }
                        AboutLink("Terms of Service", Icons.Default.Gavel) {
                            viewModel.openTermsOfService()
                        }
                        AboutLink("GitHub Repository", Icons.Default.Code) {
                            viewModel.openGitHubRepo()
                        }
                        AboutLink("Contact Support", Icons.Default.Email) {
                            viewModel.contactSupport()
                        }
                    }
                }
            }
            
            // Technical info (expandable)
            item {
                var expanded by remember { mutableStateOf(false) }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Technical Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Icon(
                                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                null,
                                tint = TextSecondary
                            )
                        }
                        
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                viewModel.getTechnicalInfo().forEach { (label, value) ->
                                    TechnicalInfoRow(label, value)
                                }
                            }
                        }
                    }
                }
            }
            
            // Third-party libraries (expandable)
            item {
                var expanded by remember { mutableStateOf(false) }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Third-Party Libraries",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Icon(
                                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                null,
                                tint = TextSecondary
                            )
                        }
                        
                        AnimatedVisibility(visible = expanded) {
                            Column(
                                modifier = Modifier.padding(top = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewModel.getThirdPartyLibraries().forEach { library ->
                                    LibraryItem(library)
                                }
                            }
                        }
                    }
                }
            }
            
            // Copyright
            item {
                Text(
                    "© 2025 SENET. All rights reserved.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
            }
        }
    }
}

/**
 * About Link Component
 */
@Composable
fun AboutLink(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = TextSecondary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, modifier = Modifier.weight(1f), color = TextPrimary)
        Icon(Icons.Default.ChevronRight, null, tint = TextTertiary)
    }
}

/**
 * Technical Info Row Component
 */
@Composable
fun TechnicalInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = ElectricBlue,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Library Item Component
 */
@Composable
fun LibraryItem(library: com.example.local_network_scanner.ui.viewmodel.LibraryInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                library.name,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                library.version,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontFamily = FontFamily.Monospace
            )
        }
        Text(
            library.license,
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary
        )
    }
}
