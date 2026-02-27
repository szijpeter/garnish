package com.garnish.haptic

import kotlin.test.Test
import kotlin.test.assertEquals

class HapticTypeTest {

    @Test
    fun allSevenHapticTypesExist() {
        val types = HapticType.entries
        assertEquals(7, types.size, "Should have exactly 7 haptic types")
    }

    @Test
    fun hapticTypesHaveCorrectNames() {
        val expected = listOf("Click", "DoubleClick", "HeavyClick", "Tick", "Reject", "Success", "Warning")
        val actual = HapticType.entries.map { it.name }
        assertEquals(expected, actual)
    }
}
