package dev.garnish.app

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.garnish.composeapp.R

@Composable
actual fun GarnishIcon(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.garnish_sample_icon),
        contentDescription = "Garnish mark",
        modifier = modifier,
    )
}
