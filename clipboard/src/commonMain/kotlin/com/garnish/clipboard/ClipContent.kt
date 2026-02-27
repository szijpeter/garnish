package com.garnish.clipboard

/**
 * Rich clipboard content types supported for copy/paste operations.
 */
public sealed class ClipContent {
    /** Plain text content. */
    public data class PlainText(val text: String) : ClipContent()

    /** HTML content with optional plain-text fallback. */
    public data class Html(val html: String, val plainText: String = "") : ClipContent()

    /** URI/URL content. */
    public data class Uri(val uri: String) : ClipContent()

    /** Image content as raw bytes. */
    public data class Image(val bytes: ByteArray, val mimeType: String = "image/png") : ClipContent() {
        override fun equals(other: Any?): Boolean =
            other is Image && bytes.contentEquals(other.bytes) && mimeType == other.mimeType

        override fun hashCode(): Int = 31 * bytes.contentHashCode() + mimeType.hashCode()
    }
}
