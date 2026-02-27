package com.garnish.clipboard.compose

import androidx.compose.runtime.Composable
import com.garnish.clipboard.RichClipboard

/**
 * Remembers a platform-specific [RichClipboard] instance.
 */
@Composable
public expect fun rememberRichClipboard(): RichClipboard
