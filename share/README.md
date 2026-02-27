# share

Cross-platform share sheet for Kotlin Multiplatform.

## API

```kotlin
interface ShareKit {
    fun shareText(text: String, title: String? = null)
    fun shareUrl(url: String, title: String? = null)
    fun shareImage(imageBytes: ByteArray, mimeType: String = "image/png")
    fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String)
}
```

`ShareKit` may throw:
- `ShareUnavailableException` when no platform presenter is available.
- `ShareOperationException` when temporary content preparation fails.

## Usage

```kotlin
// Android
val shareKit = ShareKit(context)

// iOS
val shareKit = ShareKit()

shareKit.shareText("Hello from Garnish!", title = "Check this out")
shareKit.shareUrl("https://example.com")
```

### Compose (`share-compose`)

```kotlin
@Composable
fun ShareButton() {
    val shareKit = rememberShareKit()
    Button(onClick = { shareKit.shareText("Hello!") }) { Text("Share") }
}
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `Intent.ACTION_SEND` + `FileProvider` for images/files |
| iOS | `UIActivityViewController` |
