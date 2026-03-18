# DynamisSpatialInput Architecture

Date: 2026-03-18
Status: PROPOSED — initial architecture for v1

---

# 1. Mission

A modular engine subsystem for tracked spatial devices, canonical hand/controller/pose models, gesture derivation, and replayable spatial interaction.

---

# 2. Module Structure

```
DynamisSpatialInput/
├── dynamisspatialinput-api/           ← Public contracts, canonical types
├── dynamisspatialinput-core/          ← Normalization, gesture derivation, filtering, replay
├── dynamisspatialinput-runtime/       ← Subsystem lifecycle, backend management, telemetry
├── dynamisspatialinput-test/          ← Fake backend, chaos tests, replay tests
├── dynamisspatialinput-backend-ultraleap/  ← Ultraleap SDK integration
└── dynamisspatialinput-backend-openxr/     ← OpenXR integration (v3)
```

---

# 3. Module Dependency DAG

```
dynamisspatialinput-api                    (no internal deps; depends on vectrix)
    ↑
dynamisspatialinput-core                   (depends on api)
    ↑
dynamisspatialinput-runtime                (depends on api + core)
    ↑
dynamisspatialinput-test                   (depends on all above)

dynamisspatialinput-backend-ultraleap      (depends on api only)
dynamisspatialinput-backend-openxr         (depends on api only)
```

Backends depend ONLY on api. They do not depend on core or runtime.

---

# 4. Data Flow Pipeline

```
Provider Backend (Ultraleap/OpenXR)
    │ raw vendor frame
    ▼
Internal Raw Frame (provider-specific intermediate)
    │
    ▼
Normalization Layer (core)
    │ coordinate transform, unit normalization
    ▼
Canonical SpatialInputFrame (api)
    │
    ├──→ Gesture Derivation (core) → DerivedGesture states
    ├──→ Pointer Derivation (core) → SpatialPointer rays
    ├──→ Filtering/Smoothing (core) → stabilized values
    │
    ▼
Runtime Snapshot
    │
    ├──→ Replay Recorder
    ├──→ Telemetry
    └──→ Game/UI consumption
```

---

# 5. Package Structure

```
org.dynamisengine.spatialinput.api
├── frame/       SpatialInputFrame
├── hand/        TrackedHand, Handedness, FingerJoint
├── controller/  TrackedController
├── gesture/     DerivedGesture, GestureState
├── pointer/     SpatialPointer, PointerSource
├── device/      SpatialDeviceId, SpatialDeviceInfo, TrackingState, TrackingConfidence

org.dynamisengine.spatialinput.core
├── normalize/   CoordinateNormalizer, UnitConverter
├── gesture/     GestureDeriver, PinchDetector, GrabDetector
├── filter/      OneEuroFilter, ExponentialSmoothing
├── replay/      SpatialRecorder, SpatialReplayer, SpatialRecording

org.dynamisengine.spatialinput.runtime
├──              SpatialInputRuntime, SpatialInputManager
├──              SpatialBackend (SPI), SpatialBackendCapabilities
├──              SpatialTelemetry
```

---

# 6. Core Invariants

**INV-1: Vendor firewall.**
No vendor SDK types appear in the public API. All provider data is mapped through the internal raw model into canonical types.

**INV-2: Deterministic frames.**
Given the same raw input sequence, the same `SpatialInputFrame` is produced. Filtering/smoothing must be deterministic (seeded or tick-based, no wall-clock).

**INV-3: Replay-safe.**
Every `SpatialInputFrame` can be recorded and replayed. Gesture derivation from replayed frames produces identical results.

**INV-4: Backend = mechanism, not policy.**
Backends map vendor data, manage native sessions, and fire events. They do not derive gestures, smooth values, or make interaction decisions.

**INV-5: Tracking loss is normal.**
The system must handle tracking loss and recovery gracefully. No crashes, no stale data presented as fresh, no undefined behavior during loss.

**INV-6: Frame rate independence.**
The spatial frame model must work correctly regardless of provider polling rate. Interpolation/extrapolation is explicit, not implicit.

---

# 7. Runtime Lifecycle

```
UNINITIALIZED ──initialize()──→ INITIALIZED ──start()──→ RUNNING
RUNNING ──tracking loss──→ RUNNING (degraded, hand absent)
RUNNING ──provider fault──→ FAULTED
RUNNING ──stop()──→ CLOSED
FAULTED ──shutdown()──→ CLOSED
```

Simpler than Audio because spatial input does not have device hot-swap in the same sense — the provider session is long-lived, and tracking loss/recovery is handled within RUNNING state.

---

# 8. Threading Model

| Thread | Responsibility |
|--------|---------------|
| Engine tick thread | Polls provider, produces SpatialInputFrame, evaluates gestures |
| Provider poll thread (if async) | Backend-specific; delivers raw frames to a queue |
| Game consumption | Reads latest SpatialInputFrame (immutable record) |

The v1 design is synchronous: the engine tick thread polls the backend and produces the frame. Async backends (if needed) queue raw frames for the tick thread to drain.

---

# 9. Compatibility with DynamisInput

DynamisSpatialInput and DynamisInput are sibling subsystems, not parent-child:

- Both produce per-tick immutable frame records
- Both support deterministic replay
- Both have independent backend/provider models
- v2+ may bridge spatial gestures to DynamisInput actions via a mapping layer
- No circular dependency between the two
