package org.dynamisengine.spatialinput.runtime;

/**
 * Service Provider Interface for spatial input backends.
 *
 * Discovered at runtime via ServiceLoader. Each provider (Ultraleap, OpenXR)
 * implements this interface to supply raw spatial tracking data.
 *
 * Backends are mechanism, not policy: they map vendor data into the internal
 * raw model, manage native sessions, and fire device events. They do not
 * derive gestures, smooth values, or make interaction decisions.
 *
 * Placeholder — full contract defined in Phase 1D.
 */
public interface SpatialBackend {

    /** Human-readable backend name (e.g., "Ultraleap", "OpenXR"). */
    String name();

    /** Priority for selection when multiple backends available. Higher = preferred. */
    int priority();

    /** Fast probe: is this backend usable on the current platform? */
    boolean isAvailable();
}
