package dev.garnish.clipboard

/**
 * Cross-platform rich clipboard interface.
 *
 * Supports copying and pasting text, HTML, URIs, and images.
 *
 * Usage:
 * ```kotlin
 * val clipboard: RichClipboard = ...
 * clipboard.copy(ClipContent.PlainText("Hello!"))
 * val content: ClipContent? = clipboard.paste()
 * ```
 */
public interface RichClipboard {

    /**
     * Copies the given [content] to the system clipboard.
     */
    public fun copy(content: ClipContent)

    /**
     * Pastes the current clipboard content.
     *
     * @return The clipboard content, or `null` if the clipboard is empty
     *         or contains an unsupported format.
     */
    public fun paste(): ClipContent?

    /**
     * Whether the clipboard currently has content.
     */
    public fun hasContent(): Boolean
}
