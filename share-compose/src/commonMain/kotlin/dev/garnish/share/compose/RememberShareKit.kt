package dev.garnish.share.compose

import androidx.compose.runtime.Composable
import dev.garnish.share.ShareKit

/**
 * Remembers a platform-specific [ShareKit] instance tied to the current Composition.
 *
 * On Android, the [ShareKit] is backed by the current Activity context.
 * On iOS, it presents via the root `UIViewController`.
 *
 * Usage:
 * ```kotlin
 * @Composable
 * fun ShareButton() {
 *     val shareKit = rememberShareKit()
 *     Button(onClick = { shareKit.shareText("Hello!") }) {
 *         Text("Share")
 *     }
 * }
 * ```
 */
@Composable
public expect fun rememberShareKit(): ShareKit
