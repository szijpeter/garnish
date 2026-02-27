package com.garnish.haptic.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import com.garnish.haptic.HapticEngine

/**
 * Android implementation of [rememberHapticEngine].
 * Uses [LocalView] to obtain the View reference needed for haptic feedback.
 */
@Composable
public actual fun rememberHapticEngine(): HapticEngine {
    val view = LocalView.current
    return remember(view) { HapticEngine(view) }
}
