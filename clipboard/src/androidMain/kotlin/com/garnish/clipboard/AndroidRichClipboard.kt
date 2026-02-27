package com.garnish.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

/**
 * Android [RichClipboard] implementation using [ClipboardManager].
 */
internal class AndroidRichClipboard(context: Context) : RichClipboard {

    private val appContext = context.applicationContext
    private val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override fun copy(content: ClipContent) {
        val clip = when (content) {
            is ClipContent.PlainText -> ClipData.newPlainText("text", content.text)
            is ClipContent.Html -> ClipData.newHtmlText("html", content.plainText, content.html)
            is ClipContent.Uri -> ClipData.newRawUri("uri", android.net.Uri.parse(content.uri))
            is ClipContent.Image -> {
                val file = writeImageTempFile(content.bytes, content.mimeType)
                val uri = fileProviderUri(file)
                ClipData.newUri(appContext.contentResolver, "image", uri)
            }
        }
        manager.setPrimaryClip(clip)
    }

    override fun paste(): ClipContent? {
        val clip = manager.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        val item = clip.getItemAt(0)
        return when {
            item.htmlText != null -> ClipContent.Html(item.htmlText!!, item.text?.toString() ?: "")
            item.uri != null -> {
                val uri = item.uri
                val mimeType = appContext.contentResolver.getType(uri).orEmpty()
                if (mimeType.startsWith("image/")) {
                    val imageBytes = appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    if (imageBytes != null) {
                        ClipContent.Image(imageBytes, mimeType = mimeType)
                    } else {
                        ClipContent.Uri(uri.toString())
                    }
                } else {
                    ClipContent.Uri(uri.toString())
                }
            }
            item.text != null -> ClipContent.PlainText(item.text.toString())
            else -> null
        }
    }

    override fun hasContent(): Boolean = manager.hasPrimaryClip()

    private fun writeImageTempFile(bytes: ByteArray, mimeType: String): File {
        val extension = when {
            mimeType.contains("png", ignoreCase = true) -> "png"
            mimeType.contains("jpeg", ignoreCase = true) || mimeType.contains("jpg", ignoreCase = true) -> "jpg"
            mimeType.contains("gif", ignoreCase = true) -> "gif"
            mimeType.contains("webp", ignoreCase = true) -> "webp"
            else -> "bin"
        }
        val dir = File(appContext.cacheDir, "garnish_clipboard").also { it.mkdirs() }
        val file = File(dir, "clipboard_image_${System.currentTimeMillis()}.$extension")
        try {
            file.writeBytes(bytes)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to persist clipboard image data", e)
        }
        return file
    }

    private fun fileProviderUri(file: File): android.net.Uri =
        FileProvider.getUriForFile(appContext, "${appContext.packageName}.garnish.clipboard.provider", file)
}

/**
 * Creates an Android [RichClipboard].
 *
 * @param context Application context for clipboard access.
 */
public fun RichClipboard(context: Context): RichClipboard = AndroidRichClipboard(context)
