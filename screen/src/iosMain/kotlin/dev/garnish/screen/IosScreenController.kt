@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package dev.garnish.screen

import kotlinx.cinterop.cValue
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationMaskAll
import platform.UIKit.UIInterfaceOrientationMaskLandscapeLeft
import platform.UIKit.UIInterfaceOrientationMaskLandscapeRight
import platform.UIKit.UIInterfaceOrientationMaskPortrait
import platform.UIKit.UIInterfaceOrientationMaskPortraitUpsideDown
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIScreen
import platform.UIKit.UIWindowScene
import platform.UIKit.UIWindowSceneGeometryPreferencesIOS

/**
 * iOS [ScreenController] implementation using UIKit APIs.
 *
 * Brightness uses [UIScreen.mainScreen].
 * Keep-on uses [UIApplication.setIdleTimerDisabled].
 * Orientation locking uses [UIWindowScene.requestGeometryUpdate] (iOS 16+).
 */
internal class IosScreenController : ScreenController {

    override var brightness: Float
        get() = UIScreen.mainScreen.brightness.toFloat()
        set(value) {
            UIScreen.mainScreen.brightness = value.coerceIn(0f, 1f).toDouble()
        }

    override var keepScreenOn: Boolean
        get() = UIApplication.sharedApplication.isIdleTimerDisabled()
        set(value) {
            UIApplication.sharedApplication.setIdleTimerDisabled(value)
        }

    override fun lockOrientation(orientation: ScreenOrientation) {
        val mask = when (orientation) {
            ScreenOrientation.Portrait -> UIInterfaceOrientationMaskPortrait
            ScreenOrientation.Landscape -> UIInterfaceOrientationMaskLandscapeRight
            ScreenOrientation.PortraitUpsideDown -> UIInterfaceOrientationMaskPortraitUpsideDown
            ScreenOrientation.LandscapeReverse -> UIInterfaceOrientationMaskLandscapeLeft
        }
        requestGeometryUpdate(mask)
    }

    override fun unlockOrientation() {
        requestGeometryUpdate(UIInterfaceOrientationMaskAll)
    }

    private fun requestGeometryUpdate(orientationMask: platform.UIKit.UIInterfaceOrientationMask) {
        if (!isAtLeastIos16()) return

        val scene = UIApplication.sharedApplication.connectedScenes
            .mapNotNull { it as? UIWindowScene }
            .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
            ?: UIApplication.sharedApplication.connectedScenes.firstOrNull { it is UIWindowScene } as? UIWindowScene
            ?: return

        val preferences = UIWindowSceneGeometryPreferencesIOS(
            interfaceOrientations = orientationMask,
        )
        scene.requestGeometryUpdateWithPreferences(preferences, errorHandler = null)
    }

    private fun isAtLeastIos16(): Boolean = NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
        cValue<NSOperatingSystemVersion> {
            majorVersion = 16
            minorVersion = 0
            patchVersion = 0
        },
    )
}

/**
 * Creates an iOS [ScreenController].
 */
public fun ScreenController(): ScreenController = IosScreenController()
