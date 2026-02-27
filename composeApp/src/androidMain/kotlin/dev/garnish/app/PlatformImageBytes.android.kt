package dev.garnish.app

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberPlatformGarnishImageBytes(): ByteArray? {
    val context = LocalContext.current
    return remember(context) {
        runCatching {
            val resourceId = context.resources.getIdentifier(
                "garnish_sample_icon",
                "drawable",
                context.packageName,
            )
            if (resourceId == 0) return@runCatching null

            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                ?: return@runCatching null
            ByteArrayOutputStream().use { out ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out)
                out.toByteArray()
            }
        }.getOrNull()
    }
}
