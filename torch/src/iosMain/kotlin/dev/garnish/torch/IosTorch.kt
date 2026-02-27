@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package dev.garnish.torch

import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.hasTorch
import platform.AVFoundation.isTorchAvailable
import platform.AVFoundation.torchMode

/**
 * iOS [Torch] implementation using [AVCaptureDevice].
 */
internal class IosTorch : Torch {

    private val device: AVCaptureDevice? = AVCaptureDeviceDiscoverySession
        .discoverySessionWithDeviceTypes(
            deviceTypes = listOf(AVCaptureDeviceTypeBuiltInWideAngleCamera),
            mediaType = AVMediaTypeVideo,
            position = AVCaptureDevicePositionBack
        )
        .devices
        .firstOrNull() as? AVCaptureDevice

    override val isAvailable: Boolean = device?.hasTorch == true && device.isTorchAvailable()

    override var isOn: Boolean
        get() = device?.torchMode == AVCaptureTorchModeOn
        set(value) {
            val d = device ?: throw TorchUnavailableException()
            if (!isAvailable) throw TorchUnavailableException()
            val locked = d.lockForConfiguration(null)
            if (!locked) throw TorchOperationException("Unable to lock torch configuration.")
            try {
                d.torchMode = if (value) AVCaptureTorchModeOn else AVCaptureTorchModeOff
            } finally {
                d.unlockForConfiguration()
            }
        }
}
