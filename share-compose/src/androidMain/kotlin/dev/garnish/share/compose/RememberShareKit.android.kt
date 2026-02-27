package dev.garnish.share.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.garnish.share.ShareKit

/**
 * Android implementation of [rememberShareKit].
 * Uses [LocalContext] to obtain the Activity context needed by [ShareKit].
 */
@Composable
public actual fun rememberShareKit(): ShareKit {
    val context = LocalContext.current
    return remember(context) { ShareKit(context) }
}
