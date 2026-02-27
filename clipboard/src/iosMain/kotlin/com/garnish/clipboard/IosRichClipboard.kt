@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.garnish.clipboard

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIPasteboard
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

/**
 * iOS [RichClipboard] implementation using [UIPasteboard].
 */
internal class IosRichClipboard : RichClipboard {

    override fun copy(content: ClipContent) {
        when (content) {
            is ClipContent.PlainText -> UIPasteboard.generalPasteboard.string = content.text
            is ClipContent.Html -> {
                val plainText = content.plainText.ifEmpty { content.html }
                UIPasteboard.generalPasteboard.string = plainText
                UIPasteboard.generalPasteboard.setValue(content.html, forPasteboardType = HTML_PASTEBOARD_TYPE)
            }
            is ClipContent.Uri -> {
                val nsUrl = NSURL.URLWithString(content.uri)
                if (nsUrl != null) {
                    UIPasteboard.generalPasteboard.URL = nsUrl
                } else {
                    UIPasteboard.generalPasteboard.string = content.uri
                }
            }
            is ClipContent.Image -> {
                val image = UIImage.imageWithData(content.bytes.toNSData()) ?: return
                UIPasteboard.generalPasteboard.image = image
            }
        }
    }

    override fun paste(): ClipContent? {
        val html = UIPasteboard.generalPasteboard.valueForPasteboardType(HTML_PASTEBOARD_TYPE) as? String
        val imageContent = readImageContent()
        val url = UIPasteboard.generalPasteboard.URL?.absoluteString
        val text = UIPasteboard.generalPasteboard.string
        return resolvePasteContent(html, imageContent, url, text)
    }

    override fun hasContent(): Boolean =
        UIPasteboard.generalPasteboard.valueForPasteboardType(HTML_PASTEBOARD_TYPE) != null ||
            UIPasteboard.generalPasteboard.hasStrings ||
            UIPasteboard.generalPasteboard.hasImages ||
            UIPasteboard.generalPasteboard.hasURLs

    private fun readImageContent(): ClipContent.Image? {
        val image = UIPasteboard.generalPasteboard.image ?: return null
        val pngData = UIImagePNGRepresentation(image)
        if (pngData != null) {
            return ClipContent.Image(
                bytes = pngData.toByteArray(),
                mimeType = "image/png",
            )
        }
        val jpegData = UIImageJPEGRepresentation(image, 1.0)
        if (jpegData != null) {
            return ClipContent.Image(
                bytes = jpegData.toByteArray(),
                mimeType = "image/jpeg",
            )
        }
        return null
    }

    private fun ByteArray.toNSData(): NSData {
        if (isEmpty()) return NSData()
        return memScoped {
            NSData.create(bytes = this@toNSData.refTo(0).getPointer(this), length = this@toNSData.size.toULong())
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        val dataSize = length.toInt()
        if (dataSize == 0) return ByteArray(0)
        return ByteArray(dataSize).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), bytes, length)
            }
        }
    }
}

internal fun resolvePasteContent(
    html: String?,
    image: ClipContent.Image?,
    url: String?,
    text: String?,
): ClipContent? {
    if (!html.isNullOrBlank()) {
        return ClipContent.Html(
            html = html,
            plainText = text.orEmpty(),
        )
    }
    if (image != null) return image
    if (!url.isNullOrBlank()) return ClipContent.Uri(url)
    if (!text.isNullOrBlank()) return ClipContent.PlainText(text)
    return null
}

private const val HTML_PASTEBOARD_TYPE: String = "public.html"

/**
 * Creates an iOS [RichClipboard].
 */
public fun RichClipboard(): RichClipboard = IosRichClipboard()
