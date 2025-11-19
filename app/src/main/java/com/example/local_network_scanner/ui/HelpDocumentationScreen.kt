package com.example.local_network_scanner.ui

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.local_network_scanner.data.model.FAQ
import com.example.local_network_scanner.data.model.FAQData
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.HelpViewModel
import com.example.local_network_scanner.ui.viewmodel.IssueType
import com.example.local_network_scanner.ui.viewmodel.LoadingState
import dev.jeziellago.compose.markdowntext.MarkdownText

/**
 * Help & Documentation Screen with three tabs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpDocumentationScreen(
    viewModel: HelpViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Documentation", "FAQs", "Contact")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Documentation", color = TextPrimary) },
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = SurfaceDarkGray,
                contentColor = ElectricBlue
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) },
                        selectedContentColor = ElectricBlue,
                        unselectedContentColor = TextSecondary
                    )
                }
            }
            
            // Tab Content
            when (selectedTabIndex) {
                0 -> DocumentationTab(viewModel)
                1 -> FAQsTab(viewModel)
                2 -> ContactTab(viewModel)
            }
        }
    }
}

/**
 * Documentation Tab - Displays README from GitHub
 */
@Composable
fun DocumentationTab(viewModel: HelpViewModel) {
    val documentationContent by viewModel.documentationContent.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (loadingState) {
            is LoadingState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = ElectricBlue)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading documentation...", color = TextSecondary)
                }
            }
            is LoadingState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Failed to load documentation",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        (loadingState as LoadingState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.fetchDocumentation() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Retry", color = TrueBlack)
                    }
                }
            }
            else -> {
                if (documentationContent.isNotEmpty()) {
                    val scrollState = rememberScrollState()
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        MarkdownText(
                            markdown = documentationContent,
                            modifier = Modifier.fillMaxWidth(),
                            color = TextPrimary,
                            linkColor = ElectricBlue,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * FAQs Tab - Displays searchable and filterable FAQs
 */
@Composable
fun FAQsTab(viewModel: HelpViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    
    val filteredFAQs = remember(searchQuery, selectedCategory) {
        viewModel.filterFAQs(searchQuery, selectedCategory)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search FAQs...", color = TextTertiary) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricBlue,
                unfocusedBorderColor = TextTertiary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = ElectricBlue
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        // Category filter
        ScrollableTabRow(
            selectedTabIndex = FAQData.categories.indexOf(selectedCategory).takeIf { it >= 0 } ?: 0,
            containerColor = SurfaceDarkGray,
            contentColor = ElectricBlue,
            edgePadding = 16.dp
        ) {
            listOf("All") + FAQData.categories
                .forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category, maxLines = 1) },
                        selectedContentColor = ElectricBlue,
                        unselectedContentColor = TextSecondary
                    )
                }
        }
        
        // FAQ list
        if (filteredFAQs.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = TextTertiary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No FAQs found",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    "Try a different search or category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredFAQs) { faq ->
                    FAQItem(faq = faq, viewModel = viewModel)
                }
            }
        }
    }
}

/**
 * FAQ Item Component - Expandable card
 */
@Composable
fun FAQItem(faq: FAQ, viewModel: HelpViewModel) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
            
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Was this helpful?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                        IconButton(
                            onClick = { viewModel.logFAQFeedback(faq.id, true) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = "Helpful",
                                tint = Color(0xFF00C853),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = { viewModel.logFAQFeedback(faq.id, false) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbDown,
                                contentDescription = "Not helpful",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Contact Tab - Developer contact information and links
 */
@Composable
fun ContactTab(viewModel: HelpViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Email Support
        item {
            ContactSection(
                title = "Email Support",
                description = "Get help directly from the developer"
            ) {
                Button(
                    onClick = { viewModel.sendEmail() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Email Developer", color = TrueBlack)
                }
            }
        }
        
        // GitHub Issues
        item {
            ContactSection(
                title = "GitHub Issues",
                description = "Report bugs or request features"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.openGitHubIssues(IssueType.BUG) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.BugReport, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Report Bug")
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.openGitHubIssues(IssueType.FEATURE) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        border = BorderStroke(1.dp, ElectricBlue)
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Request")
                    }
                }
            }
        }
        
        // Developer Info
        item {
            ContactSection(
                title = "Developer",
                description = "About the creator of SENET"
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.openDeveloperProfile() },
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
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
                                "phoenixdev-512",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                "Open Source Developer",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
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
        }
        
        // Community Links
        item {
            ContactSection(
                title = "Community",
                description = "Connect with other SENET users"
            ) {
                OutlinedButton(
                    onClick = { viewModel.openGitHubRepo() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = BorderStroke(1.dp, TextTertiary)
                ) {
                    Icon(Icons.Default.Code, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GitHub Repository")
                }
            }
        }
    }
}

/**
 * Contact Section Component
 */
@Composable
fun ContactSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        content()
    }
}
