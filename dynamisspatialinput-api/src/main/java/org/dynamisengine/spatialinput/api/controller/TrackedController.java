package org.dynamisengine.spatialinput.api.controller;

import org.dynamisengine.spatialinput.api.device.TrackingConfidence;
import org.dynamisengine.spatialinput.api.device.TrackingState;
import org.dynamisengine.spatialinput.api.frame.Pose3D;
import org.dynamisengine.spatialinput.api.hand.Handedness;
import org.dynamisengine.spatialinput.api.pointer.SpatialPointer;

/**
 * Canonical tracked controller data for one frame.
 *
 * Placeholder for v3 (OpenXR). Defined now to reserve the package and type shape.
 *
 * @param handedness     which hand holds this controller
 * @param trackingState  current tracking quality
 * @param confidence     tracking confidence
 * @param gripPose       controller grip position and orientation
 * @param aimPointer     derived aim/pointer ray
 * @param triggerValue   primary trigger analog value [0.0, 1.0]
 * @param gripValue      grip button analog value [0.0, 1.0]
 * @param buttonMask     bitmask of pressed buttons
 */
public record TrackedController(
        Handedness handedness,
        TrackingState trackingState,
        TrackingConfidence confidence,
        Pose3D gripPose,
        SpatialPointer aimPointer,
        float triggerValue,
        float gripValue,
        int buttonMask) {
}
