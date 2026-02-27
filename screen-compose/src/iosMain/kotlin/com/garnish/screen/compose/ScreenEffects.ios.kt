package com.garnish.screen.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.garnish.screen.ScreenController
import com.garnish.screen.ScreenOrientation

@Composable
public actual fun rememberScreenController(): ScreenController {
    return remember { ScreenController() }
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
    val controller = rememberScreenController()
    DisposableEffect(orientation) {
        controller.lockOrientation(orientation)
        onDispose { controller.unlockOrientation() }
    }
}
