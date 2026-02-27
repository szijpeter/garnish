package com.garnish.torch

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

/**
 * Android [Torch] implementation using [CameraManager].
 *
 * No camera preview required — uses [CameraManager.setTorchMode] directly.
 */
internal class AndroidTorch(context: Context) : Torch {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val cameraId: String? = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    override val isAvailable: Boolean = cameraId != null

    private var _isOn: Boolean = false

    override var isOn: Boolean
        get() = _isOn
        set(value) {
            val id = cameraId ?: throw TorchUnavailableException()
            try {
                cameraManager.setTorchMode(id, value)
                _isOn = value
            } catch (e: Exception) {
                throw TorchOperationException("Unable to set torch state to $value.", e)
            }
        }
}
