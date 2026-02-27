package dev.garnish.share

/** Base exception for share operation failures. */
public open class ShareException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

/** Thrown when no eligible platform presenter is available for sharing. */
public class ShareUnavailableException(
    message: String,
) : ShareException(message)

/** Thrown when preparing share content fails. */
public class ShareOperationException(
    message: String,
    cause: Throwable? = null,
) : ShareException(message, cause)
