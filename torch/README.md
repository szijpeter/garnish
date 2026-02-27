# torch

Cross-platform flashlight control for Kotlin Multiplatform. No camera preview required.

## Setup

```kotlin
repositories {
    google()
    mavenCentral() // Garnish modules are published here
}

dependencies {
    commonMainImplementation("io.github.szijpeter:garnish-torch:<version>")
    // Optional Compose helper:
    commonMainImplementation("io.github.szijpeter:garnish-torch-compose:<version>")
}
```

## API

```kotlin
interface Torch {
    val isAvailable: Boolean
    var isOn: Boolean
    fun toggle()
}
```

## Usage

```kotlin
// Android
val torch = Torch(context)

// iOS
val torch = Torch()

if (torch.isAvailable) {
    torch.toggle()
}
```

### Compose (`torch-compose`)

```kotlin
@Composable
fun TorchToggle() {
    val torch = rememberTorch()
    Switch(
        enabled = torch.isAvailable,
        checked = torch.isOn,
        onCheckedChange = { torch.isOn = it },
    )
}
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `CameraManager.setTorchMode` — finds first flash-capable camera |
| iOS | `AVCaptureDevice.torchMode` — back camera, locks for configuration |
