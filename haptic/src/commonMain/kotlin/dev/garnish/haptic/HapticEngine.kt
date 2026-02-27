package dev.garnish.haptic

/**
 * Cross-platform haptic feedback engine.
 *
 * Triggers device vibration patterns corresponding to [HapticType] values.
 *
 * Usage:
 * ```kotlin
 * val engine: HapticEngine = ...  // platform-specific creation
 * engine.perform(HapticType.Click)
 * engine.perform(HapticType.Success)
 * ```
 *
 * On Android, create via [HapticEngine] factory function with a `View` or `Context`.
 * On iOS, create via the no-arg [HapticEngine] factory function.
 */
public interface HapticEngine {

    /**
     * Performs haptic feedback of the given [type].
     *
     * This is a fire-and-forget call. On devices without haptic hardware,
     * this is a no-op.
     *
     * @param type The type of haptic feedback to perform.
     */
    public fun perform(type: HapticType)
}
