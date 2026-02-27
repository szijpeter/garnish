package com.garnish.screen.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.garnish.screen.ScreenController
import com.garnish.screen.ScreenOrientation

@Composable
public actual fun rememberScreenController(): ScreenController {
    val activity = LocalContext.current.findActivity()
        ?: error("ScreenController requires an Activity-backed composition context")
    return remember(activity) { ScreenController(activity) }
}

@Composable
public actual fun KeepScreenOn() {
    val controller = rememberScreenController()
    DisposableEffect(Unit) {
        controller.keepScreenOn = true
        onDispose { controller.keepScreenOn = false }
    }
}

@Composable
public actual fun LockOrientation(orientation: ScreenOrientation) {
    val activity = LocalContext.current.findActivity()
        ?: error("ScreenController requires an Activity-backed composition context")
    val controller = remember(activity) { ScreenController(activity) }
    DisposableEffect(activity, orientation) {
        controller.lockOrientation(orientation)
        onDispose {
            // Avoid unlock/lock churn while Activity is being recreated for orientation change.
            if (!activity.isChangingConfigurations) {
                controller.unlockOrientation()
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
