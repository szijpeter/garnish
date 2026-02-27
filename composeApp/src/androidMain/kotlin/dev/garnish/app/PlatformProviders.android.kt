package dev.garnish.app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.garnish.badge.BadgeController
import dev.garnish.review.InAppReview
import dev.garnish.torch.Torch

@Composable
actual fun rememberTorch(): Torch {
    val context = LocalContext.current
    return remember(context) { Torch(context) }
}

@Composable
actual fun rememberBadgeController(): BadgeController {
    val context = LocalContext.current
    return remember(context) { BadgeController(context) }
}

@Composable
actual fun rememberInAppReview(): InAppReview {
    val activity = LocalContext.current.findActivity()
        ?: error("InAppReview requires an Activity-backed composition context")
    return remember(activity) { InAppReview(activity) }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
