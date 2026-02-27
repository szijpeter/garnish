@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.garnish.review

import kotlinx.cinterop.cValue
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.StoreKit.SKStoreReviewController
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIWindowScene

/**
 * iOS [InAppReview] implementation using [SKStoreReviewController].
 */
internal class IosInAppReview : InAppReview {

    override suspend fun requestReview(): ReviewResult {
        if (!isAtLeastIos10_3()) return ReviewResult.NotAvailable

        return try {
            val windowScene = UIApplication.sharedApplication.connectedScenes
                .mapNotNull { it as? UIWindowScene }
                .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
                ?: UIApplication.sharedApplication.connectedScenes.firstOrNull { it is UIWindowScene } as? UIWindowScene

            when (resolveIosReviewRequestMode(isAtLeastIos14(), windowScene != null)) {
                IosReviewRequestMode.Scene -> {
                    SKStoreReviewController.requestReviewInScene(windowScene!!)
                    ReviewResult.Requested
                }
                IosReviewRequestMode.Legacy -> {
                    SKStoreReviewController.requestReview()
                    ReviewResult.Requested
                }
                IosReviewRequestMode.NotAvailable -> ReviewResult.NotAvailable
            }
        } catch (_: Exception) {
            ReviewResult.Error
        }
    }

    private fun isAtLeastIos10_3(): Boolean = NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
        cValue<NSOperatingSystemVersion> {
            majorVersion = 10
            minorVersion = 3
            patchVersion = 0
        },
    )

    private fun isAtLeastIos14(): Boolean = NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
        cValue<NSOperatingSystemVersion> {
            majorVersion = 14
            minorVersion = 0
            patchVersion = 0
        },
    )
}

internal enum class IosReviewRequestMode {
    Scene,
    Legacy,
    NotAvailable,
}

internal fun resolveIosReviewRequestMode(
    isAtLeastIos14: Boolean,
    hasWindowScene: Boolean,
): IosReviewRequestMode = when {
    hasWindowScene -> IosReviewRequestMode.Scene
    isAtLeastIos14 -> IosReviewRequestMode.NotAvailable
    else -> IosReviewRequestMode.Legacy
}

/**
 * Creates an iOS [InAppReview].
 */
public fun InAppReview(): InAppReview = IosInAppReview()
