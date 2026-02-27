# 🌿 Garnish

**KMP System Essentials** — small, focused Kotlin Multiplatform libraries for common platform features.

[![Check](https://github.com/garnish-kmp/garnish/actions/workflows/check.yml/badge.svg)](https://github.com/garnish-kmp/garnish/actions/workflows/check.yml)

## Modules

| Module | Description | Android | iOS |
|---|---|---|---|
| `share` | Share sheet (text, URL, image, file) | `Intent.ACTION_SEND` | `UIActivityViewController` |
| `share-compose` | `rememberShareKit()` Composable | ✅ | ✅ |
| `haptic` | Haptic feedback (7 types) | `View.performHapticFeedback` | UIKit feedback generators |
| `haptic-compose` | `rememberHapticEngine()` Composable | ✅ | ✅ |
| `torch` | Flashlight control (no camera preview) | `CameraManager.setTorchMode` | `AVCaptureDevice.torchMode` |
| `screen` | Brightness, keep-on, orientation lock | `WindowManager` | `UIScreen` |
| `screen-compose` | `KeepScreenOn()`, `LockOrientation()` | ✅ | ✅ |
| `badge` | App icon badge count | `NotificationManager` | `UNUserNotificationCenter` |
| `clipboard` | Rich clipboard (text, HTML, URI, image) | `ClipboardManager` | `UIPasteboard` |
| `clipboard-compose` | `rememberRichClipboard()` Composable | ✅ | ✅ |
| `review` | In-app review | Play In-App Review | `SKStoreReviewController` |

## Quick Start

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.garnishKmp)       // or garnishCmp for Compose modules
    alias(libs.plugins.garnishPublishing)
}

dependencies {
    commonMainImplementation("com.garnish:share:0.1.0-SNAPSHOT")
    commonMainImplementation("com.garnish:haptic:0.1.0-SNAPSHOT")
    // ... pick what you need
}
```

### Share

```kotlin
// Core (ViewModel / non-Compose)
val shareKit = ShareKit(context)  // Android
val shareKit = ShareKit()          // iOS
shareKit.shareText("Hello from Garnish!")

// Compose
@Composable
fun ShareButton() {
    val shareKit = rememberShareKit()
    Button(onClick = { shareKit.shareText("Hello!") }) { Text("Share") }
}
```

### Haptic

```kotlin
// Core
val engine = HapticEngine(view)   // Android
val engine = HapticEngine()        // iOS
engine.perform(HapticType.Success)

// Compose
@Composable
fun HapticButton() {
    val engine = rememberHapticEngine()
    Button(onClick = { engine.perform(HapticType.Click) }) { Text("Tap") }
}
```

### Torch

```kotlin
val torch = Torch(context)  // Android
val torch = Torch()           // iOS
torch.toggle()
```

### Screen

```kotlin
val screen = ScreenController(activity)  // Android
screen.brightness = 0.8f
screen.keepScreenOn = true
screen.lockOrientation(ScreenOrientation.Portrait)

// Compose — lifecycle-aware
@Composable fun VideoPlayer() {
    KeepScreenOn()  // auto-restores on disposal
    // ...
}
```

## Architecture

```
garnish/
├── build-logic/convention/      3 convention plugins (garnish.kmp, garnish.cmp, garnish.publishing)
├── share/                       com.garnish:share
├── share-compose/               com.garnish:share-compose
├── haptic/                      com.garnish:haptic
├── haptic-compose/              com.garnish:haptic-compose
├── torch/                       com.garnish:torch
├── screen/                      com.garnish:screen
├── screen-compose/              com.garnish:screen-compose
├── badge/                       com.garnish:badge
├── clipboard/                   com.garnish:clipboard
├── clipboard-compose/           com.garnish:clipboard-compose
├── review/                      com.garnish:review
├── composeApp/                  Shared KMP sample module
├── androidApp/                  Android entry point
└── iosApp/                      iOS entry point
```

Library module `build.gradle.kts` files stay intentionally small (typically 2-10 lines)
thanks to convention plugins.

## Build

```bash
# Android
./gradlew :androidApp:assembleDebug

# iOS klibraries
./gradlew :composeApp:iosSimulatorArm64MainKlibrary

# All checks
./gradlew check

# API compatibility
./gradlew apiCheck
```

## Roadmap

- v0.x focus: harden correctness, tests, and docs for current modules.
- No new modules before v1.0.

## Tech Stack

- **AGP** 9.0.1
- **Kotlin** 2.3.10
- **Compose Multiplatform** 1.10.1
- **Gradle** 9.1.0
- **Targets**: Android (minSdk 26) + iOS (arm64 / simulatorArm64)

## License

```
Copyright 2026 Garnish Contributors
Licensed under the Apache License, Version 2.0
```
