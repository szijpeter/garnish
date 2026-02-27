package dev.garnish.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
actual fun GarnishIcon(modifier: Modifier) {
    val context = LocalContext.current
    val resourceId = remember(context) {
        context.resources.getIdentifier("garnish_sample_icon", "drawable", context.packageName)
    }

    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Garnish mark",
            modifier = modifier,
        )
    } else {
        Canvas(modifier = modifier) {
            drawCircle(
                color = Color(0xFF2F5B3C),
                radius = size.minDimension * 0.34f,
            )
        }
    }
}
