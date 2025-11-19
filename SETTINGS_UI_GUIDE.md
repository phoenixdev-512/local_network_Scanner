# Settings Screen Visual Guide

## Screen Layout

```
┌─────────────────────────────────────────┐
│ ← Settings                              │ Top Bar (Transparent)
├─────────────────────────────────────────┤
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ 🎨 Appearance                     ║ │ Section 1
│  ║                                   ║ │
│  ║ Theme                     system ▼ ║ │
│  ║ App color theme                   ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Accent Color               ⬤ Blue ║ │
│  ║ Primary theme color               ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Font Size                    100% ║ │
│  ║ Adjust text size                  ║ │
│  ║ ──────●──────────────────         ║ │ Slider (80-150%)
│  ║ ───────────────────────────────── ║ │
│  ║ Card Style               elevated ▼ ║ │
│  ║ UI card appearance                ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Icon Style                 filled ▼ ║ │
│  ║ Icon appearance                   ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Animation Intensity        medium ▼ ║ │
│  ║ UI animation level                ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ 📡 Network & VPN                  ║ │ Section 2
│  ║                                   ║ │
│  ║ Auto-start VPN            ○ OFF   ║ │
│  ║ Start VPN when device boots       ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Auto-connect Trusted Networks     ║ │
│  ║ Connect to known safe networks ○  ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Default DNS Provider  Cloudflare ▼ ║ │
│  ║ DNS server selection              ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ VPN Protocol             OpenVPN ▼ ║ │
│  ║ Connection protocol               ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Kill Switch               ○ OFF   ║ │
│  ║ Block internet if VPN disconnects ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Network Preference          Auto ▼ ║ │
│  ║ Preferred connection type         ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ 🔒 Security & Privacy             ║ │ Section 3
│  ║                                   ║ │
│  ║ Auto-scan Frequency               ║ │
│  ║ Security scan schedule            ║ │
│  ║                On Connection... ▼  ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Threat Sensitivity        Medium ▼ ║ │
│  ║ Detection sensitivity level       ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Automatic Threat Blocking         ║ │
│  ║ Block detected threats auto...  ○ ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Send Anonymous Usage Statistics   ║ │
│  ║ Help improve the app            ○ ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Crash Reporting                 ○ ║ │
│  ║ Send crash reports to developers  ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Telemetry                       ○ ║ │
│  ║ Share app usage data              ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ 🔔 Notifications                  ║ │ Section 4
│  ║                                   ║ │
│  ║ Enable Notifications            ● ║ │ Master Toggle
│  ║ Master notification toggle        ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Threat Alerts                   ● ║ │ Enabled
│  ║ Get notified of security threats  ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Alert Priority           Default ▼ ║ │
│  ║ Notification importance level     ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Connection Logs                 ○ ║ │
│  ║ Notify on new connections         ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Speed Test Reminders            ○ ║ │
│  ║ Regular speed test prompts        ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Weekly Summary                  ○ ║ │
│  ║ Receive weekly usage reports      ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Vibration                       ● ║ │
│  ║ Vibrate on notifications          ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ 📊 Data Usage                     ║ │ Section 5
│  ║                                   ║ │
│  ║ Data Saver Mode                 ○ ║ │
│  ║ Reduce data consumption           ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Update Frequency      Every 5s  ▼ ║ │ (Disabled)
│  ║ Data refresh interval             ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Background Data Restriction     ○ ║ │
│  ║ Limit background data usage       ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ WiFi Only Sync                  ○ ║ │
│  ║ Sync only on WiFi                 ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Metered Connection Warning      ● ║ │
│  ║ Warn on metered networks          ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ ⚙️ Advanced                       ║ │ Section 6
│  ║                                   ║ │
│  ║ Network Speed Unit         Mbps ▼ ║ │
│  ║ Speed display format              ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Data Usage Unit            Auto ▼ ║ │
│  ║ Usage display format              ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Debug Logging (Admin)           ○ ║ │ Admin Only
│  ║ Enable detailed logs              ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Log Level (Admin)          Error ▼ ║ │ Admin Only
│  ║ Logging verbosity                 ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Export Logs                     › ║ │
│  ║ Save app logs to file             ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Export Configuration            › ║ │
│  ║ Backup settings as JSON           ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Import Configuration            › ║ │
│  ║ Restore from backup               ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Clear All Data                  › ║ │ Red Text
│  ║ Delete all app data               ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Reset to Defaults               › ║ │ Red Text
│  ║ Restore default settings          ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
│  ╔═══════════════════════════════════╗ │
│  ║ ℹ️ About & Legal                  ║ │ Section 7
│  ║                                   ║ │
│  ║ Version                     1.0.0 ║ │ Read-only
│  ║ ───────────────────────────────── ║ │
│  ║ Check for Updates               › ║ │
│  ║ Visit Google Play Store           ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Privacy Policy                  › ║ │
│  ║ Read our privacy policy           ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Terms of Service                › ║ │
│  ║ Read our terms                    ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Contact Developer               › ║ │
│  ║ Send email to developer           ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ GitHub Repository               › ║ │
│  ║ View source code                  ║ │
│  ║ ───────────────────────────────── ║ │
│  ║ Rate on Play Store              › ║ │
│  ║ Leave a review                    ║ │
│  ╚═══════════════════════════════════╝ │
│                                         │
└─────────────────────────────────────────┘
```

## Dialog Examples

### Color Picker Dialog
```
┌─────────────────────────────────┐
│  Select Accent Color            │
│                                 │
│  ⬤ Blue    ⬤ Red                │
│  ✓ (selected with white border) │
│                                 │
│  ⬤ Green   ⬤ Orange             │
│                                 │
│  ⬤ Purple  ⬤ Yellow             │
│                                 │
│  ⬤ Cyan    ⬤ Pink               │
│                                 │
│  ┌─────────────────────────┐    │
│  │       Cancel            │    │
│  └─────────────────────────┘    │
└─────────────────────────────────┘
```

### Custom DNS Dialog
```
┌─────────────────────────────────┐
│  Custom DNS Servers             │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Primary DNS *             │  │
│  │ 1.1.1.1                   │  │
│  └───────────────────────────┘  │
│                                 │
│  ┌───────────────────────────┐  │
│  │ Secondary DNS (Optional)  │  │
│  │ 1.0.0.1                   │  │
│  └───────────────────────────┘  │
│                                 │
│  Popular DNS Providers:         │
│  • Cloudflare: 1.1.1.1 / 1.0... │
│  • Google: 8.8.8.8 / 8.8.4.4    │
│  • Quad9: 9.9.9.9 / 149.112...  │
│                                 │
│  ┌────────┐  ┌────────┐         │
│  │ Cancel │  │  Save  │         │
│  └────────┘  └────────┘         │
└─────────────────────────────────┘
```

### Reset Confirmation Dialog
```
┌─────────────────────────────────┐
│  ⚠️ Reset Settings?              │
│                                 │
│  This will restore all          │
│  settings to their default      │
│  values. This action cannot     │
│  be undone.                     │
│                                 │
│  ┌────────┐  ┌────────┐         │
│  │ Cancel │  │ Reset  │         │ (Orange)
│  └────────┘  └────────┘         │
└─────────────────────────────────┘
```

### Clear Data Confirmation Dialog
```
┌─────────────────────────────────┐
│      ⚠️ (Large Warning Icon)     │
│                                 │
│  Clear All Data?                │ (Red)
│                                 │
│  This will delete ALL app       │
│  data including profiles,       │
│  logs, and settings. This       │
│  action cannot be undone.       │
│                                 │
│  ┌────────┐  ┌───────────┐      │
│  │ Cancel │  │Clear Data │      │ (Red)
│  └────────┘  └───────────┘      │
└─────────────────────────────────┘
```

## Loading Overlay
```
┌─────────────────────────────────┐
│                                 │
│     (Semi-transparent black)    │
│                                 │
│            ⟳ Loading...         │ (Blue spinner)
│                                 │
│                                 │
└─────────────────────────────────┘
```

## Color Scheme

- **Background**: Gradient (DeepNavy → GradientMiddle → TrueBlack)
- **Cards**: SurfaceDarkGray with rounded corners (20dp)
- **Primary Text**: TextPrimary (White/Light Gray)
- **Secondary Text**: TextSecondary (Medium Gray)
- **Tertiary Text**: TextTertiary (Dark Gray)
- **Accent**: ElectricBlue
- **Success**: VibrantGreen
- **Warning**: WarningOrange
- **Dividers**: CardBackground

## Typography

- **Section Titles**: TitleLarge, Bold, Primary Color
- **Setting Titles**: BodyLarge, Primary Text
- **Setting Subtitles**: BodySmall, Tertiary Text
- **Values**: BodyMedium, Electric Blue
- **Info**: BodyMedium, Secondary Text

## Interactions

### Toggle Switch
- **Off**: Gray thumb, gray track
- **On**: Green thumb, semi-transparent green track
- **Disabled**: Semi-transparent versions

### Dropdown
- **Closed**: Value on right, down arrow
- **Open**: Value on right, up arrow, menu appears
- **Selected**: Blue text in menu

### Slider
- **Thumb**: Electric Blue circle
- **Active Track**: Electric Blue
- **Inactive Track**: Card Background
- **Value**: Percentage displayed on right

### Clickable Items
- **Normal**: Chevron right icon
- **Warning**: Red text, chevron right
- **Ripple**: On click/tap

### Color Picker
- **Current**: Circular preview with border
- **Selected in Dialog**: White border + checkmark
- **Others**: No border

## State Management

### Persistence
- All settings saved to DataStore immediately on change
- No explicit save button needed
- Changes survive app restart

### Validation
- Custom DNS: IP regex validation before saving
- Required fields: Primary DNS must be valid
- Optional fields: Secondary DNS can be empty

### Dependencies
- Master toggle (Notifications) disables dependent toggles
- Data Saver enables Update Frequency dropdown
- Debug Logging enables Log Level dropdown
- Admin role shows/hides admin-only settings

### Error States
- Invalid IP: Red border, error text below field
- Failed export: Error message via Toast
- Failed import: Error message via Toast
- Network error: Error message via Toast

## Accessibility

- All interactive elements have proper hit targets
- Color contrast meets WCAG AA standards
- Screen reader support via content descriptions
- Keyboard navigation support
- Focus indicators visible

## Responsive Design

- LazyColumn for scrolling content
- Cards fill width with 16dp horizontal padding
- Proper spacing between sections (16dp)
- Dividers between settings within sections
- Proper padding inside cards (20dp)
