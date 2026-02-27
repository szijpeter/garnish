package com.garnish.torch

/**
 * Controls the device's torch (flashlight) without requiring a camera preview.
 *
 * Use [toggle] for a simple on/off toggle, or set [isOn] directly.
 * Check [isAvailable] first to determine if the device supports a torch.
 *
 * @throws TorchUnavailableException if a toggle/set request is made on an unavailable device.
 * @throws TorchOperationException if the platform torch API rejects the operation.
 */
public interface Torch {

    /** Whether the device has a torch/flashlight. */
    public val isAvailable: Boolean

    /** Whether the torch is currently turned on. */
    public var isOn: Boolean

    /** Toggles the torch on/off. */
    public fun toggle() {
        isOn = !isOn
    }
}
