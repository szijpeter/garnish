# clipboard

Cross-platform rich clipboard for Kotlin Multiplatform — text, HTML, URI, and image support.

## API

```kotlin
sealed class ClipContent {
    data class PlainText(val text: String) : ClipContent()
    data class Html(val html: String, val plainText: String = "") : ClipContent()
    data class Uri(val uri: String) : ClipContent()
    data class Image(val bytes: ByteArray, val mimeType: String = "image/png") : ClipContent()
}

interface RichClipboard {
    fun copy(content: ClipContent)
    fun paste(): ClipContent?
    fun hasContent(): Boolean
}
```

## Usage

```kotlin
// Android
val clipboard = RichClipboard(context)

// iOS
val clipboard = RichClipboard()

clipboard.copy(ClipContent.PlainText("Hello!"))
clipboard.copy(ClipContent.Html("<b>Bold</b>", plainText = "Bold"))
clipboard.copy(ClipContent.Uri("https://example.com"))

val content = clipboard.paste()  // returns ClipContent?
```

### Compose (`clipboard-compose`)

```kotlin
@Composable
fun CopyButton() {
    val clipboard = rememberRichClipboard()
    Button(onClick = { clipboard.copy(ClipContent.PlainText("Copied!")) }) { Text("Copy") }
}
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `ClipboardManager` with `ClipData.newPlainText`, `newHtmlText`, `newRawUri` |
| iOS | `UIPasteboard.generalPasteboard` |
