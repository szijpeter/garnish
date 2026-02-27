<p align="left">
  <img src="assets/brand/garnish-mark-1024.png" alt="Garnish mark" width="84" />
</p>

# Garnish

Small, focused Kotlin Multiplatform primitives for common mobile system tasks.

[![Check](https://github.com/szijpeter/garnish/actions/workflows/check.yml/badge.svg?branch=main)](https://github.com/szijpeter/garnish/actions/workflows/check.yml)
[![Maven Central (pending)](https://img.shields.io/badge/Maven%20Central-pending-lightgrey)](./docs/MAVEN_CENTRAL.md)

## Description

Garnish is a modular KMP suite for system-level features that should be easy to use across Android and iOS.

- One feature per module.
- Explicit APIs with deterministic runtime behavior.
- Thin dependencies so apps can pick only what they actually need.

## Setup

### Consume From Maven Central (Primary)

After first release, add dependencies from Maven Central:

```kotlin
repositories {
    google()
    mavenCentral()
}

dependencies {
    commonMainImplementation("io.github.szijpeter:garnish-share:<version>")
    commonMainImplementation("io.github.szijpeter:garnish-share-compose:<version>") // Compose helper module
    commonMainImplementation("io.github.szijpeter:garnish-review:<version>")
    // Pick only the modules your app needs.
}
```

### Tiny Example

```kotlin
@Composable
fun ShareExample() {
    val shareKit = rememberShareKit()
    Button(onClick = { shareKit.shareText("Don't panic.") }) {
        Text("Share")
    }
}
```

### Local Development (Maintainers)

```bash
./gradlew check apiCheck --no-daemon
./gradlew publishToMavenLocal --no-daemon
```

In a local consumer project during development:

```kotlin
repositories {
    mavenLocal()
    google()
    mavenCentral()
}
```

Publishing guide: [docs/MAVEN_CENTRAL.md](./docs/MAVEN_CENTRAL.md)

## Modules

| Module | Description | Docs | Sample |
|---|---|---|---|
| `share` | Share text, URL, image, file | [share](./share/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `share-compose` | `rememberShareKit()` for Compose | [share (Compose section)](./share/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `haptic` | Cross-platform haptic feedback | [haptic](./haptic/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `haptic-compose` | `rememberHapticEngine()` and modifier support | [haptic (Compose section)](./haptic/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `torch` | Flashlight control | [torch](./torch/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `torch-compose` | `rememberTorch()` for Compose | [torch-compose](./torch-compose/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `screen` | Brightness, keep-screen-on, orientation | [screen](./screen/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `screen-compose` | `KeepScreenOn()` and `LockOrientation()` | [screen (Compose section)](./screen/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `badge` | App icon badge count | [badge](./badge/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `clipboard` | Rich clipboard (text, HTML, URI, image) | [clipboard](./clipboard/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `clipboard-compose` | `rememberRichClipboard()` for Compose | [clipboard (Compose section)](./clipboard/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |
| `review` | In-app review request API | [review](./review/README.md) | [compose sample](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt) |

License: Apache-2.0. See [LICENSE](./LICENSE).
