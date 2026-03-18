package org.dynamisengine.spatialinput.api.frame;

import org.dynamisengine.spatialinput.api.gesture.DerivedGesture;
import org.dynamisengine.spatialinput.api.hand.TrackedHand;
import org.dynamisengine.spatialinput.api.pointer.SpatialPointer;

import java.util.List;
import java.util.Optional;

/**
 * Per-tick immutable spatial input snapshot.
 *
 * Contains all tracked spatial data for one engine tick. Deterministic
 * and replay-safe. Consumed by game/UI/world systems.
 *
 * @param tick       engine tick number
 * @param leftHand   left hand tracking data, empty if not tracked
 * @param rightHand  right hand tracking data, empty if not tracked
 * @param pointers   all derived spatial pointers this frame
 * @param gestures   all derived gesture state changes this frame
 */
public record SpatialInputFrame(
        long tick,
        Optional<TrackedHand> leftHand,
        Optional<TrackedHand> rightHand,
        List<SpatialPointer> pointers,
        List<DerivedGesture> gestures) {

    public SpatialInputFrame {
        leftHand = leftHand != null ? leftHand : Optional.empty();
        rightHand = rightHand != null ? rightHand : Optional.empty();
        pointers = pointers != null ? List.copyOf(pointers) : List.of();
        gestures = gestures != null ? List.copyOf(gestures) : List.of();
    }

    /** True if at least one hand is tracked. */
    public boolean hasTrackedHands() {
        return (leftHand.isPresent() && leftHand.get().isTracked())
                || (rightHand.isPresent() && rightHand.get().isTracked());
    }

    /** Empty frame with no tracking data. */
    public static SpatialInputFrame empty(long tick) {
        return new SpatialInputFrame(tick, Optional.empty(), Optional.empty(),
                List.of(), List.of());
    }
}
