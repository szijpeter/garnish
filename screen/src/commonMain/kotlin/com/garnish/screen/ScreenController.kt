package com.garnish.screen

/**
 * Cross-platform screen controller for brightness, keep-screen-on, and orientation lock.
 *
 * Usage:
 * ```kotlin
 * val screen: ScreenController = ...
 * screen.brightness = 0.8f
 * screen.keepScreenOn = true
 * screen.lockOrientation(ScreenOrientation.Portrait)
 * ```
 */
public interface ScreenController {

    /**
     * Screen brightness, normalized to 0.0–1.0.
     * Setting a value outside this range clamps it.
     * A value of -1.0 means "system default" on Android.
     */
    public var brightness: Float

    /**
     * When `true`, the screen will not turn off due to inactivity.
     * Remember to set back to `false` when no longer needed.
     */
    public var keepScreenOn: Boolean

    /**
     * Locks the screen to the given [orientation].
     */
    public fun lockOrientation(orientation: ScreenOrientation)

    /**
     * Unlocks the screen orientation, allowing it to follow the device sensor.
     */
    public fun unlockOrientation()
}
