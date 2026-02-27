package com.garnish.share.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.garnish.share.ShareKit

/**
 * iOS implementation of [rememberShareKit].
 * No additional context needed — iOS ShareKit discovers the root view controller.
 */
@Composable
public actual fun rememberShareKit(): ShareKit {
    return remember { ShareKit() }
}
