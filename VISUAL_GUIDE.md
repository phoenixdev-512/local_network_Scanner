# Visual Guide - UI/UX Enhancements

## Screen Navigation Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    NetSentry Application                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
            ┌─────────────────────────────────┐
            │   ModalNavigationDrawer (NEW)   │
            │                                 │
            │  ┌────────────────────────┐    │
            │  │  Profile Header        │    │
            │  │  • Avatar              │    │
            │  │  • Name & Email        │    │
            │  └────────────────────────┘    │
            │                                 │
            │  Navigation Items:              │
            │  ├─ Profile Management          │
            │  ├─ Network Manager             │
            │  ├─ Settings & Preferences      │
            │  ├─ Help & Documentation        │
            │  └─ About                       │
            └─────────────────────────────────┘
                              │
                              ▼
            ┌─────────────────────────────────┐
            │   Bottom Navigation (4 tabs)    │
            ├─────────────────────────────────┤
            │  Dashboard │ Network │ Security │ Activity │
            └─────────────────────────────────┘
```

## Screen Layouts

### 1. Dashboard Screen (NEW)

```
┌──────────────────────────────────────────────┐
│  Network Dashboard                      ⚙️   │
│  Real-time monitoring                        │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │     SpeedTestWidget                    │ │
│  │                                        │ │
│  │         ┌──────────────┐              │ │
│  │         │              │              │ │
│  │         │    45.2      │  ← Animated  │ │
│  │         │    Mbps      │    Gauge     │ │
│  │         │              │              │ │
│  │         └──────────────┘              │ │
│  │                                        │ │
│  │  Download │ Upload │ Ping             │ │
│  │   45.2    │  12.3  │  18              │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Security Overview               🛡️    │ │
│  │                                        │ │
│  │  Threats   Active      Security       │ │
│  │  Blocked   Connections Score          │ │
│  │   127         8         85/100        │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Data Usage Today                📊    │ │
│  │                                        │ │
│  │  ████████████░░░░░░░░░  33%          │ │
│  │  342.5 MB used  /  1024 MB total     │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Connected Devices               5     │ │
│  │  Tap to view detailed information      │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Quick Actions                         │ │
│  │                                        │ │
│  │   [📡]      [🚫]      [📜]            │ │
│  │  Scan     Block      View             │ │
│  │  Network   App       Logs             │ │
│  └────────────────────────────────────────┘ │
└──────────────────────────────────────────────┘
```

### 2. Profile Screen (NEW)

```
┌──────────────────────────────────────────────┐
│  Profile                              ✏️     │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │         ╔═══════════╗                  │ │
│  │         ║     👤    ║   ← Avatar       │ │
│  │         ╚═══════════╝                  │ │
│  │                                        │ │
│  │          Admin User                    │ │
│  │       admin@example.com                │ │
│  │                                        │ │
│  │       [ 🔑 ADMIN ]  ← Role Badge       │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌─────────┬─────────┬─────────┐           │
│  │Networks │Data Saved│Threats │           │
│  │   12    │  2.4 GB  │  847   │           │
│  └─────────┴─────────┴─────────┘           │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Account                               │ │
│  │  ├─ 👤 Personal Information        >  │ │
│  │  ├─ 🔒 Change Password             >  │ │
│  │  └─ 📧 Email Preferences           >  │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Security & Privacy                    │ │
│  │  ├─ 🔑 VPN Auto-Start          [OFF]  │ │
│  │  ├─ 🚫 Default Blocking         [ON]  │ │
│  │  └─ 🔐 Privacy Mode            [OFF]  │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Admin Tools        (Admin Only)       │ │
│  │  ├─ 👥 User Management             >  │ │
│  │  ├─ 📋 Global Policies             >  │ │
│  │  ├─ 📊 Audit Logs                  >  │ │
│  │  └─ 🐛 System Diagnostics          >  │ │
│  └────────────────────────────────────────┘ │
└──────────────────────────────────────────────┘
```

### 3. Network Manager Screen (NEW)

```
┌──────────────────────────────────────────────┐
│  Network Manager                         ➕  │
├──────────────────────────────────────────────┤
│  Saved Networks │ Policies │ Analytics       │
│  ═══════════════                             │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  📡 Home WiFi              ✓ Trusted   │ │
│  │     WPA2                               │ │
│  │                        85.5 Mbps       │ │
│  │                        Avg Speed       │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  📡 Office Network                     │ │
│  │     WPA3                               │ │
│  │                       120.3 Mbps       │ │
│  │                        Avg Speed       │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  📡 Coffee Shop                        │ │
│  │     Open                               │ │
│  │                        25.7 Mbps       │ │
│  │                        Avg Speed       │ │
│  └────────────────────────────────────────┘ │
└──────────────────────────────────────────────┘
```

### 4. Enhanced Settings Screen (NEW)

```
┌──────────────────────────────────────────────┐
│  ← Settings                                  │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  🎨 Appearance                         │ │
│  │                                        │ │
│  │  🌙 Dark Mode                  [ON]   │ │
│  │     Use dark theme                     │ │
│  │  ─────────────────────────────         │ │
│  │  🎨 Theme Selection               >   │ │
│  │     Speedtest, Classic, Minimal        │ │
│  │  ─────────────────────────────         │ │
│  │  🔤 Font Size                     >   │ │
│  │     Adjust text size                   │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  🛡️ Security & Privacy                 │ │
│  │                                        │ │
│  │  🔑 VPN Auto-Start            [OFF]   │ │
│  │     Start VPN when device boots        │ │
│  │  ─────────────────────────────         │ │
│  │  🚫 Block All by Default       [ON]   │ │
│  │     Block all connections              │ │
│  │  ─────────────────────────────         │ │
│  │  🌐 DNS Provider                  >   │ │
│  │     Cloudflare                         │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  🔔 Notifications                      │ │
│  │                                        │ │
│  │  ⚠️ Threat Alerts              [ON]   │ │
│  │  📡 Connection Logs           [OFF]   │ │
│  │  📊 Weekly Summary            [OFF]   │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  🔧 Admin Tools     (Admin Only)       │ │
│  │                                        │ │
│  │  👥 User Management               >   │ │
│  │  📋 Global Policies               >   │ │
│  │  📊 Audit Logs                    >   │ │
│  └────────────────────────────────────────┘ │
└──────────────────────────────────────────────┘
```

## Color Palette

```
┌────────────────────────────────────────────┐
│  Speedtest-Inspired Color System          │
├────────────────────────────────────────────┤
│                                            │
│  PRIMARY COLORS                            │
│  ████ DeepNavy      #0A1931               │
│  ████ ElectricBlue  #1E88E5               │
│  ████ PrimaryBlue   #2196F3               │
│                                            │
│  ACCENT COLORS                             │
│  ████ VibrantGreen  #00C853 (Success)     │
│  ████ WarningOrange #FF6F00 (Warning)     │
│  ████ ThreatRed     #D32F2F (Error)       │
│  ████ InfoCyan      #00BCD4 (Info)        │
│                                            │
│  BACKGROUND COLORS                         │
│  ████ TrueBlack     #000000               │
│  ████ SurfaceDarkGray #1E1E1E             │
│  ████ CardBackground  #2A2A2A             │
│                                            │
│  TEXT COLORS                               │
│  ████ TextPrimary   #FFFFFF               │
│  ████ TextSecondary #B0BEC5               │
│  ████ TextTertiary  #78909C               │
└────────────────────────────────────────────┘
```

## Typography Scale

```
┌────────────────────────────────────────────┐
│  Material3 Typography System               │
├────────────────────────────────────────────┤
│                                            │
│  Display Large    57sp  Bold               │
│  Display Medium   45sp  Bold               │
│  Display Small    36sp  Bold               │
│                                            │
│  Headline Large   32sp  SemiBold           │
│  Headline Medium  28sp  SemiBold           │
│  Headline Small   24sp  SemiBold           │
│                                            │
│  Title Large      22sp  SemiBold           │
│  Title Medium     16sp  Medium             │
│  Title Small      14sp  Medium             │
│                                            │
│  Body Large       16sp  Normal             │
│  Body Medium      14sp  Normal             │
│  Body Small       12sp  Normal             │
│                                            │
│  Label Large      14sp  Medium             │
│  Label Medium     12sp  Medium             │
│  Label Small      11sp  Medium             │
└────────────────────────────────────────────┘
```

## Animation System

```
┌────────────────────────────────────────────┐
│  Page Transitions                          │
├────────────────────────────────────────────┤
│                                            │
│  Bottom Navigation Screens (300ms)         │
│  ├─ Enter: fadeIn + slideInHorizontally    │
│  └─ Exit:  fadeOut + slideOutHorizontally  │
│                                            │
│  Drawer Screens (400ms)                    │
│  ├─ Enter: slideInHorizontally + fadeIn    │
│  └─ Exit:  slideOutHorizontally + fadeOut  │
│                                            │
│  Loading States                            │
│  └─ Shimmer: 1200ms infinite linear        │
└────────────────────────────────────────────┘
```

## Database Schema (v6)

```
┌────────────────────────────────────────────┐
│  New Tables in Version 6                   │
├────────────────────────────────────────────┤
│                                            │
│  user_profiles                             │
│  ├─ id (PK)                                │
│  ├─ name                                   │
│  ├─ email                                  │
│  ├─ role (ADMIN/STANDARD)                  │
│  ├─ avatarUri                              │
│  ├─ createdAt                              │
│  └─ preferences (settings)                 │
│                                            │
│  saved_networks                            │
│  ├─ ssid (PK)                              │
│  ├─ bssid                                  │
│  ├─ securityType                           │
│  ├─ isTrusted                              │
│  ├─ firewallPolicyId (FK)                  │
│  ├─ averageSpeed                           │
│  └─ lastConnected                          │
│                                            │
│  network_policies                          │
│  ├─ id (PK)                                │
│  ├─ name                                   │
│  ├─ description                            │
│  ├─ allowedAppsJson                        │
│  ├─ blockedDomainsJson                     │
│  ├─ dnsProvider                            │
│  ├─ enableAdBlocking                       │
│  └─ enableMalwareProtection                │
│                                            │
│  speed_test_results                        │
│  ├─ id (PK)                                │
│  ├─ ssid                                   │
│  ├─ downloadSpeed                          │
│  ├─ uploadSpeed                            │
│  ├─ ping                                   │
│  ├─ jitter                                 │
│  └─ timestamp                              │
└────────────────────────────────────────────┘
```

## Component Hierarchy

```
NetSentryApp
├── ModalNavigationDrawer
│   ├── DrawerHeader
│   └── DrawerNavigationItems
│       ├── Profile
│       ├── NetworkManager
│       ├── Settings
│       ├── Help
│       └── About
│
├── Scaffold
│   ├── BottomNavigationBar
│   │   ├── Dashboard
│   │   ├── Network
│   │   ├── Security
│   │   └── Activity
│   │
│   └── NavHost (with animations)
│       ├── DashboardScreen
│       │   ├── SpeedTestWidget
│       │   ├── SecurityOverviewWidget
│       │   ├── DataUsageWidget
│       │   ├── ConnectedDevicesWidget
│       │   └── QuickActionsWidget
│       │
│       ├── ProfileScreen
│       │   ├── ProfileHeader
│       │   ├── UserStatisticsRow
│       │   ├── AccountSection
│       │   ├── SecuritySection
│       │   ├── NotificationSection
│       │   ├── AppearanceSection
│       │   └── AdminToolsSection
│       │
│       ├── NetworkManagerScreen
│       │   ├── TabRow
│       │   ├── SavedNetworksTab
│       │   ├── NetworkPoliciesTab
│       │   └── NetworkAnalyticsTab
│       │
│       └── EnhancedSettingsScreen
│           ├── AppearanceSection
│           ├── SecuritySection
│           ├── NotificationsSection
│           ├── AdvancedSection
│           ├── AdminToolsSection
│           └── AboutSection
```

---

**Note:** This is a text-based visual representation. Actual screenshots would be taken when the app is built and running on an Android device.

