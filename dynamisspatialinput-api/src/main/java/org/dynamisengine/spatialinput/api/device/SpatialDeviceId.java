package org.dynamisengine.spatialinput.api.device;

/**
 * Typed identity for a spatial input provider/device.
 *
 * @param providerName backend provider name (e.g., "Ultraleap", "OpenXR")
 * @param deviceIndex  device index within the provider (0-based)
 */
public record SpatialDeviceId(String providerName, int deviceIndex) {
    public SpatialDeviceId {
        if (providerName == null || providerName.isBlank())
            throw new IllegalArgumentException("providerName must not be blank");
        if (deviceIndex < 0)
            throw new IllegalArgumentException("deviceIndex must be >= 0");
    }
}
