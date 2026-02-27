package com.garnish.badge

/** Base exception for badge operation failures. */
public open class BadgeException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

/** Thrown when badge updates are unavailable due to platform/app state. */
public class BadgeUnavailableException(
    message: String,
) : BadgeException(message)

/** Thrown when the platform badge update operation fails unexpectedly. */
public class BadgeOperationException(
    message: String,
    cause: Throwable? = null,
) : BadgeException(message, cause)
