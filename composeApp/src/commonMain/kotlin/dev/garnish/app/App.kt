package dev.garnish.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import dev.garnish.clipboard.ClipContent
import dev.garnish.clipboard.compose.rememberRichClipboard
import dev.garnish.haptic.HapticType
import dev.garnish.haptic.compose.rememberHapticEngine
import dev.garnish.review.ReviewResult
import dev.garnish.screen.ScreenOrientation
import dev.garnish.screen.compose.KeepScreenOn
import dev.garnish.screen.compose.LockOrientation
import dev.garnish.screen.compose.rememberScreenController
import dev.garnish.share.compose.rememberShareKit

@Composable
fun App() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .safeContentPadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Header
                Text(
                    "🌿 Garnish",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "KMP System Essentials — All APIs Demo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                ShareDemo()
                HapticDemo()
                TorchDemo()
                ScreenDemo()
                BadgeDemo()
                ClipboardDemo()
                ReviewDemo()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ──────────────────────────────────────────────
// 1. SHARE — shareText, shareUrl, shareImage, shareFile
// ──────────────────────────────────────────────
@Composable
private fun ShareDemo() {
    val shareKit = rememberShareKit()
    var text by remember { mutableStateOf("Hello from Garnish!") }
    var url by remember { mutableStateOf("https://github.com/szijpeter/garnish") }

    SectionCard("📤 Share", "shareText · shareUrl · shareImage · shareFile") {
        // shareText
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("shareText(text, title)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { shareKit.shareText(text, title = "Garnish Demo") },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("shareText()") }

        Spacer(modifier = Modifier.height(8.dp))

        // shareUrl
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("shareUrl(url, title)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { shareKit.shareUrl(url, title = "Check this out") },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("shareUrl()") }

        Spacer(modifier = Modifier.height(8.dp))

        // shareImage
        Button(
            onClick = {
                val pngBytes = ByteArray(64) { it.toByte() } // dummy image bytes
                shareKit.shareImage(pngBytes, mimeType = "image/png")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("shareImage(bytes, \"image/png\")") }

        // shareFile
        Button(
            onClick = {
                val csvBytes = "name,score\nAlice,100\nBob,95".encodeToByteArray()
                shareKit.shareFile(csvBytes, "scores.csv", "text/csv")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("shareFile(bytes, \"scores.csv\", \"text/csv\")") }
    }
}

// ──────────────────────────────────────────────
// 2. HAPTIC — perform(HapticType)
// ──────────────────────────────────────────────
@Composable
private fun HapticDemo() {
    val engine = rememberHapticEngine()

    SectionCard("🎮 Haptic", "perform(type) — 7 haptic types") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HapticType.entries.forEach { type ->
                FilledTonalButton(
                    onClick = { engine.perform(type) },
                ) { Text(type.name) }
            }
        }
    }
}

// ──────────────────────────────────────────────
// 3. TORCH — isAvailable, isOn, toggle()
// ──────────────────────────────────────────────
@Composable
private fun TorchDemo() {
    val torch = rememberTorch()
    var torchError by remember { mutableStateOf<String?>(null) }

    SectionCard("🔦 Torch", "isAvailable · isOn · toggle()") {
        if (!torch.isAvailable) {
            Text("Torch not available on this device/emulator",
                color = MaterialTheme.colorScheme.error)
            return@SectionCard
        }

        var isOn by remember { mutableStateOf(torch.isOn) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("isAvailable: ${torch.isAvailable}")
                Text("isOn: $isOn")
            }
            Switch(
                checked = isOn,
                onCheckedChange = {
                    runCatching {
                        torch.isOn = it
                        isOn = torch.isOn
                    }.onFailure { throwable ->
                        torchError = throwable.message
                    }
                },
            )
        }

        Button(
            onClick = {
                runCatching {
                    torch.toggle()
                    isOn = torch.isOn
                }.onFailure { throwable ->
                    torchError = throwable.message
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("toggle()") }

        torchError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

// ──────────────────────────────────────────────
// 4. SCREEN — brightness, keepScreenOn, lockOrientation, unlockOrientation
// ──────────────────────────────────────────────
@Composable
private fun ScreenDemo() {
    val controller = rememberScreenController()
    var brightness by remember { mutableFloatStateOf(controller.brightness.coerceIn(0f, 1f)) }
    var keepOn by remember { mutableStateOf(false) }
    var lockedOrientation by remember { mutableStateOf<ScreenOrientation?>(null) }

    // Lifecycle-aware effects
    if (keepOn) { KeepScreenOn() }
    lockedOrientation?.let { LockOrientation(it) }

    SectionCard("🖥️ Screen", "brightness · keepScreenOn · lockOrientation · unlockOrientation") {
        // brightness
        Text("brightness: ${(brightness * 100).toInt() / 100f}")
        Slider(
            value = brightness,
            onValueChange = {
                brightness = it
                controller.brightness = it
            },
            valueRange = 0f..1f,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // keepScreenOn
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("keepScreenOn")
            Switch(checked = keepOn, onCheckedChange = { keepOn = it })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // lockOrientation / unlockOrientation
        Text("lockOrientation:", fontWeight = FontWeight.Medium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ScreenOrientation.entries.forEach { orientation ->
                FilterChip(
                    selected = lockedOrientation == orientation,
                    onClick = { lockedOrientation = orientation },
                    label = { Text(orientation.name) },
                )
            }
            AssistChip(
                onClick = { lockedOrientation = null },
                label = { Text("Unlock") },
            )
        }
    }
}

// ──────────────────────────────────────────────
// 5. BADGE — setBadgeCount, clearBadge
// ──────────────────────────────────────────────
@Composable
private fun BadgeDemo() {
    val badge = rememberBadgeController()
    var count by remember { mutableIntStateOf(0) }
    var badgeError by remember { mutableStateOf<String?>(null) }

    SectionCard("🔴 Badge", "setBadgeCount(count) · clearBadge()") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = { count = (count - 1).coerceAtLeast(0) }) { Text("−") }
            Text("$count", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(horizontal = 16.dp))
            OutlinedButton(onClick = { count++ }) { Text("+") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    runCatching { badge.setBadgeCount(count) }
                        .onFailure { badgeError = it.message }
                },
                modifier = Modifier.weight(1f),
            ) { Text("setBadgeCount($count)") }
            FilledTonalButton(
                onClick = {
                    runCatching {
                        badge.clearBadge()
                        count = 0
                    }.onFailure { badgeError = it.message }
                },
                modifier = Modifier.weight(1f),
            ) { Text("clearBadge()") }
        }

        badgeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

// ──────────────────────────────────────────────
// 6. CLIPBOARD — copy, paste, hasContent + ClipContent types
// ──────────────────────────────────────────────
@Composable
private fun ClipboardDemo() {
    val clipboard = rememberRichClipboard()
    var clipText by remember { mutableStateOf("Copy me!") }
    var htmlInput by remember { mutableStateOf("<b>Bold</b> text") }
    var uriInput by remember { mutableStateOf("https://example.com") }
    var pastedResult by remember { mutableStateOf("") }
    var hasContent by remember { mutableStateOf(false) }

    SectionCard("📋 Clipboard", "copy(ClipContent) · paste() · hasContent()") {
        // PlainText
        OutlinedTextField(
            value = clipText,
            onValueChange = { clipText = it },
            label = { Text("ClipContent.PlainText") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { clipboard.copy(ClipContent.PlainText(clipText)) },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("copy(PlainText(…))") }

        Spacer(modifier = Modifier.height(8.dp))

        // Html
        OutlinedTextField(
            value = htmlInput,
            onValueChange = { htmlInput = it },
            label = { Text("ClipContent.Html") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { clipboard.copy(ClipContent.Html(htmlInput, plainText = "fallback")) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("copy(Html(…, plainText))") }

        Spacer(modifier = Modifier.height(8.dp))

        // Uri
        OutlinedTextField(
            value = uriInput,
            onValueChange = { uriInput = it },
            label = { Text("ClipContent.Uri") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { clipboard.copy(ClipContent.Uri(uriInput)) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("copy(Uri(…))") }

        Spacer(modifier = Modifier.height(8.dp))

        // Image
        Button(
            onClick = {
                clipboard.copy(ClipContent.Image(ByteArray(32) { it.toByte() }, "image/png"))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("copy(Image(bytes, \"image/png\"))") }

        Spacer(modifier = Modifier.height(12.dp))

        // paste + hasContent
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    val content = clipboard.paste()
                    pastedResult = when (content) {
                        is ClipContent.PlainText -> "PlainText: ${content.text}"
                        is ClipContent.Html -> "Html: ${content.html}"
                        is ClipContent.Uri -> "Uri: ${content.uri}"
                        is ClipContent.Image -> "Image: ${content.bytes.size} bytes"
                        null -> "(empty)"
                    }
                },
                modifier = Modifier.weight(1f),
            ) { Text("paste()") }
            FilledTonalButton(
                onClick = { hasContent = clipboard.hasContent() },
                modifier = Modifier.weight(1f),
            ) { Text("hasContent()") }
        }

        if (pastedResult.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Text(
                    text = "paste() → $pastedResult",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Text(
            "hasContent() → $hasContent",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ──────────────────────────────────────────────
// 7. REVIEW — requestReview()
// ──────────────────────────────────────────────
@Composable
private fun ReviewDemo() {
    val review = rememberInAppReview()
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<ReviewResult?>(null) }

    SectionCard("⭐ Review", "requestReview() → ReviewResult") {
        Button(
            onClick = {
                scope.launch {
                    result = review.requestReview()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("requestReview()") }

        result?.let {
            Text(
                "Result: ${it.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = when (it) {
                    ReviewResult.Requested -> MaterialTheme.colorScheme.primary
                    ReviewResult.NotAvailable -> MaterialTheme.colorScheme.onSurfaceVariant
                    ReviewResult.Error -> MaterialTheme.colorScheme.error
                },
            )
        }
    }
}

// ──────────────────────────────────────────────
// Shared UI components
// ──────────────────────────────────────────────
@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            content()
        }
    }
}
