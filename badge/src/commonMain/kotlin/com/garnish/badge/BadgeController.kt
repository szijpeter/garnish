package com.garnish.badge

/**
 * Cross-platform app badge controller.
 *
 * Sets or clears the numeric badge shown on the app icon.
 *
 * Usage:
 * ```kotlin
 * val badge: BadgeController = ...
 * badge.setBadgeCount(5)
 * badge.clearBadge()
 * ```
 */
public interface BadgeController {

    /**
     * Sets the app icon badge count.
     *
     * @param count The badge number to display. Must be >= 0.
     * @throws IllegalArgumentException if [count] is negative.
     * @throws BadgeUnavailableException when the platform cannot apply badge updates.
     * @throws BadgeOperationException when the platform update call fails.
     */
    public fun setBadgeCount(count: Int)

    /**
     * Clears the app icon badge (equivalent to `setBadgeCount(0)`).
     */
    public fun clearBadge() {
        setBadgeCount(0)
    }
}
