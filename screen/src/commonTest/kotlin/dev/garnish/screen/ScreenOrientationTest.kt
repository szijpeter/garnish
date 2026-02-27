package dev.garnish.screen

import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenOrientationTest {

    @Test
    fun allFourOrientationsExist() {
        val orientations = ScreenOrientation.entries
        assertEquals(4, orientations.size, "Should have exactly 4 orientations")
    }

    @Test
    fun orientationsHaveCorrectNames() {
        val expected = listOf("Portrait", "Landscape", "PortraitUpsideDown", "LandscapeReverse")
        val actual = ScreenOrientation.entries.map { it.name }
        assertEquals(expected, actual)
    }
}
