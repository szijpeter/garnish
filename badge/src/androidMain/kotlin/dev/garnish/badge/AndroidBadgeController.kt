package dev.garnish.badge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Android [BadgeController] implementation using [NotificationManager].
 *
 * Android app icon badges are driven by notifications on a channel
 * with `setShowBadge(true)`. The launcher displays the badge
 * based on active notifications.
 *
 * **Requirements:**
 * - Android 8+ (API 26) for notification channels and badge support
 * - The `POST_NOTIFICATIONS` permission must be granted on Android 13+ (API 33)
 *   before calling [setBadgeCount]. The consuming app is responsible for
 *   requesting this permission at runtime.
 */
internal class AndroidBadgeController(
    private val context: Context,
    private val options: AndroidBadgeOptions,
) : BadgeController {

    private val smallIconResId: Int = resolveSmallIconResId(
        context = context,
        configuredIconResId = options.smallIconResId,
    )

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        ensureChannel()
    }

    override fun setBadgeCount(count: Int) {
        require(count >= 0) { "Badge count must be >= 0, was $count" }
        if (count == 0) {
            notificationManager.cancel(options.notificationId)
            return
        }
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            throw BadgeUnavailableException("Notifications are disabled for this app.")
        }

        val notification = NotificationCompat.Builder(context, options.channelId)
            .setSmallIcon(smallIconResId)
            .setContentTitle(options.notificationTitle)
            .setContentText(options.notificationText(count))
            .setNumber(count)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .setOngoing(false)
            .setAutoCancel(false)
            .build()
        try {
            notificationManager.notify(options.notificationId, notification)
        } catch (e: Exception) {
            throw BadgeOperationException("Unable to update badge notification.", e)
        }
    }

    private fun ensureChannel() {
        val existing = notificationManager.getNotificationChannel(options.channelId)
        if (existing == null) {
            val channel = NotificationChannel(
                options.channelId,
                options.channelName,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                setShowBadge(true)
                description = options.channelDescription
                // Suppress sound/vibration — we only want the badge
                setSound(null, null)
                enableVibration(false)
            }
            try {
                notificationManager.createNotificationChannel(channel)
            } catch (e: Exception) {
                throw BadgeOperationException("Unable to create badge notification channel.", e)
            }
        }
    }

    @DrawableRes
    private fun resolveSmallIconResId(
        context: Context,
        @DrawableRes configuredIconResId: Int,
    ): Int {
        if (configuredIconResId != 0) return configuredIconResId

        val appIconResId = context.applicationInfo.icon
        if (appIconResId != 0) return appIconResId

        return android.R.drawable.sym_def_app_icon
    }
}

/**
 * Android-specific options for badge notification customization.
 *
 * These values let consuming apps localize and brand the user-visible
 * notification text/channel used to drive launcher badge counts.
 *
 * [smallIconResId] defaults to `0`, which resolves automatically to the
 * host app launcher icon (`ApplicationInfo.icon`) at runtime.
 */
public data class AndroidBadgeOptions(
    val channelId: String = "garnish_badge",
    val channelName: String = "App Badge",
    val channelDescription: String = "Used to display app icon badge count",
    @param:DrawableRes val smallIconResId: Int = 0,
    val notificationId: Int = 9999,
    val notificationTitle: String = "Notifications",
    val notificationText: (count: Int) -> String = { count -> "You have $count pending items" },
)

/**
 * Creates an Android [BadgeController].
 *
 * @param context Application context for notification access.
 */
public fun BadgeController(context: Context): BadgeController =
    AndroidBadgeController(context, options = AndroidBadgeOptions())

/**
 * Creates an Android [BadgeController] with custom notification/channel options.
 */
public fun BadgeController(
    context: Context,
    options: AndroidBadgeOptions,
): BadgeController = AndroidBadgeController(context, options)
