# DynamisSpatialInput v1 Implementation Plan

Date: 2026-03-18
Status: PROPOSED — ready for implementation

---

# 1. Goal

Prove the canonical spatial input model with one real backend (Ultraleap).

Success = hand cursor + pinch-to-select demo with deterministic replay.

---

# 2. Implementation Phases

## Phase 1A — API Types + Module Scaffold

1. Parent POM with all 6 modules
2. `dynamisspatialinput-api` core types:
   - `Handedness` (enum: LEFT, RIGHT)
   - `TrackingState` (enum: NOT_TRACKED, TRACKED, INFERRED)
   - `TrackingConfidence` (enum: LOW, MEDIUM, HIGH)
   - `SpatialDeviceId` (record: provider name + device index)
   - `SpatialDeviceInfo` (record: id, display name, capabilities)
   - `Pose3D` (record: position vec3, orientation quat — using vectrix types)
   - `SpatialPointer` (record: origin, direction, confidence, handedness)
   - `TrackedHand` (record: handedness, trackingState, confidence, palmPose, palmVelocity, pinchStrength, grabStrength, pointingRay)
   - `SpatialInputFrame` (record: tick, timestamp, leftHand, rightHand, pointers)
   - `DerivedGesture` (sealed: PinchStarted, PinchHeld, PinchReleased, GrabStarted, GrabHeld, GrabReleased, PointActive, PointEnded)
     **NOTE:** DerivedGesture variants are provisional — semantics may be refined after first hardware integration. Do not treat as locked API until Ultraleap proving validates the gesture model.
3. Module-info.java for each module
4. Gate: compiles, types feel right

## Phase 1B — Core: Normalization + Gesture Derivation

1. `CoordinateNormalizer` — transform provider coordinate space to engine space
2. `PinchDetector` — hysteresis-based pinch state machine (threshold + release threshold)
3. `GrabDetector` — same pattern for grab
4. `PointDetector` — index finger extension detection
5. `GestureDeriver` — combines detectors, produces `List<DerivedGesture>` per frame
6. Gate: deterministic tests pass

## Phase 1C — Core: Filtering + Replay

1. `OneEuroFilter` — standard adaptive low-pass filter for pose smoothing
2. `ExponentialSmoothing` — simpler alternative
3. `SpatialRecorder` — captures `SpatialInputFrame` sequence with tick stamps
4. `SpatialRecording` — immutable recording data (list of frames)
5. `SpatialReplayer` — feeds recording back, verifies determinism
6. Gate: replay produces identical gesture output

## Phase 1D — Runtime

1. `SpatialBackend` (SPI interface) — mirrors AudioBackend pattern
2. `SpatialBackendCapabilities` (record)
3. `SpatialInputManager` — discovery, lifecycle, telemetry
4. `SpatialTelemetry` (record)
5. `NullSpatialBackend` — fake backend for CI
6. Gate: lifecycle state machine works, telemetry captures

## Phase 1E — Backend: Ultraleap

1. `UltraleapBackend` implements `SpatialBackend`
2. Panama FFM bindings to LeapC.dll / libLeapC.so / libLeapC.dylib
3. Frame polling: `LeapPollConnection` → `LEAP_TRACKING_EVENT` → internal raw frame
4. Map Ultraleap hand data → `TrackedHand`
5. Device connect/disconnect events
6. Gate: real hand tracking data flows through the pipeline

## Phase 1F — Testing + Proving

1. `FakeSpatialBackend` — generates synthetic hand tracking frames
2. `ChaosSpatialBackend` — jitter, tracking loss, noise
3. Determinism tests
4. Tracking loss/recovery tests
5. Gesture edge-case tests (near threshold, rapid transitions)
6. Proving artifact: hand cursor + pinch-to-select demo

---

# 3. v1 Type Summary

| Type | Package | Description |
|------|---------|-------------|
| `Handedness` | api.hand | LEFT, RIGHT |
| `TrackingState` | api.device | NOT_TRACKED, TRACKED, INFERRED |
| `TrackingConfidence` | api.device | LOW, MEDIUM, HIGH |
| `SpatialDeviceId` | api.device | Provider + index identity |
| `SpatialDeviceInfo` | api.device | Device descriptor |
| `Pose3D` | api.frame | Position + orientation |
| `SpatialPointer` | api.pointer | Ray origin + direction + metadata |
| `TrackedHand` | api.hand | Palm pose, pinch/grab strength, pointing ray |
| `SpatialInputFrame` | api.frame | Per-tick immutable spatial snapshot |
| `DerivedGesture` | api.gesture | Pinch/grab/point state changes |
| `SpatialBackend` | runtime | SPI for providers |
| `SpatialInputManager` | runtime | Lifecycle + telemetry |

---

# 4. Dependencies

```
dynamisspatialinput-api     → vectrix (vec3, quaternion)
dynamisspatialinput-core    → api
dynamisspatialinput-runtime → api, core
dynamisspatialinput-test    → api, core, runtime
dynamisspatialinput-backend-ultraleap → api (only)
```
