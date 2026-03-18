package org.dynamisengine.spatialinput.api.device;

/**
 * Describes a connected spatial input device.
 *
 * @param id          device identity
 * @param displayName human-readable name
 * @param connected   true if currently connected
 */
public record SpatialDeviceInfo(SpatialDeviceId id, String displayName, boolean connected) {
    public SpatialDeviceInfo {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        if (displayName == null) throw new IllegalArgumentException("displayName must not be null");
    }
}
