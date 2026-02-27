package dev.garnish.app

import androidx.compose.runtime.Composable
import dev.garnish.badge.BadgeController
import dev.garnish.review.InAppReview
import dev.garnish.torch.Torch

/** Platform-specific [Torch] factory, remembered. */
@Composable
expect fun rememberTorch(): Torch

/** Platform-specific [BadgeController] factory, remembered. */
@Composable
expect fun rememberBadgeController(): BadgeController

/** Platform-specific [InAppReview] factory, remembered. */
@Composable
expect fun rememberInAppReview(): InAppReview
