package org.dynamisengine.spatialinput.api.pointer;

import org.dynamisengine.spatialinput.api.device.TrackingConfidence;
import org.dynamisengine.spatialinput.api.hand.Handedness;
import org.dynamisengine.vectrix.core.Vector3f;

/**
 * A spatial pointing ray derived from a tracked hand or controller.
 *
 * @param origin      ray origin in world space (meters)
 * @param direction   unit direction vector
 * @param confidence  tracking confidence of the source
 * @param handedness  which hand/side this pointer originates from
 * @param source      what produced this pointer
 */
public record SpatialPointer(
        Vector3f origin,
        Vector3f direction,
        TrackingConfidence confidence,
        Handedness handedness,
        PointerSource source) {

    public SpatialPointer {
        if (origin == null) throw new IllegalArgumentException("origin required");
        if (direction == null) throw new IllegalArgumentException("direction required");
    }
}
