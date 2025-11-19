# Profile Management System - Testing Guide

## Manual Testing Checklist

### 1. Profile Creation Flow

#### Test Case 1.1: Create Profile with All Fields
**Steps:**
1. Open app and swipe right to open navigation drawer
2. Tap "Profile Management"
3. Tap the floating action button (+ icon)
4. Enter name: "Test User"
5. Enter email: "test@example.com"
6. Select role: "Standard User"
7. Tap "Change Photo" and select an image
8. Expand "Profile Preferences"
9. Enable "Auto-start VPN"
10. Enable "Enable Notifications"
11. Enter custom DNS: "8.8.8.8"
12. Tap "Save Profile"

**Expected:**
- Profile created successfully
- Toast: "Profile created successfully"
- Dialog closes
- New profile appears in list
- Avatar is displayed

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 1.2: Create Profile with Minimum Fields
**Steps:**
1. Tap FAB
2. Enter name: "Minimum User"
3. Enter email: "min@test.com"
4. Tap "Save Profile"

**Expected:**
- Profile created with defaults
- Role: Standard User
- Auto-start VPN: OFF
- Notifications: ON
- Custom DNS: Empty

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 1.3: Validation - Empty Name
**Steps:**
1. Tap FAB
2. Leave name empty
3. Enter email: "test@example.com"
4. Attempt to tap "Save Profile"

**Expected:**
- Save button is disabled
- Error message: "Name is required"

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 1.4: Validation - Invalid Email
**Steps:**
1. Tap FAB
2. Enter name: "Test"
3. Enter email: "invalid-email"
4. Attempt to tap "Save Profile"

**Expected:**
- Save button is disabled
- Error message: "Invalid email format"

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 1.5: Validation - Name Too Long
**Steps:**
1. Tap FAB
2. Enter name: "A" repeated 31 times
3. Verify character counter

**Expected:**
- Only 30 characters allowed
- Counter shows "30/30"
- Additional characters not accepted

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 1.6: Validation - Special Characters in Name
**Steps:**
1. Tap FAB
2. Enter name: "Test@User#123"
3. Check for error

**Expected:**
- Error: "Only letters, spaces, and hyphens allowed"

**Status:** ☐ Pass ☐ Fail

---

### 2. Profile Editing Flow

#### Test Case 2.1: Edit Existing Profile
**Steps:**
1. Tap three-dot menu on a profile
2. Select "Edit"
3. Change name to "Updated Name"
4. Change email to "updated@example.com"
5. Change role to "Administrator"
6. Tap "Save Profile"

**Expected:**
- Profile updated successfully
- Changes reflected in list
- Toast: "Profile updated successfully"

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 2.2: Edit Profile Avatar
**Steps:**
1. Edit a profile
2. Tap avatar or "Change Photo"
3. Select new image
4. Save profile

**Expected:**
- New avatar displayed
- Old avatar file deleted (if different)

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 2.3: Cancel Edit
**Steps:**
1. Edit a profile
2. Make changes
3. Tap "Cancel"

**Expected:**
- Dialog closes
- No changes saved
- Original data remains

**Status:** ☐ Pass ☐ Fail

---

### 3. Profile Deletion Flow

#### Test Case 3.1: Delete Inactive Profile
**Steps:**
1. Create two profiles
2. Make one active
3. Tap menu on inactive profile
4. Select "Delete"
5. Confirm deletion

**Expected:**
- Confirmation dialog shown
- Profile deleted after confirmation
- Avatar file deleted
- Toast: "Profile deleted successfully"

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 3.2: Cannot Delete Active Profile
**Steps:**
1. Tap menu on active profile
2. Check menu options

**Expected:**
- "Delete" option not shown
- Only "Edit" option available

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 3.3: Cancel Deletion
**Steps:**
1. Request to delete profile
2. Tap "Cancel" on confirmation dialog

**Expected:**
- Dialog closes
- Profile not deleted
- Profile still in list

**Status:** ☐ Pass ☐ Fail

---

### 4. Profile Switching Flow

#### Test Case 4.1: Switch to Different Profile
**Steps:**
1. Create two profiles
2. Tap on inactive profile card

**Expected:**
- Profile becomes active
- Previous profile deactivated
- Active profile highlighted with border
- Toast: "Profile switched successfully"
- Last active timestamp updated

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 4.2: Tap Active Profile
**Steps:**
1. Tap on currently active profile

**Expected:**
- No action
- No dialog shown
- Profile remains active

**Status:** ☐ Pass ☐ Fail

---

### 5. Empty State

#### Test Case 5.1: Display Empty State
**Steps:**
1. Delete all profiles (or start fresh)
2. Navigate to Profile Management

**Expected:**
- Empty state shown
- Icon: PersonAdd
- Text: "No profiles yet"
- Button: "Create Profile"

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 5.2: Create from Empty State
**Steps:**
1. From empty state, tap "Create Profile"

**Expected:**
- Dialog opens
- Can create profile normally
- Empty state disappears after creation

**Status:** ☐ Pass ☐ Fail

---

### 6. UI/UX Tests

#### Test Case 6.1: Loading State
**Steps:**
1. Create/edit/delete a profile
2. Observe loading indicator

**Expected:**
- Loading overlay shown during operation
- Spinner displayed
- UI disabled during loading
- Overlay disappears after completion

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 6.2: Error Handling
**Steps:**
1. Simulate database error (if possible)
2. Attempt profile operation

**Expected:**
- Error toast shown
- User-friendly error message
- UI returns to previous state
- No crash

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 6.3: Profile List Ordering
**Steps:**
1. Create multiple profiles at different times
2. Switch between profiles
3. Check list order

**Expected:**
- Profiles ordered by last active (most recent first)
- Active profile maintains position

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 6.4: Relative Time Display
**Steps:**
1. Create profile
2. Wait 2 minutes
3. Check "Last active" text

**Expected:**
- Shows "Just now" initially
- Updates to "2 min ago" after 2 minutes
- Shows "X hours ago" after hours
- Shows "X days ago" after days

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 6.5: Role Badge Display
**Steps:**
1. Create admin profile
2. Create standard profile
3. Check badge colors

**Expected:**
- Admin: Red badge with "ADMIN" text
- Standard: Blue badge with "USER" text
- Correct styling and colors

**Status:** ☐ Pass ☐ Fail

---

### 7. Navigation Tests

#### Test Case 7.1: Navigate from Drawer
**Steps:**
1. Swipe right to open drawer
2. Tap "Profile Management"

**Expected:**
- Drawer closes
- Profile Management screen opens
- Slide transition animation plays

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 7.2: Navigate Back
**Steps:**
1. From Profile Management screen
2. Tap back arrow

**Expected:**
- Returns to previous screen
- Navigation animation plays

**Status:** ☐ Pass ☐ Fail

---

### 8. Avatar/Image Tests

#### Test Case 8.1: Select Avatar from Gallery
**Steps:**
1. Create/edit profile
2. Tap "Change Photo"
3. Select image from gallery

**Expected:**
- Image picker opens
- Selected image shown in preview
- Image saved to internal storage

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 8.2: Default Avatar Display
**Steps:**
1. Create profile without selecting avatar
2. Check avatar display

**Expected:**
- Default avatar icon shown
- Person silhouette icon
- Proper sizing (64dp in list, 120dp in dialog)

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 8.3: Avatar Persistence
**Steps:**
1. Create profile with avatar
2. Close app
3. Reopen app
4. View profile

**Expected:**
- Avatar still displayed
- Image loaded from storage
- No broken image

**Status:** ☐ Pass ☐ Fail

---

### 9. Form Behavior Tests

#### Test Case 9.1: Preferences Expand/Collapse
**Steps:**
1. Open create/edit dialog
2. Tap "Profile Preferences"
3. Tap again

**Expected:**
- Section expands to show options
- Section collapses when tapped again
- Smooth animation

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 9.2: Toggle Switches
**Steps:**
1. Open preferences section
2. Toggle "Auto-start VPN"
3. Toggle "Enable Notifications"

**Expected:**
- Switches animate ON/OFF
- Blue color when ON
- State saved when profile saved

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 9.3: Role Dropdown
**Steps:**
1. Open create/edit dialog
2. Tap "User Role" field
3. Select different role

**Expected:**
- Dropdown menu appears
- Shows "Standard User" and "Administrator"
- Selection updates field
- Dropdown closes after selection

**Status:** ☐ Pass ☐ Fail

---

### 10. Edge Cases

#### Test Case 10.1: Create Many Profiles
**Steps:**
1. Create 10+ profiles
2. Scroll through list

**Expected:**
- List scrolls smoothly
- All profiles displayed
- No performance issues

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 10.2: Very Long Name (Max Length)
**Steps:**
1. Create profile with 30-character name
2. View in list

**Expected:**
- Name displays properly
- No text overflow
- Readable in card layout

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 10.3: Very Long Email
**Steps:**
1. Create profile with long email
2. View in list

**Expected:**
- Email truncates or wraps properly
- Card layout not broken

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 10.4: Special Characters in Email
**Steps:**
1. Enter email: "test+alias@example.com"
2. Save profile

**Expected:**
- Email accepted (valid format)
- Profile created successfully

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 10.5: Rapid Button Clicks
**Steps:**
1. Open dialog
2. Rapidly tap "Save Profile" multiple times

**Expected:**
- Only one profile created
- No duplicate submissions
- Loading state prevents multiple clicks

**Status:** ☐ Pass ☐ Fail

---

### 11. Persistence Tests

#### Test Case 11.1: Data Persistence
**Steps:**
1. Create multiple profiles
2. Force close app
3. Reopen app
4. Navigate to Profile Management

**Expected:**
- All profiles still exist
- Data intact
- Active profile remembered

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 11.2: Active Profile Persistence
**Steps:**
1. Switch to different profile
2. Close app
3. Reopen app

**Expected:**
- Same profile still active
- Active state persisted

**Status:** ☐ Pass ☐ Fail

---

### 12. Accessibility Tests

#### Test Case 12.1: Screen Reader Support
**Steps:**
1. Enable TalkBack
2. Navigate through profile list
3. Interact with buttons

**Expected:**
- All elements have content descriptions
- Proper focus order
- Actions announced correctly

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 12.2: Touch Target Size
**Steps:**
1. Measure touch targets
2. Verify minimum 48dp

**Expected:**
- All buttons ≥ 48dp
- FAB ≥ 56dp
- IconButtons ≥ 48dp

**Status:** ☐ Pass ☐ Fail

---

#### Test Case 12.3: Color Contrast
**Steps:**
1. Check text readability
2. Verify contrast ratios

**Expected:**
- All text meets WCAG AA standards
- Contrast ratio ≥ 4.5:1

**Status:** ☐ Pass ☐ Fail

---

## Automated Testing

### Unit Tests

```kotlin
// ProfileRepository Tests
class ProfileRepositoryTest {
    @Test
    fun `create profile returns success`() { }
    
    @Test
    fun `update profile returns success`() { }
    
    @Test
    fun `delete active profile returns failure`() { }
    
    @Test
    fun `switch profile updates active state`() { }
}

// ProfileViewModel Tests
class ProfileViewModelTest {
    @Test
    fun `validate email returns true for valid email`() { }
    
    @Test
    fun `validate email returns false for invalid email`() { }
    
    @Test
    fun `create profile with empty name shows error`() { }
    
    @Test
    fun `profile creation updates UI state to success`() { }
    
    @Test
    fun `profile deletion shows confirmation dialog`() { }
}

// ImageStorageService Tests
class ImageStorageServiceTest {
    @Test
    fun `save image creates file in correct directory`() { }
    
    @Test
    fun `delete image removes file from storage`() { }
    
    @Test
    fun `image exists returns true for existing file`() { }
}
```

### Integration Tests

```kotlin
// Database Tests
class ProfileDaoTest {
    @Test
    fun `insert and retrieve profile`() { }
    
    @Test
    fun `get active profile returns only active one`() { }
    
    @Test
    fun `deactivate all profiles sets isActive to false`() { }
    
    @Test
    fun `profiles ordered by lastActiveAt`() { }
}

// End-to-End Tests
class ProfileManagementE2ETest {
    @Test
    fun `complete profile creation flow`() { }
    
    @Test
    fun `complete profile edit flow`() { }
    
    @Test
    fun `complete profile deletion flow`() { }
    
    @Test
    fun `complete profile switch flow`() { }
}
```

### UI Tests

```kotlin
// Compose UI Tests
class ProfileManagementScreenTest {
    @Test
    fun `empty state shown when no profiles`() { }
    
    @Test
    fun `FAB opens create dialog`() { }
    
    @Test
    fun `profile card displays all information`() { }
    
    @Test
    fun `edit menu opens on menu button click`() { }
    
    @Test
    fun `delete confirmation dialog appears`() { }
}

class ProfileFormDialogTest {
    @Test
    fun `form validation shows errors`() { }
    
    @Test
    fun `save button disabled when invalid`() { }
    
    @Test
    fun `preferences section expands and collapses`() { }
    
    @Test
    fun `image picker launches on avatar tap`() { }
}
```

## Performance Testing

### Test Case P.1: List Scrolling Performance
**Steps:**
1. Create 100 profiles
2. Scroll through list rapidly
3. Monitor frame rate

**Expected:**
- Smooth 60fps scrolling
- No jank or stuttering
- LazyColumn efficient loading

**Status:** ☐ Pass ☐ Fail

---

### Test Case P.2: Image Loading Performance
**Steps:**
1. Create profiles with avatars
2. Scroll through list
3. Monitor image loading

**Expected:**
- Images cached properly
- No UI blocking
- Smooth thumbnail display

**Status:** ☐ Pass ☐ Fail

---

### Test Case P.3: Database Query Performance
**Steps:**
1. Create 100 profiles
2. Measure query time

**Expected:**
- getAllProfiles() < 50ms
- getActiveProfile() < 10ms
- Room optimizations working

**Status:** ☐ Pass ☐ Fail

---

## Security Testing

### Test Case S.1: SQL Injection Prevention
**Steps:**
1. Enter name: "'; DROP TABLE user_profiles; --"
2. Save profile

**Expected:**
- Input sanitized
- No SQL injection
- Profile created safely

**Status:** ☐ Pass ☐ Fail

---

### Test Case S.2: File Access Control
**Steps:**
1. Check avatar storage location
2. Verify permissions

**Expected:**
- Files in app-private directory
- Other apps cannot access
- Proper file permissions

**Status:** ☐ Pass ☐ Fail

---

## Regression Testing

After bug fixes or updates, re-run all test cases to ensure no regressions.

## Test Environment

- **Device:** [Specify device model]
- **Android Version:** [Specify OS version]
- **App Version:** 1.0
- **Test Date:** [Date]
- **Tester:** [Name]

## Bug Report Template

```
Bug ID: PM-XXX
Title: [Brief description]
Severity: Critical | High | Medium | Low
Priority: P1 | P2 | P3 | P4

Steps to Reproduce:
1. 
2. 
3. 

Expected Behavior:


Actual Behavior:


Screenshots/Logs:


Device Info:
- Model:
- Android Version:
- App Version:

Additional Notes:
```

## Test Summary Report Template

```
Test Summary Report
===================
Date: [Date]
Tester: [Name]

Total Test Cases: XX
Passed: XX
Failed: XX
Blocked: XX
Not Tested: XX

Pass Rate: XX%

Critical Issues: X
High Priority: X
Medium Priority: X
Low Priority: X

Notes:
- 
- 
```
