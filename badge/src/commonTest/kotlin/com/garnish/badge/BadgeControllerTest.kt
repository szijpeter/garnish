package com.garnish.badge

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BadgeControllerTest {

    @Test
    fun clearBadgeDefaultImplementation() {
        var lastCount = -1
        val controller = object : BadgeController {
            override fun setBadgeCount(count: Int) {
                lastCount = count
            }
        }
        controller.clearBadge()
        kotlin.test.assertEquals(0, lastCount, "clearBadge should call setBadgeCount(0)")
    }

    @Test
    fun setBadgeCountRejectsNegative() {
        val controller = object : BadgeController {
            override fun setBadgeCount(count: Int) {
                require(count >= 0) { "Badge count must be >= 0, was $count" }
            }
        }
        assertFailsWith<IllegalArgumentException> {
            controller.setBadgeCount(-1)
        }
    }

    @Test
    fun badgeExceptionsExposeMessages() {
        val unavailable = BadgeUnavailableException("notifications disabled")
        val failed = BadgeOperationException("update failed")
        assertEquals("notifications disabled", unavailable.message)
        assertEquals("update failed", failed.message)
    }
}
