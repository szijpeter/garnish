package dev.garnish.haptic

/**
 * Types of haptic feedback available across platforms.
 *
 * Maps to platform-specific feedback generators:
 * - **Android**: [android.view.HapticFeedbackConstants] / [android.os.VibrationEffect]
 * - **iOS**: `UIImpactFeedbackGenerator` / `UINotificationFeedbackGenerator` / `UISelectionFeedbackGenerator`
 */
public enum class HapticType {
    /** Light tap — selection change or minor interaction. */
    Click,

    /** Double pulse — confirmation of a secondary action. */
    DoubleClick,

    /** Strong single pulse — significant interaction or long-press activation. */
    HeavyClick,

    /** Very subtle tick — scrolling through a picker or stepper. */
    Tick,

    /** Error/reject pattern — destructive action or validation failure. */
    Reject,

    /** Success notification — task completed or positive outcome. */
    Success,

    /** Warning notification — caution or attention needed. */
    Warning,
}
