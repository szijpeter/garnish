package com.garnish.app

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.garnish.badge.AndroidBadgeOptions
import com.garnish.badge.BadgeController
import org.junit.Assert.assertNotNull
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BadgeIntegrationTest {

    @Test
    fun createsBadgeNotificationChannel() {
        assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val channelId = "garnish_test_badge_channel"
        BadgeController(
            context = context,
            options = AndroidBadgeOptions(
                channelId = channelId,
                notificationId = 4242,
            ),
        )

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        assertNotNull(manager.getNotificationChannel(channelId))
    }
}
