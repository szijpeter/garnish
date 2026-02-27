package dev.garnish.review

import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewResultTest {

    @Test
    fun allThreeResultsExist() {
        val results = ReviewResult.entries
        assertEquals(3, results.size, "Should have exactly 3 review results")
    }

    @Test
    fun resultsHaveCorrectNames() {
        val expected = listOf("Requested", "NotAvailable", "Error")
        val actual = ReviewResult.entries.map { it.name }
        assertEquals(expected, actual)
    }
}
