@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package dev.garnish.haptic

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UISelectionFeedbackGenerator

/**
 * iOS [HapticEngine] implementation using UIKit feedback generators.
 *
 * Uses:
 * - [UIImpactFeedbackGenerator] for physical impact feedback (click, heavy click)
 * - [UINotificationFeedbackGenerator] for success/warning/reject
 * - [UISelectionFeedbackGenerator] for tick/selection changes
 */
internal class IosHapticEngine : HapticEngine {

    override fun perform(type: HapticType) {
        when (type) {
            HapticType.Click -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
            HapticType.DoubleClick -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
            HapticType.HeavyClick -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
            HapticType.Tick -> selection()
            HapticType.Reject -> notification(UINotificationFeedbackType.UINotificationFeedbackTypeError)
            HapticType.Success -> notification(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
            HapticType.Warning -> notification(UINotificationFeedbackType.UINotificationFeedbackTypeWarning)
        }
    }

    private fun impact(style: UIImpactFeedbackStyle) {
        val generator = UIImpactFeedbackGenerator(style)
        generator.prepare()
        generator.impactOccurred()
    }

    private fun notification(type: UINotificationFeedbackType) {
        val generator = UINotificationFeedbackGenerator()
        generator.prepare()
        generator.notificationOccurred(type)
    }

    private fun selection() {
        val generator = UISelectionFeedbackGenerator()
        generator.prepare()
        generator.selectionChanged()
    }
}

/**
 * Creates an iOS [HapticEngine].
 */
public fun HapticEngine(): HapticEngine = IosHapticEngine()
