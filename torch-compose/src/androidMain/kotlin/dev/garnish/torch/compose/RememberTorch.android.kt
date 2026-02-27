package dev.garnish.torch.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.garnish.torch.Torch

@Composable
public actual fun rememberTorch(): Torch {
    val context = LocalContext.current
    return remember(context) { Torch(context) }
}
