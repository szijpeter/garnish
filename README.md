# Garnish

Small, focused Kotlin Multiplatform primitives for common mobile system tasks.

[![Check](https://github.com/szijpeter/garnish/actions/workflows/check.yml/badge.svg?branch=main)](https://github.com/szijpeter/garnish/actions/workflows/check.yml)

## Description

Garnish is a modular KMP suite for system-level features that should be easy to use across Android and iOS.

- One feature per module.
- Explicit APIs with deterministic runtime behavior.
- Pick only what your app needs.

## Setup

### Build This Repository

```bash
./gradlew check --no-daemon
./gradlew apiCheck --no-daemon
./gradlew :composeApp:iosSimulatorArm64MainKlibrary --no-daemon
```

### Publish And Consume Locally (Current Flow)

```bash
./gradlew publishToMavenLocal --no-daemon
```

In your consumer KMP project:

```kotlin
repositories {
    mavenLocal()
    google()
    mavenCentral()
}

dependencies {
    commonMainImplementation("io.github.szijpeter.garnish:share:0.1.0-SNAPSHOT")
    commonMainImplementation("io.github.szijpeter.garnish:haptic:0.1.0-SNAPSHOT")
}
```

`alias(libs.plugins.garnishPublishing)` is only for this repo's maintainers when publishing Garnish modules. Consumers do not need it.
Maintainer publishing guide: [docs/MAVEN_CENTRAL.md](./docs/MAVEN_CENTRAL.md).

## Modules

| Module | Description | Module Docs | Sample |
|---|---|---|---|
| `share` | Share text, URL, image, file | [share](./share/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `share-compose` | `rememberShareKit()` for Compose | [share (Compose section)](./share/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `haptic` | Cross-platform haptic feedback | [haptic](./haptic/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `haptic-compose` | `rememberHapticEngine()` and modifier support | [haptic (Compose section)](./haptic/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `torch` | Flashlight control | [torch](./torch/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `torch-compose` | `rememberTorch()` for Compose | [torch-compose](./torch-compose/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `screen` | Brightness, keep-screen-on, orientation | [screen](./screen/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `screen-compose` | `KeepScreenOn()` and `LockOrientation()` | [screen (Compose section)](./screen/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `badge` | App icon badge count | [badge](./badge/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `clipboard` | Rich clipboard (text, HTML, URI, image) | [clipboard](./clipboard/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `clipboard-compose` | `rememberRichClipboard()` for Compose | [clipboard (Compose section)](./clipboard/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |
| `review` | In-app review request API | [review](./review/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/com/garnish/app/App.kt) |

## Sample Snippets

### Share

```kotlin
val shareKit = rememberShareKit()
shareKit.shareText("Hello from Garnish", title = "Share")
```

### Haptic

```kotlin
val haptic = rememberHapticEngine()
haptic.perform(HapticType.Success)
```

### Torch

```kotlin
val torch = rememberTorch()
if (torch.isAvailable) {
    torch.toggle()
}
```

### Review

```kotlin
// Android
val review = InAppReview(activity)

// iOS
val review = InAppReview()

val result = review.requestReview()
```

License: Apache-2.0. See [LICENSE](./LICENSE).
