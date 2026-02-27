package com.garnish.screen.compose

import androidx.compose.runtime.Composable
import com.garnish.screen.ScreenController
import com.garnish.screen.ScreenOrientation

/**
 * Remembers a platform-specific [ScreenController] tied to the current Composition.
 */
@Composable
public expect fun rememberScreenController(): ScreenController

/**
 * Keeps the screen on for the duration of this Composable's lifecycle.
 *
 * When the Composable leaves the composition, the screen-on flag is restored.
 *
 * Usage:
 * ```kotlin
 * @Composable
 * fun VideoPlayer() {
 *     KeepScreenOn()
 *     // ... video player content
 * }
 * ```
 */
@Composable
public expect fun KeepScreenOn()

/**
 * Locks the screen orientation for the duration of this Composable's lifecycle.
 *
 * When the Composable leaves the composition, the orientation lock is released.
 *
 * @param orientation The orientation to lock to.
 */
@Composable
public expect fun LockOrientation(orientation: ScreenOrientation)
