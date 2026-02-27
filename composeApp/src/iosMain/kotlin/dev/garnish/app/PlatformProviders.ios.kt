package dev.garnish.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.garnish.badge.BadgeController
import dev.garnish.review.InAppReview
import dev.garnish.torch.Torch

@Composable
actual fun rememberTorch(): Torch {
    return remember { Torch() }
}

@Composable
actual fun rememberBadgeController(): BadgeController {
    return remember { BadgeController() }
}

@Composable
actual fun rememberInAppReview(): InAppReview {
    return remember { InAppReview() }
}
