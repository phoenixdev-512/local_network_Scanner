# 🧪 Testing Guide - NetSentry App

## Quick Start Testing

### **Prerequisites**
- Android device or emulator (API 24+)
- Project built successfully
- VPN permission capability on device

---

## 🎯 Feature Testing Checklist

### **1. Dashboard - Live Network Metrics** ✅

#### Test Steps:
1. Launch app → Navigate to Dashboard tab
2. Observe the "Network Speed" widget

#### Expected Results:
- ✅ "LIVE" indicator pulsing (green dot)
- ✅ Download speed updating every 0.5 seconds
- ✅ Upload speed updating every 0.5 seconds
- ✅ Ping updating every 0.5 seconds
- ✅ Smooth animated transitions between values
- ✅ Ping color changes:
  - Green if < 50ms (Excellent)
  - Yellow if < 100ms (Good)
  - Red if >= 100ms (Poor)
  - Gray if network unavailable

#### Validation:
```
1. Generate network traffic (browse web, download file)
2. Watch metrics increase in real-time
3. Stop traffic, watch metrics decrease
4. Disconnect network, verify ping shows -1 or "N/A"
```

---

### **2. Security Page - VPN Functionality** ✅

#### Test Steps:
1. Navigate to Security tab
2. Tap the large START button

#### Expected Results:
- ✅ VPN permission dialog appears (first time)
- ✅ After approval, button changes to red "STOP"
- ✅ Status changes from "INACTIVE" to "PROTECTING: Your Network"
- ✅ Scanning progress bar appears
- ✅ Progress bar fills from 0% to 100%
- ✅ "Scanning Network..." text visible during scan

#### Test Scenarios:

**Scenario A: First Time**
```
1. Tap START
2. System VPN permission dialog shows
3. Tap "OK"
4. VPN starts, status updates
5. Scanning begins automatically
```

**Scenario B: Permission Already Granted**
```
1. Tap START
2. No dialog (already authorized)
3. VPN starts immediately
4. Scanning begins
```

**Scenario C: Stop VPN**
```
1. While VPN active, tap STOP button
2. Status changes to "INACTIVE"
3. Button changes to green "START"
4. Scanning stops
5. Progress bar disappears
```

#### Validation:
```
1. Check Android VPN settings → Should show NetSentry active
2. Network traffic should route through VPN service
3. VPN icon appears in status bar
```

---

### **3. Activity Page - 5-Minute Network Stats** ✅

#### Test Steps:
1. Navigate to Activity tab
2. Observe the app list

#### Expected Results:
- ✅ "Last 5 Minutes" header with time range
- ✅ "LIVE" indicator pulsing
- ✅ Data Usage Summary card shows:
  - Total Downloaded (MB/GB)
  - Total Uploaded (MB/GB)
  - Active Apps count
- ✅ List of apps with network activity
- ✅ Each app shows:
  - App icon
  - App name
  - Connection count
  - Upload amount (green arrow)
  - Download amount (blue arrow)
- ✅ List updates every 1 second
- ✅ Apps sorted by total data usage (highest first)

#### Test Scenarios:

**Scenario A: Generate Network Activity**
```
1. Open Chrome/browser in background
2. Start downloading a file
3. Return to NetSentry → Activity tab
4. Chrome should appear at top with high download
```

**Scenario B: Refresh**
```
1. Tap refresh icon (top right)
2. Loading spinner appears briefly
3. List updates with latest data
```

**Scenario C: No Activity**
```
1. Close all apps, no network usage
2. Activity list should be empty or show minimal data
3. Total stats should show low/zero values
```

#### Validation:
```
1. Total download/upload = Sum of all app stats
2. Connection counts update in real-time
3. Byte formatting correct (B, KB, MB, GB)
```

---

### **4. Quick Actions - Navigation** ✅

#### Test Steps (From Dashboard):

**Action 1: Scan Network**
```
1. Tap "Scan Network" quick action
Expected: Navigate to Network tab
```

**Action 2: Block App**
```
1. Tap "Block App" quick action
Expected: Navigate to Security tab
```

**Action 3: View Logs**
```
1. Tap "View Logs" quick action
Expected: Navigate to Activity tab
```

#### Validation:
- ✅ All buttons functional
- ✅ Navigation smooth with animations
- ✅ No crashes or errors
- ✅ Back button returns to Dashboard

---

### **5. Visual Refinements & Animations** ✅

#### Test Areas:

**Smooth Transitions**
```
Test: Navigate between tabs rapidly
Expected:
- Fade in/out animations (300ms)
- Slide horizontal transitions
- No visual glitches
- Smooth 60fps animations
```

**Card Designs**
```
Test: Scroll through Dashboard
Expected:
- Consistent rounded corners (12-20dp)
- Proper elevation shadows
- Uniform spacing (8dp, 16dp, 24dp)
- Cards stand out from background
```

**Animated Values**
```
Test: Watch Dashboard metrics
Expected:
- Numbers smoothly transition (not jump)
- 500ms animation duration
- FastOutSlowInEasing curve
- Progress indicators animate fluidly
```

**Loading States**
```
Test: Navigate to Activity tab
Expected:
- Loading spinner appears briefly
- Fade transition to content
- No blank screens
- Shimmer/skeleton screens (if implemented)
```

---

## 🔍 Advanced Testing

### **Network Monitor Accuracy**

#### Speed Test Validation:
```
1. Run NetSentry Dashboard
2. Open Speedtest.net in parallel
3. Start a download
4. Compare results:
   - NetSentry download speed ≈ Speedtest speed
   - Values should be within 10-20% margin
```

#### Ping Test Validation:
```
1. Note NetSentry ping value
2. Run: adb shell ping 8.8.8.8
3. Compare values
Expected: NetSentry ping ≈ adb ping ±10ms
```

### **VPN Service Persistence**

#### Background Test:
```
1. Start VPN in NetSentry
2. Press Home button (minimize app)
3. Wait 5 minutes
4. Return to app
Expected:
- VPN still active
- Status still shows "PROTECTING"
- No service crashes
```

#### Reboot Test:
```
1. Start VPN
2. Reboot device
3. Open NetSentry
Expected:
- VPN not auto-started (unless setting enabled)
- Can manually restart without issues
```

### **Memory & Performance**

#### Memory Leak Test:
```
1. Navigate between tabs 50+ times rapidly
2. Check Android Profiler (Android Studio)
Expected:
- Memory usage stable (~50-100MB)
- No continuous memory growth
- Garbage collection working properly
```

#### Battery Impact:
```
1. Run VPN for 1 hour with screen off
2. Check battery stats (Settings → Battery)
Expected:
- NetSentry < 5% battery drain
- Reasonable background usage
```

---

## 🐛 Bug Reproduction

### **If App Crashes**

#### Collect Logs:
```bash
# Connect device via ADB
adb logcat | grep -i "local_network_scanner"

# Or filter for errors
adb logcat *:E | grep -i "netSentry"
```

#### Common Issues:

**Crash on VPN Start**
```
Possible Cause: VPN permission denied
Solution:
1. Clear app data
2. Reinstall app
3. Grant VPN permission when prompted
```

**Network Monitor Shows 0.00 Mbps**
```
Possible Cause: TrafficStats not available
Check:
1. Device API level >= 24
2. Network connected
3. Some traffic generated
```

**Activity Screen Empty**
```
Possible Cause: No apps using network in last 5 min
Solution:
1. Open browser, browse websites
2. Download a file
3. Wait 5 seconds, check Activity tab
```

---

## 📊 Expected Performance Metrics

### **Update Frequencies**
| Metric | Update Interval | Location |
|--------|----------------|----------|
| Download Speed | 500ms | Dashboard |
| Upload Speed | 500ms | Dashboard |
| Ping | 500ms | Dashboard |
| VPN Status | 1000ms | Security |
| Activity Stats | 1000ms | Activity |

### **Animation Durations**
| Animation | Duration | Type |
|-----------|----------|------|
| Screen Transitions | 300ms | Fade + Slide |
| Value Updates | 500ms | Animated Float |
| Progress Bar | 100ms/step | Linear |
| Pulsing Dot | 800ms | Infinite Loop |

### **Memory Usage**
| State | Expected RAM |
|-------|--------------|
| Idle (Dashboard) | ~60 MB |
| VPN Active | ~80 MB |
| All Tabs Visited | ~100 MB |

---

## ✅ Acceptance Criteria

### **Feature Complete When:**

**Dashboard**
- [x] Live metrics update every 0.5s
- [x] Animations smooth and consistent
- [x] Quick actions navigate correctly
- [x] "LIVE" indicator pulsing

**Security**
- [x] VPN starts/stops without errors
- [x] Permission handling works
- [x] Scanning progress visible
- [x] Status reflects actual state

**Activity**
- [x] Shows last 5 minutes data
- [x] Per-app breakdown accurate
- [x] Updates in real-time
- [x] Refresh works

**Visual**
- [x] Consistent spacing throughout
- [x] Smooth 60fps animations
- [x] No visual glitches
- [x] Material Design 3 compliance

---

## 🎓 Testing Tips

### **Use ADB for Network Simulation**
```bash
# Simulate slow network
adb shell settings put global bandwidth_throttle_enabled 1

# Reset network speed
adb shell settings put global bandwidth_throttle_enabled 0

# Disable WiFi
adb shell svc wifi disable

# Enable WiFi
adb shell svc wifi enable
```

### **Generate Network Traffic**
```bash
# Download large file
adb shell "wget http://ipv4.download.thinkbroadband.com/100MB.zip"

# Continuous ping
adb shell "ping -c 100 8.8.8.8"
```

### **Check VPN Status**
```bash
# List active VPN
adb shell dumpsys connectivity | grep -i vpn

# Check VPN service
adb shell ps | grep "local_network_scanner"
```

---

## 📝 Test Report Template

```
Date: __________
Tester: __________
Device: __________
Android Version: __________

DASHBOARD:
[ ] Live metrics updating (0.5s)
[ ] Animations smooth
[ ] Quick actions working
[ ] Visual consistency

SECURITY:
[ ] VPN starts successfully
[ ] VPN stops successfully
[ ] Permission handling correct
[ ] Scanning progress visible

ACTIVITY:
[ ] 5-minute stats showing
[ ] App list accurate
[ ] Real-time updates
[ ] Refresh working

OVERALL:
[ ] No crashes
[ ] Performance acceptable
[ ] Battery usage reasonable
[ ] Memory usage normal

BUGS FOUND:
1. ________________________
2. ________________________
3. ________________________

NOTES:
_______________________________
_______________________________
```

---

## 🚀 Ready for Production?

### **Checklist**
- [ ] All features tested
- [ ] No critical bugs
- [ ] Performance acceptable
- [ ] Battery usage reasonable
- [ ] Permissions granted properly
- [ ] UI smooth and responsive
- [ ] Real data (not mock) integrated
- [ ] Error handling robust

**If all checked, ready to ship! 🎉**

---

## 📞 Support

### **Issues?**
1. Check logs: `adb logcat`
2. Review `IMPLEMENTATION_COMPLETE.md`
3. Verify all files created/modified
4. Rebuild project: `./gradlew clean build`
5. Clear app data and reinstall

**Happy Testing! 🧪✨**
