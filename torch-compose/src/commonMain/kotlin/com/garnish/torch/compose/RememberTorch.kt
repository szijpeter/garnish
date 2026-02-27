package com.garnish.torch.compose

import androidx.compose.runtime.Composable
import com.garnish.torch.Torch

/**
 * Remembers a platform-specific [Torch] instance.
 */
@Composable
public expect fun rememberTorch(): Torch
