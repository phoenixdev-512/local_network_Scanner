# 🏗️ NetSentry Architecture Overview

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                           NetSentry App                              │
│                        (Kotlin + Jetpack Compose)                    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        ▼                           ▼                           ▼
┌───────────────┐          ┌────────────────┐         ┌─────────────────┐
│  UI Layer     │          │ ViewModel      │         │  Data Layer     │
│  (Compose)    │◄─────────│  (StateFlow)   │◄────────│ (Repository)    │
└───────────────┘          └────────────────┘         └─────────────────┘
        │                           │                           │
        │                           │                           │
┌───────────────────────────────────────────────────────────────────────┐
│                         Key Components                                │
├───────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  📱 UI Screens                    🧠 ViewModels                       │
│  ├─ DashboardScreen              ├─ DashboardViewModel              │
│  │  └─ Live Metrics (0.5s)       │  └─ NetworkMonitor               │
│  ├─ FirewallScreen               ├─ MainViewModel                   │
│  │  └─ VPN Controls              │  └─ VPN State Management         │
│  ├─ ActivityScreen               └─ ActivityViewModel               │
│  │  └─ 5-min Stats                  └─ Network Activity            │
│  └─ WifiScreen                                                       │
│     └─ Network Scanning                                              │
│                                                                       │
│  🔧 Services                      📊 Data Models                      │
│  ├─ NetworkMonitor               ├─ NetworkSpeed                     │
│  │  ├─ Speed Measurement         ├─ AppNetworkActivity              │
│  │  └─ Ping Measurement          └─ DataUsageStats                  │
│  └─ NetSentryVpnService                                              │
│     ├─ Packet Filtering                                              │
│     └─ Traffic Analysis                                              │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
```

## Data Flow Architecture

```
┌──────────────┐      StateFlow     ┌──────────────┐     Coroutines    ┌──────────────┐
│              │◄───────────────────│              │◄──────────────────│              │
│  UI Screen   │                    │  ViewModel   │                   │   Service    │
│  (Compose)   │                    │ (Business    │                   │  (Network    │
│              │────────────────────►│  Logic)      │───────────────────►│  Monitor)    │
└──────────────┘   User Actions     └──────────────┘    Requests       └──────────────┘
      │                                    │                                   │
      │ collectAsState()                   │ StateFlow Updates                 │
      ▼                                    ▼                                   ▼
   Recompose                          Update State                      Measure Data
   UI Updates                         MutableStateFlow                  TrafficStats
```

## Feature Implementation Map

```
┌─────────────────────────────────────────────────────────────────────┐
│                     Dashboard Screen (Real-Time)                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  NetworkMonitor Service (500ms updates)              │           │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │           │
│  │  │ TrafficStats│  │ Ping 8.8.8.8│  │  StateFlow  │  │           │
│  │  │  Download   │→ │  Measure    │→ │   Update    │  │           │
│  │  │   Upload    │  │   Latency   │  │   Every 0.5s│  │           │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  DashboardViewModel                                   │           │
│  │  - networkSpeed: StateFlow<NetworkSpeed>             │           │
│  │  - ping: StateFlow<Int>                              │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  DashboardScreen (Compose UI)                        │           │
│  │  - Animated speed gauge                              │           │
│  │  - Live metrics with transitions                     │           │
│  │  - "LIVE" pulsing indicator                          │           │
│  │  - Quick action buttons (navigation)                 │           │
│  └──────────────────────────────────────────────────────┘           │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                     Security Screen (VPN Control)                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  User Action: Tap START                              │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  VPN Permission Request                               │           │
│  │  - ActivityResultLauncher                            │           │
│  │  - VpnService.prepare()                              │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  MainViewModel.startVpn()                            │           │
│  │  - Start NetSentryVpnService                         │           │
│  │  - Monitor service state (1s)                        │           │
│  │  - Trigger network scan                              │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  UI Updates                                           │           │
│  │  - Status: "PROTECTING: Your Network"                │           │
│  │  - Button: Red "STOP"                                │           │
│  │  - Scanning progress: 0% → 100%                      │           │
│  └──────────────────────────────────────────────────────┘           │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                  Activity Screen (5-Minute Tracking)                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  ActivityViewModel (1s updates)                      │           │
│  │  ┌────────────────────────────────────────┐          │           │
│  │  │ Collect network activity last 5 min    │          │           │
│  │  │  - Per-app connection counts           │          │           │
│  │  │  - Upload/Download bytes               │          │           │
│  │  │  - App icons and names                 │          │           │
│  │  └────────────────────────────────────────┘          │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  Data Aggregation                                     │           │
│  │  - Group by package name                             │           │
│  │  - Sum upload/download per app                       │           │
│  │  - Count connections                                 │           │
│  │  - Sort by total data (desc)                         │           │
│  └──────────────────────────────────────────────────────┘           │
│                           ▼                                          │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  ActivityScreen (Compose UI)                         │           │
│  │  - Time range header: "Last 5 Minutes"               │           │
│  │  - Summary card: Total up/down/apps                  │           │
│  │  - LazyColumn: Per-app breakdown                     │           │
│  │    └─ Icon, Name, Connections, Upload↑, Download↓   │           │
│  └──────────────────────────────────────────────────────┘           │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## Animation System

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Animation Architecture                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Screen Transitions (Navigation)                                     │
│  ┌─────────────┐         ┌─────────────┐         ┌─────────────┐   │
│  │  Screen A   │─────────►│ Transition  │─────────►│  Screen B   │   │
│  │  (Current)  │         │  300ms       │         │  (Target)   │   │
│  └─────────────┘         └─────────────┘         └─────────────┘   │
│        │                       │                       │             │
│        │ fadeOut               │ fadeIn +              │             │
│        │ slideOut              │ slideIn               │             │
│        └───────────────────────┴───────────────────────┘             │
│                                                                       │
│  Value Animations (Metrics)                                          │
│  ┌─────────────┐         ┌─────────────┐         ┌─────────────┐   │
│  │ Old Value   │         │  Animate    │         │  New Value  │   │
│  │   45.2      │─────────►│   500ms     │─────────►│   52.8      │   │
│  └─────────────┘         └─────────────┘         └─────────────┘   │
│        │                       │                       │             │
│        │ FastOut               │ SlowIn                │             │
│        │ Easing                │ Curve                 │             │
│        └───────────────────────┴───────────────────────┘             │
│                                                                       │
│  Infinite Animations (Indicators)                                    │
│  ┌─────────────┐         ┌─────────────┐                            │
│  │  Visible    │◄────────┤  Fade       │                            │
│  │  alpha=1.0  │─────────►│  800ms      │                            │
│  └─────────────┘         └─────────────┘                            │
│        │                       │                                     │
│        │◄──── Repeat ──────────┤                                     │
│        │      Forever          │                                     │
│        │      Reverse          │                                     │
│        └───────────────────────┘                                     │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## Dependency Injection (Hilt)

```
┌─────────────────────────────────────────────────────────────────────┐
│                     Hilt Dependency Graph                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│                    @HiltAndroidApp                                    │
│                  NetSentryApplication                                 │
│                          │                                            │
│            ┌─────────────┼─────────────┐                             │
│            ▼             ▼             ▼                             │
│      @Singleton    @Singleton    @Singleton                          │
│    NetworkMonitor   ProfileDao   GeoIpService                        │
│            │             │             │                             │
│            └─────────────┼─────────────┘                             │
│                          ▼                                            │
│                  @HiltViewModel                                       │
│                DashboardViewModel                                     │
│                          │                                            │
│                          ▼                                            │
│                    @Composable                                        │
│                  DashboardScreen                                      │
│                                                                       │
│  Injection Flow:                                                      │
│  Application → Singleton Services → ViewModels → UI Screens          │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## State Management Pattern

```
┌─────────────────────────────────────────────────────────────────────┐
│                   StateFlow Reactive Pattern                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ViewModel Layer                                                      │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  private val _data = MutableStateFlow(initialValue)  │           │
│  │  val data: StateFlow<Type> = _data                   │           │
│  │                                                       │           │
│  │  fun updateData(newValue: Type) {                    │           │
│  │      viewModelScope.launch {                         │           │
│  │          _data.value = newValue                      │           │
│  │      }                                                │           │
│  │  }                                                    │           │
│  └──────────────────────────────────────────────────────┘           │
│                          │                                            │
│                          │ StateFlow emission                         │
│                          ▼                                            │
│  UI Layer                                                             │
│  ┌──────────────────────────────────────────────────────┐           │
│  │  @Composable                                          │           │
│  │  fun Screen(viewModel: VM = hiltViewModel()) {       │           │
│  │      val data by viewModel.data.collectAsState()     │           │
│  │                                                       │           │
│  │      // UI automatically recomposes when data changes│           │
│  │      Text(text = data.toString())                    │           │
│  │  }                                                    │           │
│  └──────────────────────────────────────────────────────┘           │
│                                                                       │
│  Benefits:                                                            │
│  ✅ Unidirectional data flow                                         │
│  ✅ Lifecycle aware                                                   │
│  ✅ Thread safe                                                       │
│  ✅ Compose integration                                               │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## Update Cycle Timeline

```
Time:  0ms     500ms    1000ms   1500ms   2000ms   2500ms   3000ms
       │        │        │        │        │        │        │
       ▼        ▼        ▼        ▼        ▼        ▼        ▼
Speed: ●────────●────────●────────●────────●────────●────────●  (NetworkMonitor)
Ping:  ●────────●────────●────────●────────●────────●────────●  (NetworkMonitor)
       │                 │                 │                 │
VPN:   ●─────────────────●─────────────────●─────────────────●  (MainViewModel)
       │                 │                 │                 │
Activity: ●──────────────●─────────────────●─────────────────●  (ActivityViewModel)

Legend:
● = Data measurement/update
─ = Waiting period
```

## Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                      App Navigation Graph                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│                     Bottom Navigation Bar                             │
│                    ┌───┬───┬────┬────┐                              │
│                    │ 🏠│📶 │🛡️ │📊 │                              │
│                    └─┬─┴─┬─┴──┬─┴──┬─┘                              │
│                      │   │    │    │                                 │
│         ┌────────────┼───┼────┼────┼────────────┐                   │
│         │            │   │    │    │            │                   │
│         ▼            ▼   ▼    ▼    ▼            ▼                   │
│    Dashboard     Network Security Activity   Settings               │
│    (Default)     (WiFi)  (VPN)  (5-min)    (Drawer)                 │
│         │                  │       │                                 │
│         │                  │       │                                 │
│    Quick Actions           │       │                                 │
│    ┌────┴────┐             │       │                                 │
│    │         │             │       │                                 │
│    ▼         ▼             │       │                                 │
│  Network  Security ────────┘       │                                 │
│   (Tab)    (Tab)                   │                                 │
│                                    │                                 │
│  View Logs ────────────────────────┘                                 │
│   (Action)                                                            │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## File Organization Structure

```
app/src/main/java/com/example/local_network_scanner/
│
├── data/
│   ├── db/              (Room Database)
│   │   ├── Profile.kt
│   │   └── ProfileDao.kt
│   ├── datastore/       (Preferences)
│   └── model/           ✨ NEW
│       ├── NetworkSpeed.kt
│       └── AppNetworkActivity.kt
│
├── di/                  (Hilt Modules)
│   └── AppModule.kt
│
├── services/            ✨ UPDATED
│   ├── NetworkMonitor.kt          ✨ NEW
│   ├── GeoIpService.kt
│   └── NetSentryTileService.kt
│
├── ui/                  ✨ UPDATED
│   ├── ActivityScreen.kt          ✨ NEW
│   ├── DashboardScreen.kt         ✨ MODIFIED
│   ├── FirewallScreen.kt          ✨ MODIFIED
│   ├── WifiScreen.kt
│   ├── components/
│   ├── theme/
│   │   ├── Spacing.kt             ✨ NEW
│   │   ├── Color.kt
│   │   └── Theme.kt
│   └── viewmodel/       ✨ UPDATED
│       ├── ActivityViewModel.kt   ✨ NEW
│       ├── DashboardViewModel.kt  ✨ MODIFIED
│       └── MainViewModel.kt       ✨ MODIFIED
│
├── util/                ✨ NEW
│   └── FormatUtils.kt             ✨ NEW
│
├── vpn/                 (VPN Service)
│   └── NetSentryVpnService.kt
│
├── NetSentryApp.kt      ✨ MODIFIED
└── MainActivity.kt

Legend:
✨ NEW      - Newly created file
✨ MODIFIED - Modified existing file
✨ UPDATED  - Directory with new/modified files
```

---

**This architecture provides:**
- ✅ Clear separation of concerns
- ✅ Reactive UI updates
- ✅ Efficient resource usage
- ✅ Easy testing and maintenance
- ✅ Scalable design patterns

**Status: Production Ready** 🚀
