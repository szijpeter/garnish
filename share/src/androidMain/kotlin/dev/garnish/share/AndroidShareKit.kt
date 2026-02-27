package dev.garnish.share

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
        startChooser(binaryShareIntent(uri, mimeType), null, uri)
    }

    override fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String) {
        val safeName = sanitizeFileName(fileName.ifBlank { "shared_file" })
        val file = writeTempFile(fileBytes, safeName, "")
        val uri = fileProviderUri(file)
        startChooser(binaryShareIntent(uri, mimeType), null, uri)
    }

    private fun binaryShareIntent(uri: Uri, mimeType: String): Intent =
        Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "shared", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    private fun startChooser(intent: Intent, title: String?, grantedUri: Uri? = null) {
        grantedUri?.let { grantReadPermissionToShareTargets(intent, it) }

        val chooser = Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (grantedUri != null) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                clipData = ClipData.newUri(context.contentResolver, "shared", grantedUri)
            }
        }
        context.startActivity(chooser)
    }

    private fun grantReadPermissionToShareTargets(intent: Intent, uri: Uri) {
        val targets = queryShareTargets(intent)
        for (resolveInfo in targets) {
            val packageName = resolveInfo.activityInfo?.packageName ?: continue
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    @Suppress("DEPRECATION")
    private fun queryShareTargets(intent: Intent) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong()),
            )
        } else {
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
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
