package com.garnish.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.WindowManager

/**
 * Android [ScreenController] implementation using [WindowManager.LayoutParams] and [ActivityInfo].
 */
internal class AndroidScreenController(private val activity: Activity) : ScreenController {

    override var brightness: Float
        get() = activity.window.attributes.screenBrightness
        set(value) {
            val attrs = activity.window.attributes
            attrs.screenBrightness = value.coerceIn(-1f, 1f)
            activity.window.attributes = attrs
        }

    override var keepScreenOn: Boolean
        get() = activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0
        set(value) {
            if (value) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

    override fun lockOrientation(orientation: ScreenOrientation) {
        activity.requestedOrientation = when (orientation) {
            ScreenOrientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            ScreenOrientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            ScreenOrientation.PortraitUpsideDown -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            ScreenOrientation.LandscapeReverse -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }
    }

    override fun unlockOrientation() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

/**
 * Creates an Android [ScreenController].
 *
 * @param activity The Activity whose window and orientation will be controlled.
 */
public fun ScreenController(activity: Activity): ScreenController = AndroidScreenController(activity)
