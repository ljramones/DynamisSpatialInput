package org.dynamisengine.spatialinput.api.hand;

import org.dynamisengine.spatialinput.api.device.TrackingConfidence;
import org.dynamisengine.spatialinput.api.device.TrackingState;
import org.dynamisengine.spatialinput.api.frame.Pose3D;
import org.dynamisengine.spatialinput.api.pointer.SpatialPointer;
import org.dynamisengine.vectrix.core.Vector3f;

/**
 * Canonical tracked hand data for one frame.
 *
 * Immutable. Produced by the normalization layer from raw provider data.
 * All positions in engine world space (meters). All values deterministic.
 *
 * @param handedness      left or right hand
 * @param trackingState   current tracking quality
 * @param confidence      tracking confidence level
 * @param palmPose        palm center position and orientation
 * @param palmVelocity    palm velocity in m/s
 * @param pinchStrength   pinch gesture strength [0.0, 1.0] (thumb to index distance)
 * @param grabStrength    grab gesture strength [0.0, 1.0] (finger curl)
 * @param pointingRay     derived pointing ray from index finger direction
 */
public record TrackedHand(
        Handedness handedness,
        TrackingState trackingState,
        TrackingConfidence confidence,
        Pose3D palmPose,
        Vector3f palmVelocity,
        float pinchStrength,
        float grabStrength,
        SpatialPointer pointingRay) {

    public TrackedHand {
        if (handedness == null) throw new IllegalArgumentException("handedness required");
        if (trackingState == null) throw new IllegalArgumentException("trackingState required");
        if (confidence == null) throw new IllegalArgumentException("confidence required");
        if (palmPose == null) throw new IllegalArgumentException("palmPose required");
        if (palmVelocity == null) throw new IllegalArgumentException("palmVelocity required");
    }

    /** True if the hand is actively tracked (not lost). */
    public boolean isTracked() {
        return trackingState == TrackingState.TRACKED || trackingState == TrackingState.INFERRED;
    }
}
