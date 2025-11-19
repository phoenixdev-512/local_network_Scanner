# NetSentry User Guide

## Table of Contents

1. [Getting Started](#getting-started)
2. [Dashboard](#dashboard)
3. [Network Scanner](#network-scanner)
4. [Security](#security)
5. [Activity Monitor](#activity-monitor)
6. [Settings](#settings)
7. [Troubleshooting](#troubleshooting)

## Getting Started

### Installation

1. Download the APK from the releases page
2. Enable "Install from Unknown Sources" in Android settings
3. Install the APK
4. Grant required permissions when prompted

### First Launch

On first launch, NetSentry will request the following permissions:

- **Location**: Required by Android to access WiFi network information
- **Notifications**: For security alerts and network status
- **Query All Packages**: To scan installed apps for security analysis

You can choose to grant or deny these permissions. Some features require specific permissions to function.

## Dashboard

The dashboard provides an overview of your network status and security.

### Network Speed

Real-time display of your current network speed:
- **Download Speed**: Data received per second (Mbps)
- **Upload Speed**: Data sent per second (Mbps)
- **Ping**: Latency to Google DNS (8.8.8.8) in milliseconds

**Interpretation**:
- Green ping (<50ms): Excellent connection
- Orange ping (50-100ms): Good connection
- Red ping (>100ms): Poor connection

### Security Score

A 0-100 score indicating your device's security status:
- **90-100**: Excellent security
- **70-89**: Good security
- **50-69**: Fair security (review recommendations)
- **0-49**: Poor security (action required)

The score is calculated based on:
- Number of detected threats
- Suspicious app installations
- Apps with network access
- Active network connections

### Data Usage

Visual representation of network data consumption:
- Total data used
- Upload/download breakdown
- Real-time updates

### Connected Devices

Number of devices detected on your local network. Tap to scan for detailed device information.

### Quick Actions

- **Scan Network**: Discover WiFi networks and devices
- **Block App**: Access security features
- **View Logs**: See recent network activity

## Network Scanner

Discover and analyze WiFi networks and devices on your network.

### WiFi Networks

**Scanning for Networks**:
1. Tap the scan button (circular arrow icon)
2. Grant location permission if prompted
3. Wait for scan to complete
4. View discovered networks in the list

**Network Information**:
- SSID (network name)
- Signal strength (bars and dBm)
- Security type (Open, WPA, WPA2, WPA3)
- Frequency (2.4GHz or 5GHz)

**Sorting Options**:
- By signal strength (strongest first)
- By name (alphabetical)
- By security type

**Filtering**:
- All networks
- Open networks only
- Secured networks only

### Device Discovery

**Scanning for Devices**:
1. Navigate to the Network tab
2. Ensure you're connected to WiFi
3. Tap "Scan Devices"
4. Wait for scan to complete (may take 30-60 seconds)

**Device Information**:
- IP address
- Device type (if detected)
- Reachability status

**Note**: Device scanning works best on local networks. Some routers may block scanning.

## Security

Comprehensive security analysis of installed applications.

### Running a Security Scan

1. Navigate to the Security tab
2. Tap the large "Scan" button
3. Wait for scan to complete (typically 10-30 seconds)
4. Review detected issues

### Scan Phases

The security scan consists of three phases:

1. **Network Access Count** (20%): Counts apps with internet permission
2. **Suspicious App Detection** (60%): Analyzes app permissions and behavior
3. **Security Score Calculation** (20%): Computes overall security rating

### Suspicious Apps

Apps are flagged as suspicious based on:

**Permission Combinations**:
- Internet + Location + Camera
- Internet + SMS/MMS
- Internet + Contacts + Phone State

**Network Usage**:
- Apps using >100MB of data

**Risk Levels**:
- **Low**: 1 suspicious reason
- **Medium**: 2 suspicious reasons
- **High**: 3+ suspicious reasons

### Taking Action

For each suspicious app, you can:
- **View Details**: See why the app was flagged
- **Uninstall**: Remove the app from your device
- **Ignore**: Mark as false positive (future feature)

### Security Metrics

Real-time security information:
- Total installed apps
- Apps with network access
- Active network connections
- Threats detected

## Activity Monitor

Track network usage per application over the last 5 minutes.

### Understanding Activity Data

**Per-App Metrics**:
- App name and icon
- Upload bytes
- Download bytes
- Active connections
- Last active timestamp

**Data Format**:
- Bytes (B)
- Kilobytes (KB)
- Megabytes (MB)
- Gigabytes (GB)

### Search and Filter

**Search**:
1. Tap the search icon
2. Enter app name or package name
3. Results filter automatically

**Sort Options**:
- By data usage (highest first)
- By app name (alphabetical)
- By connection count (most active first)

### Interpreting Results

High data usage may indicate:
- Video streaming
- Downloads in progress
- Background sync
- Potential data leak (investigate if unexpected)

## Settings

Configure NetSentry to your preferences.

### Firewall Controls

**Block All by Default**:
- When enabled, new apps are blocked from network access
- Requires VPN service to be active

**Ad Blocking**:
- Blocks known advertising domains
- Reduces data usage and improves privacy

**Malware Blocking**:
- Blocks connections to known malicious domains
- Provides additional security layer

### DNS Configuration

**DNS Mode**:
- System: Use device default DNS
- Cloudflare: Use 1.1.1.1
- Google: Use 8.8.8.8
- Custom: Enter your own DNS server

**Secure DNS (DNS over HTTPS)**:
- Encrypts DNS queries
- Prevents DNS hijacking
- May increase latency slightly

**Custom DNS IP**:
Enter IP address in format: `1.2.3.4`

### Notifications

**Threat Detection Alerts**:
- Notified when new threats are detected
- Includes threat details

**New App Notifications**:
- Alert when new apps are installed
- Helps detect unwanted installations

**Weekly Summary**:
- Summary of network activity
- Security status overview
- Sent every Monday

### Data and Privacy

- All data is processed locally on your device
- No information is sent to external servers
- Settings are stored securely using DataStore

## Troubleshooting

### WiFi Scanning Not Working

**Solution**:
1. Ensure location permission is granted
2. Enable location services in device settings
3. Check that WiFi is enabled
4. Try restarting the app

### No Devices Found

**Possible Causes**:
- Not connected to WiFi
- Router blocking ping requests
- Firewall restrictions
- Network isolation enabled on router

**Solutions**:
- Connect to WiFi network
- Disable AP isolation in router settings
- Check router firewall settings

### Security Scan Fails

**Solutions**:
1. Grant "Query All Packages" permission
2. Restart the app
3. Clear app cache in system settings
4. Reinstall if issue persists

### Network Speed Shows Zero

**Possible Causes**:
- No active data transfer
- VPN interference
- TrafficStats not supported on device

**Solutions**:
- Use internet (browse, stream) to generate traffic
- Disable VPN temporarily
- Device may not support real-time monitoring

### Permissions Not Requested

If permissions aren't requested on first launch:

1. Go to Android Settings > Apps > NetSentry
2. Tap "Permissions"
3. Manually grant required permissions
4. Restart NetSentry

## Tips and Best Practices

### Battery Optimization

- NetSentry adapts to power save mode automatically
- Disable real-time monitoring when not needed
- Close app when not in use

### Network Efficiency

- Run device scans sparingly (every few minutes at most)
- Use estimation instead of full scans when possible
- Avoid scanning on mobile data

### Security Best Practices

1. Run security scans weekly
2. Review suspicious apps carefully
3. Uninstall unused apps
4. Keep apps updated
5. Only install apps from trusted sources

### Privacy Considerations

- Grant only necessary permissions
- Review app network usage regularly
- Disable features you don't use
- Check DNS settings for privacy

## Frequently Asked Questions

**Q: Does NetSentry collect my data?**  
A: No. All processing is done locally on your device. No data is transmitted externally.

**Q: Why does NetSentry need location permission?**  
A: Android requires location permission to access WiFi network information (SSID). NetSentry does not track your location.

**Q: Can NetSentry block internet for specific apps?**  
A: Not yet. This feature requires VPN service integration, which is planned for a future update.

**Q: Is NetSentry compatible with VPN apps?**  
A: NetSentry works alongside most VPN apps, but some features may be limited.

**Q: How accurate is the security score?**  
A: The score is based on multiple factors and provides a general indication. It should not be the sole measure of device security.

**Q: Can I export my activity logs?**  
A: Log export is planned for a future update.

## Getting Help

If you encounter issues not covered in this guide:

1. Open an issue on GitHub
2. Contact the developer: phoenixdev-512
3. Check for app updates

## Version

This guide is for NetSentry version 1.0.0

Last updated: 2025-11-19
