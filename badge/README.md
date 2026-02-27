# badge

Cross-platform app icon badge count for Kotlin Multiplatform.

## Setup

```kotlin
repositories {
    google()
    mavenCentral() // Garnish modules are published here
}

dependencies {
    commonMainImplementation("io.github.szijpeter:garnish-badge:<version>")
}
```

## API

```kotlin
interface BadgeController {
    fun setBadgeCount(count: Int)
    fun clearBadge()  // default: setBadgeCount(0)
}
```

`setBadgeCount` throws:
- `IllegalArgumentException` for negative counts.
- `BadgeUnavailableException` when notifications are disabled.
- `BadgeOperationException` when platform update calls fail.

## Usage

```kotlin
// Android (requires POST_NOTIFICATIONS permission on API 33+)
val badge = BadgeController(context)

// Optional Android customization (recommended for localized/brand-safe text)
val customBadge = BadgeController(
    context = context,
    options = AndroidBadgeOptions(
        channelId = "updates_badge",
        channelName = "Updates",
        channelDescription = "Badge count support",
        smallIconResId = R.drawable.ic_stat_notify,
        notificationTitle = "Updates",
        notificationText = { count -> "$count pending updates" },
    ),
)

// iOS (requires notification permission)
val iosBadge = BadgeController()

iosBadge.setBadgeCount(5)
iosBadge.clearBadge()
```

## Platform Details

| Platform | Implementation |
|---|---|
| Android | `NotificationManager` with `IMPORTANCE_DEFAULT` channel + `setShowBadge(true)` |
| iOS | `UNUserNotificationCenter.setBadgeCount()` (iOS 16+) |

> **Note**: Android badge visibility depends on the launcher. Pixel and Samsung launchers show badge dots/counts from notification channels.
