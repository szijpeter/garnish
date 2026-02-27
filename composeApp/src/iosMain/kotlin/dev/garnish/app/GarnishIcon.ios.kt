package dev.garnish.app

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import garnish.composeapp.generated.resources.Res
import garnish.composeapp.generated.resources.garnish_mark
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun GarnishIcon(modifier: Modifier) {
    Image(
        painter = painterResource(Res.drawable.garnish_mark),
        contentDescription = "Garnish mark",
        modifier = modifier,
    )
}
