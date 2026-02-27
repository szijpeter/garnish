package dev.garnish.torch

import android.content.Context

/**
 * Creates an Android [Torch] backed by [android.hardware.camera2.CameraManager].
 *
 * @param context Android application context, used to access the camera service.
 */
public fun Torch(context: Context): Torch = AndroidTorch(context)
