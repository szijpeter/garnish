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

### Example

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

| Module(s) | Description |
|---|---|
| [`share`](./share/README.md) + `share-compose` | Share text, URL, image, file and Compose `rememberShareKit()` helper |
| [`haptic`](./haptic/README.md) + `haptic-compose` | Cross-platform haptic feedback and Compose engine/modifier helpers |
| [`torch`](./torch/README.md) + `torch-compose` | Flashlight control and Compose `rememberTorch()` helper |
| [`screen`](./screen/README.md) + `screen-compose` | Brightness, keep-screen-on, orientation and Compose lifecycle helpers |
| [`badge`](./badge/README.md) | App icon badge count |
| [`clipboard`](./clipboard/README.md) + `clipboard-compose` | Rich clipboard and Compose `rememberRichClipboard()` helper |
| [`review`](./review/README.md) | In-app review request API |

## Sample App

- Compose sample UI covering all modules: [composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt](./composeApp/src/commonMain/kotlin/dev/garnish/app/App.kt)
- Android host app: [androidApp](./androidApp)
- iOS host app: [iosApp](./iosApp)

License: Apache-2.0. See [LICENSE](./LICENSE).
