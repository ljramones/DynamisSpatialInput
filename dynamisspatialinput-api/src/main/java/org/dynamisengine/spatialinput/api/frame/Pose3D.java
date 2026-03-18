package org.dynamisengine.spatialinput.api.frame;

import org.dynamisengine.vectrix.core.Vector3f;
import org.dynamisengine.vectrix.core.Quaternionf;

/**
 * A 3D pose: position and orientation.
 *
 * @param position    world-space position (meters)
 * @param orientation rotation as a unit quaternion
 */
public record Pose3D(Vector3f position, Quaternionf orientation) {

    /** Identity pose at origin with no rotation. */
    public static final Pose3D IDENTITY = new Pose3D(new Vector3f(), new Quaternionf());

    public Pose3D {
        if (position == null) throw new IllegalArgumentException("position must not be null");
        if (orientation == null) throw new IllegalArgumentException("orientation must not be null");
    }
}
