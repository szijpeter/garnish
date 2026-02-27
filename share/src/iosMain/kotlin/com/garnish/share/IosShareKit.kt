@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.garnish.share

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

/**
 * iOS [ShareKit] implementation using [UIActivityViewController].
 */
internal class IosShareKit : ShareKit {

    override fun shareText(text: String, title: String?) {
        val items = mutableListOf<Any>(text)
        present(items)
    }

    override fun shareUrl(url: String, title: String?) {
        val nsUrl = NSURL(string = url)
        val items = mutableListOf<Any>(nsUrl)
        present(items)
    }

    override fun shareImage(imageBytes: ByteArray, mimeType: String) {
        val imageUrl = writeTempFile(
            bytes = imageBytes,
            fileName = "shared_image_${NSUUID().UUIDString}.${mimeExtension(mimeType)}",
        )
        present(items = listOf(imageUrl))
    }

    override fun shareFile(fileBytes: ByteArray, fileName: String, mimeType: String) {
        val fallback = "shared_file.${mimeExtension(mimeType)}"
        val sanitizedName = sanitizeFileName(fileName).ifBlank { fallback }
        val fileUrl = writeTempFile(bytes = fileBytes, fileName = sanitizedName)
        present(items = listOf(fileUrl))
    }

    private fun present(items: List<Any>) {
        val presenter = topViewController()
            ?: throw ShareUnavailableException("No active UIViewController available to present share sheet.")
        val controller = UIActivityViewController(
            activityItems = items,
            applicationActivities = null,
        )
        presenter.presentViewController(controller, animated = true, completion = null)
    }

    private fun topViewController(): UIViewController? {
        val scene = UIApplication.sharedApplication.connectedScenes
            .mapNotNull { it as? UIWindowScene }
            .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
            ?: UIApplication.sharedApplication.connectedScenes.firstOrNull { it is UIWindowScene } as? UIWindowScene

        val root = (scene?.windows?.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow)?.rootViewController
            ?: (scene?.windows?.firstOrNull() as? UIWindow)?.rootViewController
            ?: UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return null

        var top = root
        while (top.presentedViewController != null) {
            top = top.presentedViewController!!
        }
        return top
    }

    private fun writeTempFile(bytes: ByteArray, fileName: String): NSURL {
        val uniqueName = "${NSUUID().UUIDString}_$fileName"
        val filePath = NSTemporaryDirectory() + uniqueName
        val data = bytes.toNSData()
        val didWrite = NSFileManager.defaultManager.createFileAtPath(
            path = filePath,
            contents = data,
            attributes = null,
        )
        if (!didWrite) {
            throw ShareOperationException("Unable to create a temporary share file for $fileName.")
        }
        return NSURL.fileURLWithPath(filePath)
    }

    private fun mimeExtension(mimeType: String): String = when {
        mimeType.contains("png", ignoreCase = true) -> "png"
        mimeType.contains("jpeg", ignoreCase = true) || mimeType.contains("jpg", ignoreCase = true) -> "jpg"
        mimeType.contains("gif", ignoreCase = true) -> "gif"
        mimeType.contains("webp", ignoreCase = true) -> "webp"
        mimeType.contains("pdf", ignoreCase = true) -> "pdf"
        mimeType.contains("csv", ignoreCase = true) -> "csv"
        mimeType.contains("json", ignoreCase = true) -> "json"
        mimeType.contains("plain", ignoreCase = true) || mimeType.contains("text", ignoreCase = true) -> "txt"
        else -> "bin"
    }

    private fun sanitizeFileName(fileName: String): String =
        fileName.substringAfterLast('/').substringAfterLast('\\')

    @OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)
    private fun ByteArray.toNSData(): NSData {
        if (isEmpty()) return NSData()
        return memScoped {
            NSData.create(bytes = this@toNSData.refTo(0).getPointer(this), length = this@toNSData.size.toULong())
        }
    }
}

/**
 * Creates an iOS [ShareKit].
 */
public fun ShareKit(): ShareKit = IosShareKit()
