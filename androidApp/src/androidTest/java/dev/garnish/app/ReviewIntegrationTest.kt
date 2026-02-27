package dev.garnish.app

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.garnish.review.InAppReview
import dev.garnish.review.ReviewResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewIntegrationTest {

    @Test
    fun requestReviewReturnsKnownResultWithoutThrowing() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val result = runBlocking { InAppReview(activity).requestReview() }
                assertTrue(
                    result == ReviewResult.Requested ||
                        result == ReviewResult.NotAvailable ||
                        result == ReviewResult.Error,
                )
            }
        }
    }
}
