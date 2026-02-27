package dev.garnish.share

/**
 * Cross-platform sharing interface for text, URLs, images, and files.
 *
 * Usage:
 * ```kotlin
 * val shareKit: ShareKit = ...  // platform-specific creation
 * shareKit.shareText("Hello from Garnish!")
 * shareKit.shareUrl("https://example.com", title = "Check this out")
 * ```
 *
 * On Android, create via [ShareKit] factory function with a `Context`.
 * On iOS, create via the no-arg [ShareKit] factory function.
 */
public interface ShareKit {

    /**
     * Shares plain text using the platform share sheet.
     *
     * @param text The text content to share.
     * @param title Optional title displayed in the share sheet header.
     * @throws ShareUnavailableException when no share presenter is available.
     */
    public fun shareText(text: String, title: String? = null)

    /**
     * Shares a URL using the platform share sheet.
     *
     * @param url The URL string to share.
     * @param title Optional title displayed in the share sheet header.
     * @throws ShareUnavailableException when no share presenter is available.
     */
    public fun shareUrl(url: String, title: String? = null)

    /**
     * Shares an image using the platform share sheet.
     *
     * @param imageBytes Raw image bytes (PNG, JPEG, etc.).
     * @param mimeType MIME type of the image (default: `"image/png"`).
     * @throws ShareOperationException when temporary image preparation fails.
     * @throws ShareUnavailableException when no share presenter is available.
     */
    public fun shareImage(imageBytes: ByteArray, mimeType: String = "image/png")

    /**
     * Shares a file using the platform share sheet.
     *
     * @param fileBytes Raw file bytes.
     * @param fileName File name including extension (e.g., `"report.pdf"`).
     * @param mimeType MIME type of the file.
     * @throws ShareOperationException when temporary file preparation fails.
     * @throws ShareUnavailableException when no share presenter is available.
     */
    public fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String)
}
