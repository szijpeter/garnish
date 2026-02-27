package com.garnish.clipboard

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class IosRichClipboardTest {

    @Test
    fun htmlTakesPrecedenceOverImageUrlAndText() {
        val result = resolvePasteContent(
            html = "<b>Hello</b>",
            image = ClipContent.Image(byteArrayOf(1, 2, 3), mimeType = "image/png"),
            url = "https://example.com",
            text = "Hello",
        )

        val html = assertIs<ClipContent.Html>(result)
        assertEquals("<b>Hello</b>", html.html)
        assertEquals("Hello", html.plainText)
    }

    @Test
    fun imageTakesPrecedenceOverUrlAndTextWhenNoHtml() {
        val image = ClipContent.Image(byteArrayOf(4, 5, 6), mimeType = "image/jpeg")
        val result = resolvePasteContent(
            html = null,
            image = image,
            url = "https://example.com",
            text = "fallback",
        )

        assertEquals(image, result)
    }

    @Test
    fun returnsNullWhenAllContentIsMissing() {
        assertNull(resolvePasteContent(html = null, image = null, url = null, text = null))
    }
}
