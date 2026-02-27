package com.garnish.app

import androidx.compose.runtime.Composable
import com.garnish.badge.BadgeController
import com.garnish.review.InAppReview
import com.garnish.torch.Torch

/** Platform-specific [Torch] factory, remembered. */
@Composable
expect fun rememberTorch(): Torch

/** Platform-specific [BadgeController] factory, remembered. */
@Composable
expect fun rememberBadgeController(): BadgeController

/** Platform-specific [InAppReview] factory, remembered. */
@Composable
expect fun rememberInAppReview(): InAppReview
