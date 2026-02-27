package com.garnish.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.garnish.badge.BadgeController
import com.garnish.review.InAppReview
import com.garnish.torch.Torch

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
