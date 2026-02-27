package dev.garnish.app

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.garnish.review.InAppReview
import dev.garnish.review.ReviewResult
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewIntegrationTest {

    @Test
    fun requestReviewReturnsKnownResultWithoutThrowing() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            var review: InAppReview? = null
            scenario.onActivity { activity ->
                review = InAppReview(activity)
            }

            assertNotNull(review)
            val result = runBlocking {
                withTimeout(10_000) {
                    review!!.requestReview()
                }
            }

            assertTrue(
                result == ReviewResult.Requested ||
                    result == ReviewResult.NotAvailable ||
                    result == ReviewResult.Error,
            )
        }
    }
}
