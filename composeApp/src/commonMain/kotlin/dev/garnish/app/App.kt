package dev.garnish.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import garnish.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

private val GarnishColors = lightColorScheme(
    primary = Color(0xFF2F5B3C),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF7A5734),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF5F2E9),
    onBackground = Color(0xFF231E17),
    surface = Color(0xFFFFFCF5),
    onSurface = Color(0xFF231E17),
    surfaceVariant = Color(0xFFE6DFCF),
    onSurfaceVariant = Color(0xFF5A5447),
    error = Color(0xFF9A2D2F),
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val platformImageBytes = rememberPlatformGarnishImageBytes()
    val garnishMarkBytes by produceState<ByteArray?>(initialValue = platformImageBytes, key1 = platformImageBytes) {
        if (platformImageBytes != null) {
            value = platformImageBytes
            return@produceState
        }
        value = runCatching { Res.readBytes("files/garnish-mark-256.png") }.getOrNull()
    }

    MaterialTheme(colorScheme = GarnishColors) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFF8F4E8), Color(0xFFEEE6D4)),
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .safeContentPadding()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Header()

                    ShareDemo(garnishMarkBytes)
                    HapticDemo()
                    TorchDemo()
                    ScreenDemo()
                    BadgeDemo()
                    ClipboardDemo(garnishMarkBytes)
                    ReviewDemo()

                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFEBE4D2))
                .border(1.dp, Color(0xFFD6CCB7), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            GarnishMark(modifier = Modifier.size(34.dp), tint = MaterialTheme.colorScheme.primary)
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Garnish",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                "Small KMP system primitives. One focused module at a time.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color(0xFFD6CCB7))
}

@Composable
private fun ShareDemo(garnishMarkBytes: ByteArray?) {
    val shareKit = rememberShareKit()
    var text by remember { mutableStateOf("Don't panic. Garnish can handle this share.") }
    var url by remember { mutableStateOf("https://en.wikipedia.org/wiki/The_Hitchhiker%27s_Guide_to_the_Galaxy") }

    SectionCard(
        title = "Share",
        subtitle = "shareText · shareUrl · shareImage · shareFile",
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("shareText(text, title)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { shareKit.shareText(text, title = "Mostly Harmless") },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("shareText()") }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("shareUrl(url, title)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { shareKit.shareUrl(url, title = "Guide Link") },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("shareUrl()") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                garnishMarkBytes?.let { bytes ->
                    shareKit.shareImage(bytes, mimeType = "image/png")
                }
            },
            enabled = garnishMarkBytes != null,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("shareImage(garnish icon)") }

        Button(
            onClick = {
                val csvBytes = "name,answer\nArthur Dent,42\nFord Prefect,42".encodeToByteArray()
                shareKit.shareFile(csvBytes, "answers.csv", "text/csv")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("shareFile(bytes, \"answers.csv\")") }
    }
}

@Composable
private fun HapticDemo() {
    val engine = rememberHapticEngine()

    SectionCard(
        title = "Haptic",
        subtitle = "perform(type) — 7 haptic types",
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HapticType.entries.forEach { type ->
                FilledTonalButton(onClick = { engine.perform(type) }) {
                    Text(type.name)
                }
            }
        }
    }
}

@Composable
private fun TorchDemo() {
    val torch = rememberTorch()
    var torchError by remember { mutableStateOf<String?>(null) }

    SectionCard(
        title = "Torch",
        subtitle = "isAvailable · isOn · toggle()",
    ) {
        if (!torch.isAvailable) {
            Text(
                "Torch is unavailable on this device.",
                color = MaterialTheme.colorScheme.error,
            )
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

@Composable
private fun ScreenDemo() {
    val controller = rememberScreenController()
    var brightness by remember { mutableFloatStateOf(controller.brightness.coerceIn(0f, 1f)) }
    var keepOn by remember { mutableStateOf(false) }
    var lockedOrientation by remember { mutableStateOf<ScreenOrientation?>(null) }

    if (keepOn) {
        KeepScreenOn()
    }
    lockedOrientation?.let { LockOrientation(it) }

    SectionCard(
        title = "Screen",
        subtitle = "brightness · keepScreenOn · lockOrientation · unlockOrientation",
    ) {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("keepScreenOn")
            Switch(checked = keepOn, onCheckedChange = { keepOn = it })
        }

        Spacer(modifier = Modifier.height(8.dp))

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

@Composable
private fun BadgeDemo() {
    val badge = rememberBadgeController()
    var count by remember { mutableIntStateOf(0) }
    var badgeError by remember { mutableStateOf<String?>(null) }

    SectionCard(
        title = "Badge",
        subtitle = "setBadgeCount(count) · clearBadge()",
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = { count = (count - 1).coerceAtLeast(0) }) { Text("-") }
            Text(
                "$count",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
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

@Composable
private fun ClipboardDemo(garnishMarkBytes: ByteArray?) {
    val clipboard = rememberRichClipboard()
    var clipText by remember { mutableStateOf("The answer is 42.") }
    var htmlInput by remember { mutableStateOf("<b>Don't panic.</b> Bring a towel.") }
    var uriInput by remember { mutableStateOf("https://github.com/szijpeter/garnish") }
    var pastedResult by remember { mutableStateOf("") }
    var hasContent by remember { mutableStateOf(false) }

    SectionCard(
        title = "Clipboard",
        subtitle = "copy(ClipContent) · paste() · hasContent()",
    ) {
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
        ) { Text("copy(PlainText)") }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = htmlInput,
            onValueChange = { htmlInput = it },
            label = { Text("ClipContent.Html") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = {
                clipboard.copy(
                    ClipContent.Html(
                        htmlInput,
                        plainText = htmlInput.toClipboardFallbackText(),
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("copy(Html)") }

        Spacer(modifier = Modifier.height(8.dp))

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
        ) { Text("copy(Uri)") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                garnishMarkBytes?.let { bytes ->
                    clipboard.copy(ClipContent.Image(bytes, "image/png"))
                }
            },
            enabled = garnishMarkBytes != null,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) { Text("copy(Image: garnish icon)") }

        Spacer(modifier = Modifier.height(12.dp))

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
                    text = "paste() -> $pastedResult",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Text(
            "hasContent() -> $hasContent",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val HtmlTagRegex = Regex("<[^>]*>")

private fun String.toClipboardFallbackText(): String {
    val noTags = replace(HtmlTagRegex, " ")
    return noTags
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .trim()
        .replace(Regex("\\s+"), " ")
        .ifBlank { "Clipboard HTML" }
}

@Composable
private fun ReviewDemo() {
    val review = rememberInAppReview()
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<ReviewResult?>(null) }

    SectionCard(
        title = "Review",
        subtitle = "requestReview() -> ReviewResult",
    ) {
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

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD9CFBB)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFECE5D5)),
                    contentAlignment = Alignment.Center,
                ) {
                    GarnishMark(modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE3DBC9))
            content()
        }
    }
}

@Composable
private fun GarnishMark(
    modifier: Modifier,
    tint: Color,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val stem = Path().apply {
            moveTo(width * 0.50f, height * 0.22f)
            cubicTo(width * 0.45f, height * 0.32f, width * 0.43f, height * 0.42f, width * 0.43f, height * 0.52f)
            cubicTo(width * 0.43f, height * 0.65f, width * 0.48f, height * 0.74f, width * 0.50f, height * 0.82f)
            cubicTo(width * 0.52f, height * 0.74f, width * 0.57f, height * 0.65f, width * 0.57f, height * 0.52f)
            cubicTo(width * 0.57f, height * 0.42f, width * 0.55f, height * 0.32f, width * 0.50f, height * 0.22f)
            close()
        }
        val leftLeaf = Path().apply {
            moveTo(width * 0.22f, height * 0.44f)
            cubicTo(width * 0.35f, height * 0.35f, width * 0.48f, height * 0.34f, width * 0.58f, height * 0.43f)
            cubicTo(width * 0.48f, height * 0.55f, width * 0.34f, height * 0.57f, width * 0.22f, height * 0.44f)
            close()
        }
        val rightLeaf = Path().apply {
            moveTo(width * 0.78f, height * 0.44f)
            cubicTo(width * 0.65f, height * 0.35f, width * 0.52f, height * 0.34f, width * 0.42f, height * 0.43f)
            cubicTo(width * 0.52f, height * 0.55f, width * 0.66f, height * 0.57f, width * 0.78f, height * 0.44f)
            close()
        }

        drawPath(path = stem, color = tint)
        drawPath(path = leftLeaf, color = tint)
        drawPath(path = rightLeaf, color = tint)
    }
}
