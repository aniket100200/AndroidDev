# Android Floating Auto-Clicker

A user-controlled, personal accessibility auto-clicker application built natively using **Java and XML layouts**. It displays a movable floating overlay over authorized target applications and performs a finite sequence of tap gestures using Android's standard `AccessibilityService` APIs.

## Features

- **Movable Overlay Control**: Compact floating panel displaying target coordinates, tap progress, Start, Stop, and Close controls.
- **Finite Tap Execution**: Configure tap count (1–100), interval (150–5000 ms), and physical target coordinates (X/Y).
- **Explicit User Control**: Automation only runs when manually triggered by the user via the floating control's Start button. Prominent Stop button halts execution immediately.
- **Privacy & Safety First**:
  - Offline application (requires no Internet permission).
  - Collects no credentials, keystrokes, screen contents, or app data.
  - Performs no anti-cheat evasion or stealth attempts.

## Architecture

```text
MainActivity
  ├── Permission status checkers (Overlay & Accessibility)
  ├── Configuration UI (Tap count, Interval, X/Y coordinates)
  └── Overlay Service launcher & Stop All controls

OverlayService (Foreground Service)
  ├── WindowManager floating view layout (view_floating_control.xml)
  ├── Drag handle & touch gesture tracking
  ├── Progress UI & status updates
  └── ClickController delegate

ClickController
  ├── Finite state machine (IDLE, RUNNING, COMPLETED, STOPPED, ERROR)
  ├── Bounded gesture scheduling
  ├── Single gesture at a time via dispatchGesture
  └── Main handler delay loop

AutoClickAccessibilityService (AccessibilityService)
  ├── Handles gesture dispatch via Path & GestureDescription
  └── Does not inspect or read target app content
```

## System Permissions & Setup

1. **Overlay Permission (`SYSTEM_ALERT_WINDOW`)**:
   - Required to draw the movable floating panel above other apps.
   - Granted manually via Android Settings (*Display over other apps*).

2. **Accessibility Service (`BIND_ACCESSIBILITY_SERVICE`)**:
   - Required to perform user-configured touch gestures (`dispatchGesture()`).
   - Enabled manually in Android System Settings (*Accessibility > Auto Clicker*).

## Building & Installation

### Build Debug APK
Run the following command from the project root:
```bash
./gradlew assembleDebug
```
The compiled APK will be available at `app/build/reports/` or `app/build/outputs/apk/debug/app-debug.apk`.

### Run Unit Tests
To execute local unit tests for `ClickConfig` and `ClickController`:
```bash
./gradlew testDebugUnitTest
```

## Safe Usage Guidelines

- **Personal & Testing Use**: Intended solely for personal accessibility assistance, repetitive touch tasks where permitted, and testing applications/devices owned or authorized by the user.
- **Game & Third-Party App Rules**: Automation may violate terms of service for certain third-party apps or games. Always ensure you have authorization before performing automated taps.
