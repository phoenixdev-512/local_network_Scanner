package com.example.local_network_scanner.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.BuildConfig
import com.example.local_network_scanner.data.model.FAQ
import com.example.local_network_scanner.data.model.FAQData
import com.example.local_network_scanner.services.GitHubApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Help & Documentation screen
 */
@HiltViewModel
class HelpViewModel @Inject constructor(
    private val gitHubApi: GitHubApiService,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _documentationContent = MutableStateFlow("")
    val documentationContent: StateFlow<String> = _documentationContent.asStateFlow()
    
    private val _faqs = MutableStateFlow<List<FAQ>>(FAQData.faqs)
    val faqs: StateFlow<List<FAQ>> = _faqs.asStateFlow()
    
    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()
    
    init {
        fetchDocumentation()
    }
    
    /**
     * Fetch README documentation from GitHub
     */
    fun fetchDocumentation() {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.Loading
                val readme = gitHubApi.getReadme(
                    owner = "phoenixdev-512",
                    repo = "local_network_Scanner"
                )
                _documentationContent.value = readme
                _loadingState.value = LoadingState.Success
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Error(
                    e.message ?: "Failed to load documentation"
                )
            }
        }
    }
    
    /**
     * Filter FAQs by search query and category
     */
    fun filterFAQs(query: String, category: String): List<FAQ> {
        return FAQData.faqs.filter { faq ->
            val matchesCategory = category == "All" || faq.category == category
            val matchesQuery = query.isEmpty() || 
                faq.question.contains(query, ignoreCase = true) ||
                faq.answer.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
    
    /**
     * Open GitHub issues page for bug reporting or feature requests
     */
    fun openGitHubIssues(issueType: IssueType) {
        val url = when (issueType) {
            IssueType.BUG -> "https://github.com/phoenixdev-512/local_network_Scanner/issues/new?labels=bug&template=bug_report.md"
            IssueType.FEATURE -> "https://github.com/phoenixdev-512/local_network_Scanner/issues/new?labels=enhancement&template=feature_request.md"
        }
        openUrl(url)
    }
    
    /**
     * Open GitHub repository
     */
    fun openGitHubRepo() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner")
    }
    
    /**
     * Open developer's GitHub profile
     */
    fun openDeveloperProfile() {
        openUrl("https://github.com/phoenixdev-512")
    }
    
    /**
     * Compose and send email to developer
     */
    fun sendEmail() {
        try {
            val deviceInfo = buildString {
                appendLine("Device Model: ${android.os.Build.MODEL}")
                appendLine("Android Version: ${android.os.Build.VERSION.RELEASE} (SDK ${android.os.Build.VERSION.SDK_INT})")
                appendLine("App Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                appendLine()
                appendLine("--- Please describe your issue or feedback below ---")
                appendLine()
            }
            
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("phoenixdev512@example.com"))
                putExtra(Intent.EXTRA_SUBJECT, "SENET Support Request")
                putExtra(Intent.EXTRA_TEXT, deviceInfo)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            context.startActivity(Intent.createChooser(intent, "Send Email").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: Exception) {
            // Handle error - no email app available
        }
    }
    
    /**
     * Open URL in browser
     */
    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle error - no browser available
        }
    }
    
    /**
     * Log FAQ feedback (helpful/not helpful)
     */
    fun logFAQFeedback(faqId: Int, helpful: Boolean) {
        // In a real app, this would log to analytics or a database
        // For now, it's a no-op
    }
}

/**
 * Loading state for documentation
 */
sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    object Success : LoadingState()
    data class Error(val message: String) : LoadingState()
}

/**
 * GitHub issue types
 */
enum class IssueType {
    BUG,
    FEATURE
}
