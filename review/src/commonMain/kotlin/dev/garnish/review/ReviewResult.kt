package dev.garnish.review

/**
 * Result of an in-app review request.
 */
public enum class ReviewResult {
    /** The platform review API request was accepted. */
    Requested,

    /** In-app review is not available on this device or platform. */
    NotAvailable,

    /** An error occurred while requesting the review. */
    Error,
}
