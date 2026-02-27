# haptic

Cross-platform haptic feedback for Kotlin Multiplatform.

## Setup

```kotlin
repositories {
    google()
    mavenCentral() // Garnish modules are published here
}

dependencies {
    commonMainImplementation("io.github.szijpeter:garnish-haptic:<version>")
    // Optional Compose helper:
    commonMainImplementation("io.github.szijpeter:garnish-haptic-compose:<version>")
}
```

## API

```kotlin
enum class HapticType { Click, DoubleClick, HeavyClick, Tick, Reject, Success, Warning }

interface HapticEngine {
    fun perform(type: HapticType)
}
```

## Usage

```kotlin
// Android
val engine = HapticEngine(view)

// iOS
val engine = HapticEngine()

engine.perform(HapticType.Success)
```

### Compose (`haptic-compose`)

```kotlin
@Composable
fun HapticButton() {
    val engine = rememberHapticEngine()
    Button(onClick = { engine.perform(HapticType.Click) }) { Text("Tap") }
}

// Modifier shorthand
Button(modifier = Modifier.hapticFeedback(HapticType.Click)) { Text("Tap") }
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `View.performHapticFeedback(HapticFeedbackConstants.*)` |
| iOS | `UIImpactFeedbackGenerator`, `UINotificationFeedbackGenerator`, `UISelectionFeedbackGenerator` |
