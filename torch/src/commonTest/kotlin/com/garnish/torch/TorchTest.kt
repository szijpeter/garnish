package com.garnish.torch

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TorchTest {

    @Test
    fun torchInterfaceContract() {
        val dummy = object : Torch {
            override val isAvailable: Boolean = true
            override var isOn: Boolean = false
            override fun toggle() {}
        }
        assertNotNull(dummy)
    }

    @Test
    fun torchExceptionsExposeMessages() {
        val unavailable = TorchUnavailableException("no torch")
        val failed = TorchOperationException("failed")
        assertEquals("no torch", unavailable.message)
        assertEquals("failed", failed.message)
    }
}
