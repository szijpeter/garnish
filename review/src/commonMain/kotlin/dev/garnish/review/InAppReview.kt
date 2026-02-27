package dev.garnish.review

/**
 * Cross-platform in-app review interface.
 *
 * Triggers the platform's native review dialog:
 * - **Android**: Google Play In-App Review API
 * - **iOS**: `SKStoreReviewController.requestReview(in:)`
 *
 * Usage:
 * ```kotlin
 * val review: InAppReview = ...
 * val result = review.requestReview()
 * when (result) {
 *     ReviewResult.Requested -> println("Review requested")
 *     ReviewResult.NotAvailable -> println("Not available")
 *     ReviewResult.Error -> println("Error")
 * }
 * ```
 */
public interface InAppReview {

    /**
     * Requests the platform's in-app review flow.
     *
     * Note: platforms may silently suppress the dialog if the user has already
     * reviewed or if the app has requested too frequently.
     *
     * @return The result of the review request.
     */
    public suspend fun requestReview(): ReviewResult
}
