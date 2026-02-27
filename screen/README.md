# screen

Cross-platform screen controller for Kotlin Multiplatform — brightness, keep-screen-on, and orientation lock.

## API

```kotlin
enum class ScreenOrientation { Portrait, Landscape, PortraitUpsideDown, LandscapeReverse }

interface ScreenController {
    var brightness: Float        // 0.0–1.0
    var keepScreenOn: Boolean
    fun lockOrientation(orientation: ScreenOrientation)
    fun unlockOrientation()
}
```

## Usage

```kotlin
// Android
val screen = ScreenController(activity)

// iOS
val screen = ScreenController()

screen.brightness = 0.8f
screen.keepScreenOn = true
screen.lockOrientation(ScreenOrientation.Portrait)
```

### Compose (`screen-compose`)

```kotlin
// Lifecycle-aware — auto-restores on disposal
@Composable
fun VideoPlayer() {
    KeepScreenOn()
    LockOrientation(ScreenOrientation.Landscape)
    // ... video content
}
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `WindowManager.LayoutParams` (brightness, keep-on), `ActivityInfo` (orientation) |
| iOS | `UIScreen.mainScreen.brightness`, `isIdleTimerDisabled`, `UIWindowScene.requestGeometryUpdate` (iOS 16+) |
