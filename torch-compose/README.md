# torch-compose

Compose Multiplatform wrappers for `:torch`.

## Setup

```kotlin
repositories {
    google()
    mavenCentral()
}

dependencies {
    commonMainImplementation("io.github.szijpeter:garnish-torch-compose:<version>")
}
```

## API

```kotlin
@Composable
fun rememberTorch(): Torch
```

## Usage

```kotlin
val torch = rememberTorch()
Button(
    enabled = torch.isAvailable,
    onClick = { torch.toggle() },
) {
    Text(if (torch.isOn) "Turn Off" else "Turn On")
}
```
