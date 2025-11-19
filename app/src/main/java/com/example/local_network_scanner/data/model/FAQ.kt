package com.example.local_network_scanner.data.model

/**
 * FAQ (Frequently Asked Questions) data model
 */
data class FAQ(
    val id: Int,
    val category: String,
    val question: String,
    val answer: String
)

/**
 * Sample FAQ data for the Help & Documentation section
 */
object FAQData {
    val categories = listOf(
        "Setup & Installation",
        "Features & Usage",
        "Troubleshooting",
        "Privacy & Security"
    )
    
    val faqs = listOf(
        FAQ(
            id = 1,
            category = "Setup & Installation",
            question = "How do I enable VPN permissions?",
            answer = "Go to Settings > VPN and tap 'Enable VPN'. Android will show a permission dialog requesting VPN access. Accept this to allow SENET to monitor network traffic. You can revoke this permission at any time from Android Settings."
        ),
        FAQ(
            id = 2,
            category = "Setup & Installation",
            question = "What permissions does SENET require?",
            answer = "SENET requires the following permissions:\n\n• VPN Service: To monitor network traffic\n• Internet Access: To fetch security information\n• Location: To detect nearby WiFi networks (optional)\n• Notifications: To alert you about security threats\n\nAll permissions are used exclusively for network security purposes."
        ),
        FAQ(
            id = 3,
            category = "Setup & Installation",
            question = "How do I install SENET on my device?",
            answer = "Download the APK from the GitHub releases page, enable 'Install from Unknown Sources' in Android Settings, then tap the downloaded file to install. For Google Play installation (when available), simply search for SENET in the Play Store."
        ),
        FAQ(
            id = 4,
            category = "Features & Usage",
            question = "How does the security score work?",
            answer = "The security score (0-100) is calculated based on multiple factors:\n\n• Number of active connections (weighted 20%)\n• Blocked threats count (weighted 30%)\n• Suspicious apps detected (weighted 25%)\n• Network encryption status (weighted 15%)\n• Firewall rules active (weighted 10%)\n\nHigher scores indicate better security posture."
        ),
        FAQ(
            id = 5,
            category = "Features & Usage",
            question = "What is the Network Scanner feature?",
            answer = "The Network Scanner discovers all devices connected to your WiFi network. It shows device names, IP addresses, MAC addresses, manufacturers, and open ports. This helps you identify unauthorized devices on your network."
        ),
        FAQ(
            id = 6,
            category = "Features & Usage",
            question = "How do I block an app from accessing the internet?",
            answer = "Navigate to Security > App Rules, find the app you want to block, and toggle the switch to 'Blocked'. The app will no longer be able to send or receive network data while the VPN is active."
        ),
        FAQ(
            id = 7,
            category = "Features & Usage",
            question = "What is Geo-Blocking and how does it work?",
            answer = "Geo-Blocking allows you to block connections to specific countries. Go to Security > Geo-Block, select countries from the list, and enable the feature. All connection attempts to those countries will be automatically blocked."
        ),
        FAQ(
            id = 8,
            category = "Features & Usage",
            question = "How can I view my network activity history?",
            answer = "The Activity screen shows real-time and historical network data in 5-minute intervals. You can see data usage, connection counts, and security events. Tap any time period to see detailed connection logs."
        ),
        FAQ(
            id = 9,
            category = "Troubleshooting",
            question = "Why is my internet connection slow after enabling VPN?",
            answer = "VPN services add a small overhead due to packet inspection. To improve performance:\n\n• Reduce the number of active firewall rules\n• Disable features you don't need (geo-blocking, DNS filtering)\n• Clear the connection log regularly\n• Ensure your device has sufficient RAM"
        ),
        FAQ(
            id = 10,
            category = "Troubleshooting",
            question = "The app crashes when scanning networks. What should I do?",
            answer = "Try these steps:\n\n1. Ensure you have granted all required permissions\n2. Clear the app cache (Settings > Apps > SENET > Clear Cache)\n3. Restart your device\n4. Update to the latest version\n5. If the issue persists, report it on GitHub with your device model and Android version"
        ),
        FAQ(
            id = 11,
            category = "Troubleshooting",
            question = "VPN disconnects frequently. How can I fix this?",
            answer = "Common solutions:\n\n• Disable battery optimization for SENET (Settings > Battery > Battery Optimization)\n• Enable 'Always-on VPN' in Android VPN settings\n• Check if other VPN apps are running simultaneously\n• Ensure SENET has 'Run in background' permission"
        ),
        FAQ(
            id = 12,
            category = "Troubleshooting",
            question = "Some apps don't work when VPN is enabled. Why?",
            answer = "Some apps detect VPN usage and restrict functionality. You can:\n\n• Add the app to the bypass list (Settings > VPN Bypass)\n• Use split tunneling to route specific apps outside the VPN\n• Temporarily disable the VPN for those apps\n• Contact the app developer about VPN compatibility"
        ),
        FAQ(
            id = 13,
            category = "Privacy & Security",
            question = "Does SENET collect or share my data?",
            answer = "No. SENET is open-source and privacy-focused:\n\n• All network analysis happens locally on your device\n• No data is sent to external servers\n• No telemetry or analytics are collected\n• No ads or third-party trackers\n\nYou can verify this by reviewing the source code on GitHub."
        ),
        FAQ(
            id = 14,
            category = "Privacy & Security",
            question = "Can SENET decrypt HTTPS traffic?",
            answer = "No. SENET does not and cannot decrypt HTTPS/TLS encrypted traffic. It only analyzes connection metadata (IP addresses, ports, packet counts) without accessing encrypted content. This ensures your private communications remain secure."
        ),
        FAQ(
            id = 15,
            category = "Privacy & Security",
            question = "Is my connection log stored securely?",
            answer = "Yes. All connection logs are stored locally in an encrypted SQLite database on your device. The data never leaves your device unless you explicitly export it. You can clear logs at any time from Settings."
        ),
        FAQ(
            id = 16,
            category = "Privacy & Security",
            question = "How does SENET protect against malicious websites?",
            answer = "SENET uses multiple protection layers:\n\n• DNS-based blocking of known malicious domains\n• IP blocklists updated regularly\n• Geo-blocking for high-risk regions\n• Real-time analysis of connection patterns\n• User-defined firewall rules"
        ),
        FAQ(
            id = 17,
            category = "Features & Usage",
            question = "What is the difference between Admin and Standard user profiles?",
            answer = "Admin profiles have full access to all features including:\n• Creating/editing/deleting profiles\n• Modifying security settings\n• Changing firewall rules\n• Exporting logs\n\nStandard profiles have view-only access to logs and limited settings access."
        ),
        FAQ(
            id = 18,
            category = "Troubleshooting",
            question = "How do I reset SENET to factory defaults?",
            answer = "Go to Settings > Advanced > Reset Application. This will:\n• Clear all connection logs\n• Reset firewall rules to defaults\n• Clear all profiles (except one default)\n• Reset all preferences\n\nYour user profiles can be backed up before resetting."
        ),
        FAQ(
            id = 19,
            category = "Setup & Installation",
            question = "Can I use SENET alongside other VPN apps?",
            answer = "No. Android only allows one VPN connection at a time. You must disconnect from other VPN apps before enabling SENET's VPN service. However, you can use SENET without VPN for network scanning features."
        ),
        FAQ(
            id = 20,
            category = "Privacy & Security",
            question = "What happens to my data if I uninstall SENET?",
            answer = "All data is permanently deleted when you uninstall SENET, including:\n• Connection logs\n• User profiles\n• Firewall rules\n• App preferences\n\nTo preserve data, export your configuration before uninstalling."
        )
    )
}
