package org.dynamisengine.spatialinput.api.gesture;

import org.dynamisengine.spatialinput.api.hand.Handedness;

/**
 * Gestures derived from tracked hand data.
 *
 * Sealed hierarchy — each variant represents a specific gesture state transition.
 * Produced by the gesture derivation layer in core, not by backends.
 */
public sealed interface DerivedGesture {

    /** Which hand produced this gesture. */
    Handedness handedness();

    record PinchStarted(Handedness handedness, float strength) implements DerivedGesture {}
    record PinchHeld(Handedness handedness, float strength) implements DerivedGesture {}
    record PinchReleased(Handedness handedness) implements DerivedGesture {}

    record GrabStarted(Handedness handedness, float strength) implements DerivedGesture {}
    record GrabHeld(Handedness handedness, float strength) implements DerivedGesture {}
    record GrabReleased(Handedness handedness) implements DerivedGesture {}

    record PointActive(Handedness handedness) implements DerivedGesture {}
    record PointEnded(Handedness handedness) implements DerivedGesture {}
}
