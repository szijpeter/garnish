package com.garnish.review

import kotlin.test.Test
import kotlin.test.assertEquals

class IosInAppReviewTest {

    @Test
    fun sceneModeIsSelectedWhenSceneExists() {
        assertEquals(
            IosReviewRequestMode.Scene,
            resolveIosReviewRequestMode(isAtLeastIos14 = true, hasWindowScene = true),
        )
    }

    @Test
    fun notAvailableWhenNoSceneOnIos14Plus() {
        assertEquals(
            IosReviewRequestMode.NotAvailable,
            resolveIosReviewRequestMode(isAtLeastIos14 = true, hasWindowScene = false),
        )
    }

    @Test
    fun legacyModeWhenBelowIos14AndNoScene() {
        assertEquals(
            IosReviewRequestMode.Legacy,
            resolveIosReviewRequestMode(isAtLeastIos14 = false, hasWindowScene = false),
        )
    }
}
