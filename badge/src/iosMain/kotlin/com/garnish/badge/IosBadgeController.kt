@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.garnish.badge

import kotlinx.cinterop.cValue
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIApplication
import platform.UserNotifications.UNUserNotificationCenter

/**
 * iOS [BadgeController] implementation using [UNUserNotificationCenter].
 *
 * Uses `setBadgeCount()` on iOS 16+ for the recommended API.
 */
internal class IosBadgeController : BadgeController {

    override fun setBadgeCount(count: Int) {
        require(count >= 0) { "Badge count must be >= 0, was $count" }
        if (isAtLeastIos16()) {
            UNUserNotificationCenter.currentNotificationCenter().setBadgeCount(count.toLong(), null)
        } else {
            UIApplication.sharedApplication.applicationIconBadgeNumber = count.toLong()
        }
    }

    private fun isAtLeastIos16(): Boolean = NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
        cValue<NSOperatingSystemVersion> {
            majorVersion = 16
            minorVersion = 0
            patchVersion = 0
        },
    )
}

/**
 * Creates an iOS [BadgeController].
 */
public fun BadgeController(): BadgeController = IosBadgeController()
