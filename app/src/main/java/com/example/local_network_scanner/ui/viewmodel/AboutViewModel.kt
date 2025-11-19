package com.example.local_network_scanner.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.local_network_scanner.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * ViewModel for About screen
 */
@HiltViewModel
class AboutViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    /**
     * Get app version information
     */
    fun getVersionInfo(): String {
        return "Version ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"
    }
    
    /**
     * Get build type (Debug/Release)
     */
    fun getBuildType(): String {
        return if (BuildConfig.DEBUG) "Debug" else "Release"
    }
    
    /**
     * Open Play Store for app rating
     */
    fun openPlayStore() {
        try {
            val appPackage = context.packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$appPackage")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web browser if Play Store app is not available
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Share the app with others
     */
    fun shareApp() {
        try {
            val shareText = buildString {
                append("Check out SENET - Security Network Scanner!\n\n")
                append("A powerful network security and monitoring tool for Android.\n\n")
                append("GitHub: https://github.com/phoenixdev-512/local_network_Scanner")
            }
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "SENET - Security Network Scanner")
                putExtra(Intent.EXTRA_TEXT, shareText)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            context.startActivity(Intent.createChooser(intent, "Share SENET").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    /**
     * Check for app updates
     */
    fun checkForUpdates() {
        // Open GitHub releases page
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner/releases/latest")
    }
    
    /**
     * Open developer's GitHub profile
     */
    fun openDeveloperProfile() {
        openUrl("https://github.com/phoenixdev-512")
    }
    
    /**
     * Open GitHub repository
     */
    fun openGitHubRepo() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner")
    }
    
    /**
     * Open privacy policy
     */
    fun openPrivacyPolicy() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner/blob/main/PRIVACY_POLICY.md")
    }
    
    /**
     * Open terms of service
     */
    fun openTermsOfService() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner/blob/main/TERMS_OF_SERVICE.md")
    }
    
    /**
     * Send email to developer
     */
    fun contactSupport() {
        try {
            val deviceInfo = buildString {
                appendLine("Device Model: ${android.os.Build.MODEL}")
                appendLine("Android Version: ${android.os.Build.VERSION.RELEASE} (SDK ${android.os.Build.VERSION.SDK_INT})")
                appendLine("App Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                appendLine()
                appendLine("--- Please describe your issue below ---")
                appendLine()
            }
            
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("phoenixdev512@example.com"))
                putExtra(Intent.EXTRA_SUBJECT, "SENET Support - ${getVersionInfo()}")
                putExtra(Intent.EXTRA_TEXT, deviceInfo)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            context.startActivity(Intent.createChooser(intent, "Contact Support").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: Exception) {
            // Handle error - no email app available
        }
    }
    
    /**
     * Get list of third-party libraries
     */
    fun getThirdPartyLibraries(): List<LibraryInfo> {
        return listOf(
            LibraryInfo("Jetpack Compose", "1.6.0", "Apache 2.0"),
            LibraryInfo("Material3", "1.2.0", "Apache 2.0"),
            LibraryInfo("Hilt", "2.48", "Apache 2.0"),
            LibraryInfo("Room", "2.6.1", "Apache 2.0"),
            LibraryInfo("OkHttp", "4.12.0", "Apache 2.0"),
            LibraryInfo("Retrofit", "2.9.0", "Apache 2.0"),
            LibraryInfo("Coil", "2.5.0", "Apache 2.0"),
            LibraryInfo("Kotlin", "1.9.22", "Apache 2.0"),
            LibraryInfo("Coroutines", "1.7.3", "Apache 2.0"),
            LibraryInfo("Navigation", "2.7.6", "Apache 2.0"),
            LibraryInfo("WorkManager", "2.9.0", "Apache 2.0"),
            LibraryInfo("DataStore", "1.0.0", "Apache 2.0"),
            LibraryInfo("Accompanist", "0.32.0", "Apache 2.0"),
            LibraryInfo("DNSJava", "3.5.3", "BSD-3-Clause"),
            LibraryInfo("Compose Markdown", "0.3.6", "MIT")
        )
    }
    
    /**
     * Get technical information
     */
    fun getTechnicalInfo(): List<Pair<String, String>> {
        return listOf(
            "Build Type" to getBuildType(),
            "Min SDK" to "24 (Android 7.0)",
            "Target SDK" to "34 (Android 14)",
            "Compile SDK" to "34",
            "Kotlin Version" to "1.9.22",
            "Gradle Version" to "8.2.0"
        )
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
}

/**
 * Library information data class
 */
data class LibraryInfo(
    val name: String,
    val version: String,
    val license: String
)
