package dev.garnish.share

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ShareKitTest {

    @Test
    fun shareKitInterfaceContract() {
        // Verify the interface contract via dummy implementation
        val dummy = object : ShareKit {
            override fun shareText(text: String, title: String?) {}
            override fun shareUrl(url: String, title: String?) {}
            override fun shareImage(imageBytes: ByteArray, mimeType: String) {}
            override fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String) {}
        }
        assertNotNull(dummy)
    }

    @Test
    fun shareExceptionsExposeMessages() {
        val unavailable = ShareUnavailableException("no presenter")
        val failed = ShareOperationException("write failed")
        assertEquals("no presenter", unavailable.message)
        assertEquals("write failed", failed.message)
    }
}
