package dev.garnish.clipboard

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ClipContentTest {

    @Test
    fun plainTextEquality() {
        val a = ClipContent.PlainText("hello")
        val b = ClipContent.PlainText("hello")
        assertEquals(a, b)
    }

    @Test
    fun htmlWithFallback() {
        val html = ClipContent.Html("<b>bold</b>", plainText = "bold")
        assertEquals("<b>bold</b>", html.html)
        assertEquals("bold", html.plainText)
    }

    @Test
    fun uriContent() {
        val uri = ClipContent.Uri("https://example.com")
        assertEquals("https://example.com", uri.uri)
    }

    @Test
    fun imageEquality() {
        val bytes = byteArrayOf(1, 2, 3)
        val a = ClipContent.Image(bytes, "image/png")
        val b = ClipContent.Image(byteArrayOf(1, 2, 3), "image/png")
        assertEquals(a, b, "Image equality should compare bytes content")
    }

    @Test
    fun imageDifferentMimeNotEqual() {
        val bytes = byteArrayOf(1, 2, 3)
        val a = ClipContent.Image(bytes, "image/png")
        val b = ClipContent.Image(bytes, "image/jpeg")
        assertNotEquals(a, b, "Different mimeType should not be equal")
    }
}
