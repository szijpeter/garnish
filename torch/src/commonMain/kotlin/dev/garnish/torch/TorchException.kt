package dev.garnish.torch

/** Base exception for torch operation failures. */
public open class TorchException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

/** Thrown when torch is unavailable on the current device/configuration. */
public class TorchUnavailableException(
    message: String = "Torch is not available on this device.",
) : TorchException(message)

/** Thrown when a platform torch operation fails at runtime. */
public class TorchOperationException(
    message: String,
    cause: Throwable? = null,
) : TorchException(message, cause)
