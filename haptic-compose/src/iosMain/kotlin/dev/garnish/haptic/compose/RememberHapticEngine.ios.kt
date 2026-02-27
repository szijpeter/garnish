package dev.garnish.haptic.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.garnish.haptic.HapticEngine

/**
 * iOS implementation of [rememberHapticEngine].
 */
@Composable
public actual fun rememberHapticEngine(): HapticEngine {
    return remember { HapticEngine() }
}
