package com.garnish.clipboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.garnish.clipboard.RichClipboard

@Composable
public actual fun rememberRichClipboard(): RichClipboard {
    val context = LocalContext.current
    return remember(context) { RichClipboard(context) }
}
