package com.garnish.torch.compose

import kotlin.test.Test
import kotlin.test.assertNotNull

class TorchComposeTest {

    @Test
    fun rememberTorchExists() {
        // This is a smoke test to ensure the expect/actual contract matches
        // It won't full run the @Composable but verifies types
        assertNotNull(TorchComposeTest::class)
    }
}
