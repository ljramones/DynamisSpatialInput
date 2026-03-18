package org.dynamisengine.spatialinput.api.device;

/**
 * Tracking state for a spatial input source.
 */
public enum TrackingState {
    /** Not currently tracked — no data available. */
    NOT_TRACKED,
    /** Fully tracked with high confidence. */
    TRACKED,
    /** Tracking is estimated/inferred (e.g., extrapolated from last known position). */
    INFERRED
}
