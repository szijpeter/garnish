package com.garnish.review

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume

/**
 * Android [InAppReview] implementation using the Google Play In-App Review API.
 *
 * Requires `com.google.android.play:review-ktx` which is bundled by this module.
 */
internal class AndroidInAppReview(private val activity: Activity) : InAppReview {

    override suspend fun requestReview(): ReviewResult {
        if (activity.isFinishing || activity.isDestroyed) {
            return ReviewResult.NotAvailable
        }

        val manager = ReviewManagerFactory.create(activity)
        return try {
            val reviewInfo = manager.requestReviewFlow().await()
            manager.launchReviewFlow(activity, reviewInfo).await()
            ReviewResult.Requested
        } catch (e: ReviewException) {
            mapReviewException(e)
        } catch (_: Exception) {
            ReviewResult.Error
        }
    }
}

internal fun mapReviewException(exception: ReviewException): ReviewResult = when (exception.errorCode) {
    ReviewErrorCode.PLAY_STORE_NOT_FOUND -> ReviewResult.NotAvailable
    else -> ReviewResult.Error
}

private suspend fun <T> Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            if (!cont.isActive) return@addOnCompleteListener
            if (task.isSuccessful) {
                cont.resume(task.result)
            } else {
                val error = task.exception ?: IllegalStateException("In-app review task failed")
                cont.resumeWithException(error)
            }
        }
    }

/**
 * Creates an Android [InAppReview].
 *
 * @param activity The Activity context for launching the review flow.
 */
public fun InAppReview(activity: Activity): InAppReview = AndroidInAppReview(activity)
