package com.garnish.torch.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.garnish.torch.Torch

@Composable
public actual fun rememberTorch(): Torch {
    return remember { Torch() }
}
