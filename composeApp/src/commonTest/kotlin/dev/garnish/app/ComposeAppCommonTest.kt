package dev.garnish.app

import dev.garnish.clipboard.ClipContent
import dev.garnish.haptic.HapticType
import dev.garnish.review.ReviewResult
import dev.garnish.screen.ScreenOrientation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComposeAppCommonTest {

    @Test
    fun dependentModuleContractsAreAvailable() {
        assertEquals(7, HapticType.entries.size)
        assertEquals(4, ScreenOrientation.entries.size)
        assertEquals(3, ReviewResult.entries.size)
        val image = ClipContent.Image(byteArrayOf(1, 2, 3), mimeType = "image/png")
        assertTrue(image == ClipContent.Image(byteArrayOf(1, 2, 3), mimeType = "image/png"))
    }
}
