package dev.garnish.clipboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.garnish.clipboard.RichClipboard

@Composable
public actual fun rememberRichClipboard(): RichClipboard {
    val context = LocalContext.current
    return remember(context) { RichClipboard(context) }
}
