package dev.garnish.haptic.compose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import dev.garnish.haptic.HapticType

/**
 * Triggers haptic feedback when the user taps this Composable.
 *
 * This modifier uses a tap gesture detector to trigger feedback.
 * Depending on modifier order and pointer-input composition, it can
 * interact with other gesture handlers such as `clickable`.
 *
 * Usage:
 * ```kotlin
 * Button(
 *     onClick = { /* action */ },
 *     modifier = Modifier.hapticFeedback(HapticType.Click)
 * ) { Text("Tap me") }
 * ```
 *
 * @param type The type of haptic feedback to trigger on tap.
 * @param enabled Whether haptic feedback is enabled. Defaults to `true`.
 */
public fun Modifier.hapticFeedback(
    type: HapticType,
    enabled: Boolean = true,
): Modifier = composed {
    if (!enabled) return@composed this
    val engine = rememberHapticEngine()
    this.pointerInput(type) {
        detectTapGestures(
            onTap = {
                engine.perform(type)
            },
        )
    }
}
