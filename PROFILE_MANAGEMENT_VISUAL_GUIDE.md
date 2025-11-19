# Profile Management System - Visual Guide

## Screen Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    Navigation Drawer (Swipe Right)               │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ NetSentry Logo                                            │  │
│  │ Network Security Monitor                                  │  │
│  ├───────────────────────────────────────────────────────────┤  │
│  │ 👥 Profile Management          ← OPENS PROFILE MGMT       │  │
│  │ 📡 Network Manager                                        │  │
│  │ ⚙️  Settings & Preferences                                │  │
│  │ ❓ Help & Documentation                                   │  │
│  │ ℹ️  About                                                  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│              Profile Management Screen                          │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ ← Profile Management                                      │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  [Avatar]  John Doe          [ADMIN]                 ⋮   │  │ ← Active
│  │            john@example.com                               │  │   (Highlighted)
│  │            Last active: 5 min ago                         │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  [Avatar]  Jane Smith        [USER]                  ⋮   │  │ ← Inactive
│  │            jane@example.com                               │  │   (Tap to switch)
│  │            Last active: 2 hours ago                       │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│                                                          [+]    │ ← FAB (Create)
└─────────────────────────────────────────────────────────────────┘
```

## Empty State

```
┌─────────────────────────────────────────────────────────────────┐
│              Profile Management Screen                          │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ ← Profile Management                                      │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│                          👤                                     │
│                    (Large Icon)                                 │
│                                                                 │
│                   No profiles yet                               │
│           Create your first profile to get started              │
│                                                                 │
│              ┌─────────────────────────┐                        │
│              │  + Create Profile       │                        │
│              └─────────────────────────┘                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Profile Card Details

```
┌─────────────────────────────────────────────────────────────────┐
│  ┌─────────┐                                                    │
│  │  [IMG]  │  John Doe                    ┌───────────┐    ⋮   │
│  │ Avatar  │                              │   ADMIN   │         │
│  │ 64x64   │  john@example.com            └───────────┘         │
│  └─────────┘                                                    │
│               Last active: 5 min ago                            │
│                                                                 │
│  Border: ElectricBlue (if active)                               │
│  Background: Tinted (if active)                                 │
└─────────────────────────────────────────────────────────────────┘
                                                              ↓
                                                    ┌──────────────┐
                                                    │  Edit        │
                                                    │  Delete      │
                                                    └──────────────┘
```

## Profile Form Dialog (Create/Edit)

```
┌─────────────────────────────────────────────────────────────────┐
│  X  Create Profile                                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                    ┌──────────────┐                             │
│                    │              │                             │
│                    │   [Avatar]   │  📷 (Camera overlay)        │
│                    │    120x120   │                             │
│                    └──────────────┘                             │
│                   📸 Change Photo                               │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ Full Name                                                 │  │
│  │ John Doe                                            28/30 │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ Email Address                                             │  │
│  │ john@example.com                                          │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ User Role                                              ▼  │  │
│  │ Administrator                                             │  │
│  └───────────────────────────────────────────────────────────┘  │
│  Administrators have full access to all features                │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ Profile Preferences                                    ▼  │  │
│  ├───────────────────────────────────────────────────────────┤  │
│  │ Auto-start VPN                              [ON]          │  │
│  │ Start VPN on profile switch                               │  │
│  │                                                           │  │
│  │ Enable Notifications                        [ON]          │  │
│  │ Receive alerts and updates                                │  │
│  │                                                           │  │
│  │ ┌─────────────────────────────────────────────────────┐   │  │
│  │ │ Custom DNS Server (Optional)                        │   │  │
│  │ │ e.g., 8.8.8.8                                       │   │  │
│  │ └─────────────────────────────────────────────────────┘   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────┐           ┌──────────────────┐            │
│  │     Cancel       │           │  Save Profile    │            │
│  └──────────────────┘           └──────────────────┘            │
└─────────────────────────────────────────────────────────────────┘
```

## Delete Confirmation Dialog

```
                    ┌──────────────────────────────────┐
                    │  Delete Profile                  │
                    ├──────────────────────────────────┤
                    │                                  │
                    │  Delete John Doe?                │
                    │  This action cannot be undone.   │
                    │                                  │
                    ├──────────────────────────────────┤
                    │  [Cancel]         [Delete] ←RED  │
                    └──────────────────────────────────┘
```

## Component Hierarchy

```
ProfileManagementScreen
├── TopAppBar
│   ├── NavigationIcon (Back arrow)
│   └── Title ("Profile Management")
├── Scaffold Content
│   ├── EmptyProfilesState (if no profiles)
│   │   ├── Icon (PersonAdd)
│   │   ├── Text ("No profiles yet")
│   │   └── Button ("Create Profile")
│   │
│   └── LazyColumn (if profiles exist)
│       └── ProfileCard (for each profile)
│           ├── AsyncImage (Avatar)
│           ├── Column (Profile Info)
│           │   ├── Row
│           │   │   ├── Text (Name)
│           │   │   └── RoleBadge
│           │   ├── Text (Email)
│           │   └── Text (Last active)
│           └── IconButton (More menu)
│               └── DropdownMenu
│                   ├── Edit
│                   └── Delete (if not active)
├── FloatingActionButton (Create profile)
└── Loading Overlay (if loading)

ProfileFormDialog
├── TopAppBar
│   ├── NavigationIcon (Close)
│   └── Title ("Create/Edit Profile")
├── Scrollable Content
│   ├── Avatar Section
│   │   ├── Avatar Preview (clickable)
│   │   └── Change Photo Button
│   ├── Form Fields
│   │   ├── Name TextField (with counter)
│   │   ├── Email TextField (with validation)
│   │   └── Role Dropdown
│   ├── Preferences Card (expandable)
│   │   ├── Auto-start VPN Switch
│   │   ├── Notifications Switch
│   │   └── Custom DNS TextField
│   └── Action Buttons
│       ├── Cancel Button
│       └── Save Button
```

## State Flow Diagram

```
┌─────────────┐
│    Idle     │ ← Initial state
└──────┬──────┘
       │
       ↓ User action (create/edit/delete/switch)
┌─────────────┐
│   Loading   │ ← Show spinner
└──────┬──────┘
       │
       ├─→ Success ─→ Toast message → Idle
       │              Close dialog
       │              Refresh list
       │
       └─→ Error ───→ Toast error → Idle
                      Keep dialog open
```

## Data Flow

```
UI Layer (Compose)
    ↓ User Input
ProfileViewModel
    ↓ Validation
    ↓ Business Logic
ProfileRepository
    ↓ Data Operations
UserProfileDao (Room)
    ↓
SQLite Database
    ↑
    │ Flow<List<UserProfile>>
    │
ProfileViewModel
    ↑
    │ StateFlow
    │
UI Layer (Compose)
```

## Color Scheme

```
┌────────────────────────────────────────┐
│ Background: DeepNavy (#0A1929)         │ ← Main background
├────────────────────────────────────────┤
│ Surface: SurfaceDarkGray (#1A2332)    │ ← Cards, dialogs
├────────────────────────────────────────┤
│ Primary: ElectricBlue (#00D9FF)       │ ← Buttons, highlights
├────────────────────────────────────────┤
│ Text Primary: #FFFFFF                  │ ← Main text
│ Text Secondary: #94A3B8               │ ← Secondary text
│ Text Tertiary: #64748B                │ ← Tertiary text
├────────────────────────────────────────┤
│ Admin Badge: Red (#D32F2F)            │ ← Admin role
│ User Badge: Blue (#1E88E5)            │ ← Standard role
└────────────────────────────────────────┘
```

## Typography Scale

```
┌─────────────────────────────────────────────┐
│ Screen Title: headlineMedium, Bold         │
│ Profile Name: titleMedium, Bold            │
│ Email: bodySmall, Secondary                │
│ Last Active: bodySmall, Tertiary           │
│ Badge: labelSmall, Bold                    │
│ Button Text: labelLarge                    │
│ Form Label: bodyMedium                     │
│ Helper Text: bodySmall                     │
└─────────────────────────────────────────────┘
```

## Icon Usage

```
┌──────────────────────────────────────────────────┐
│ SupervisorAccount → Profile Management Menu     │
│ Add → Create Profile (FAB)                      │
│ ArrowBack → Navigation back                     │
│ MoreVert → Profile actions menu                 │
│ Edit → Edit profile action                      │
│ Delete → Delete profile action                  │
│ Close → Close dialog                            │
│ CameraAlt → Camera overlay on avatar            │
│ PhotoCamera → Change photo button               │
│ PersonAdd → Empty state icon                    │
│ Person → Default avatar                         │
│ AdminPanelSettings → Admin badge (alt)          │
│ ExpandMore/Less → Expand preferences            │
└──────────────────────────────────────────────────┘
```

## Interaction Patterns

### Tap Interactions
```
Profile Card (Inactive) → Switch Profile
Profile Card (Active) → No action
FAB → Open Create Dialog
Menu Icon → Show dropdown
Edit → Open Edit Dialog
Delete → Show confirmation
Avatar in Dialog → Open image picker
Cancel Button → Close dialog
Save Button → Submit form
```

### Long Press
```
Not implemented (future: bulk selection)
```

### Swipe Gestures
```
Not implemented (future: swipe to delete)
```

## Validation Rules

### Name Field
```
✓ Required
✓ Max 30 characters
✓ Letters, spaces, hyphens only
✗ Special characters
✗ Numbers
✗ Empty
```

### Email Field
```
✓ Required
✓ Valid email format
✓ Contains @ symbol
✓ Has domain extension
✗ Empty
✗ Invalid format
```

## Error Messages

```
Name Errors:
- "Name is required"
- "Only letters, spaces, and hyphens allowed"

Email Errors:
- "Email is required"
- "Invalid email format"

Operation Errors:
- "Failed to create profile: [reason]"
- "Failed to update profile: [reason]"
- "Failed to delete profile: [reason]"
- "Cannot delete active profile"
- "Profile not found"
```

## Success Messages

```
- "Profile created successfully"
- "Profile updated successfully"
- "Profile deleted successfully"
- "Profile switched successfully"
```

## Responsive Design

```
┌─────────────────────────────────────┐
│ Phone Portrait (360dp)              │
│ - Single column                     │
│ - Full-width cards                  │
│ - Full-screen dialog                │
└─────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│ Tablet Portrait (600dp+)                     │
│ - Single column                              │
│ - Max width cards with padding               │
│ - Centered dialog (not full-screen)          │
└──────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────┐
│ Tablet Landscape (900dp+)                             │
│ - Two columns for profile list                        │
│ - Side-by-side layout option                          │
│ - Dialog max width 600dp                              │
└───────────────────────────────────────────────────────┘
```

## Accessibility

```
✓ Content descriptions on all icons
✓ Semantic roles (Button, Card, Dialog)
✓ Touch targets ≥ 48dp
✓ Color contrast ratio ≥ 4.5:1
✓ Support for screen readers
✓ Focus indicators
✓ Keyboard navigation support
```

## Performance Metrics

```
┌───────────────────────────────────────┐
│ Profile List Load: < 100ms            │
│ Database Query: < 50ms                │
│ Image Load (Cached): < 10ms           │
│ Image Load (First): < 200ms           │
│ Form Validation: < 5ms                │
│ Profile Switch: < 100ms               │
│ Dialog Open/Close: 400ms animation    │
└───────────────────────────────────────┘
```
