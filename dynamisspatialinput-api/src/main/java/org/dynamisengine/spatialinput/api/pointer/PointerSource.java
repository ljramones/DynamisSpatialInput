package org.dynamisengine.spatialinput.api.pointer;

/** What produced a spatial pointer ray. */
public enum PointerSource {
    /** Derived from hand index finger direction. */
    HAND_POINT,
    /** Derived from controller aim pose. */
    CONTROLLER_AIM,
    /** Derived from head/gaze direction. */
    HEAD_GAZE
}
