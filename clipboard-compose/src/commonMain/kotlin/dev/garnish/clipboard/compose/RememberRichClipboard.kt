package dev.garnish.clipboard.compose

import androidx.compose.runtime.Composable
import dev.garnish.clipboard.RichClipboard

/**
 * Remembers a platform-specific [RichClipboard] instance.
 */
@Composable
public expect fun rememberRichClipboard(): RichClipboard
