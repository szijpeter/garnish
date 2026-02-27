package com.garnish.haptic

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/**
 * Android [HapticEngine] implementation using [View.performHapticFeedback].
 *
 * Requires a [View] reference to trigger haptics through the system.
 */
internal class AndroidHapticEngine(private val view: View) : HapticEngine {

    override fun perform(type: HapticType) {
        val feedbackConstant = when (type) {
            HapticType.Click -> HapticFeedbackConstants.CONTEXT_CLICK
            HapticType.DoubleClick -> HapticFeedbackConstants.LONG_PRESS
            HapticType.HeavyClick -> HapticFeedbackConstants.LONG_PRESS
            HapticType.Tick -> HapticFeedbackConstants.CLOCK_TICK
            HapticType.Reject -> HapticFeedbackConstants.REJECT
            HapticType.Success -> HapticFeedbackConstants.CONFIRM
            HapticType.Warning -> HapticFeedbackConstants.REJECT
        }
        view.performHapticFeedback(feedbackConstant)
    }
}

/**
 * Creates an Android [HapticEngine] backed by the given [view].
 *
 * @param view The view used to trigger haptic feedback via the system.
 */
public fun HapticEngine(view: View): HapticEngine = AndroidHapticEngine(view)
