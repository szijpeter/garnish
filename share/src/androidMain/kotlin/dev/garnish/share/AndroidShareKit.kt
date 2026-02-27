package dev.garnish.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/**
 * Android [ShareKit] implementation using [Intent.ACTION_SEND] and the system chooser.
 *
 * For images and files, writes bytes to a temp cache directory and shares
 * via a [FileProvider] `content://` URI for security and compatibility.
 */
internal class AndroidShareKit(private val context: Context) : ShareKit {

    override fun shareText(text: String, title: String?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            title?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }
        startChooser(intent, title)
    }

    override fun shareUrl(url: String, title: String?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            title?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }
        startChooser(intent, title)
    }

    override fun shareImage(imageBytes: ByteArray, mimeType: String) {
        val file = writeTempFile(imageBytes, "shared_image", mimeExtension(mimeType))
        val uri = fileProviderUri(file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startChooser(intent, null)
    }

    override fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String) {
        val safeName = sanitizeFileName(fileName.ifBlank { "shared_file" })
        val file = writeTempFile(fileBytes, safeName, "")
        val uri = fileProviderUri(file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startChooser(intent, null)
    }

    private fun startChooser(intent: Intent, title: String?) {
        val chooser = Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    private fun writeTempFile(bytes: ByteArray, name: String, extension: String): File {
        val dir = File(context.cacheDir, "garnish_share").also { it.mkdirs() }
        val file = File(dir, if (extension.isNotEmpty()) "$name.$extension" else name)
        file.writeBytes(bytes)
        return file
    }

    private fun fileProviderUri(file: File) =
        FileProvider.getUriForFile(context, "${context.packageName}.garnish.share.provider", file)

    private fun mimeExtension(mimeType: String): String = when {
        mimeType.contains("png", ignoreCase = true) -> "png"
        mimeType.contains("jpeg", ignoreCase = true) || mimeType.contains("jpg", ignoreCase = true) -> "jpg"
        mimeType.contains("gif", ignoreCase = true) -> "gif"
        mimeType.contains("webp", ignoreCase = true) -> "webp"
        else -> "bin"
    }

    private fun sanitizeFileName(fileName: String): String =
        fileName.substringAfterLast('/').substringAfterLast('\\')
}

/**
 * Creates an Android [ShareKit].
 *
 * @param context Android context used for launching the share sheet and writing temp files.
 */
public fun ShareKit(context: Context): ShareKit = AndroidShareKit(context)
