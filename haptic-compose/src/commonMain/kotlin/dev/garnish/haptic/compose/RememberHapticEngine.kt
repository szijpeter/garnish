package dev.garnish.haptic.compose

import androidx.compose.runtime.Composable
import dev.garnish.haptic.HapticEngine

/**
 * Remembers a platform-specific [HapticEngine] instance tied to the current Composition.
 *
 * Usage:
 * ```kotlin
 * @Composable
 * fun HapticButton() {
 *     val engine = rememberHapticEngine()
 *     Button(onClick = { engine.perform(HapticType.Click) }) {
 *         Text("Tap")
 *     }
 * }
 * ```
 */
@Composable
public expect fun rememberHapticEngine(): HapticEngine
