package com.garnish.clipboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.garnish.clipboard.RichClipboard

@Composable
public actual fun rememberRichClipboard(): RichClipboard {
    return remember { RichClipboard() }
}
