package dev.garnish.torch

/**
 * Creates an iOS [Torch] backed by [platform.AVFoundation.AVCaptureDevice].
 */
public fun Torch(): Torch = IosTorch()
