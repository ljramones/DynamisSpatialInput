# DynamisSpatialInput Roadmap

Date: 2026-03-18
Status: PROPOSED — Versioned roadmap for subsystem evolution
Scope: Spatial input system (hand tracking, tracked controllers, gesture semantics, spatial interaction)

---

# 1. Purpose

This document defines the versioned development roadmap for DynamisSpatialInput.

The goal of the subsystem is to provide:

- Canonical, engine-owned spatial input models
- Device-independent abstraction for hands, controllers, and future tracked devices
- Deterministic, replayable spatial input frames
- Gesture derivation and interaction primitives
- Modular backend integration (Ultraleap, OpenXR, future providers)

---

# 2. Versioning Philosophy

Each version must:

- Introduce a coherent capability layer
- Include a proving artifact (demo or validation scenario)
- Preserve determinism and replayability
- Avoid premature abstraction or overbuild

```
v1 → foundation (hand tracking)
v2 → semantics (gesture + interaction)
v3 → platform breadth (OpenXR, controllers)
v4 → tooling + developer experience
v5 → advanced spatial interaction frontier
```

---

# 3. Version 1 — Canonical Hand Input Foundation

**Goal:** Establish a canonical spatial input model independent of any vendor SDK.

## Modules

- `dynamisspatialinput-api`
- `dynamisspatialinput-core`
- `dynamisspatialinput-runtime`
- `dynamisspatialinput-test`
- `dynamisspatialinput-backend-ultraleap`

## Core Types

- `SpatialInputFrame`, `TrackedHand`, `SpatialPointer`
- `Handedness`, `TrackingState`, `TrackingConfidence`
- `SpatialDeviceId`, `SpatialDeviceInfo`

## Hand Model (minimal)

- Handedness, tracking state, confidence
- Palm pose (position + orientation), palm velocity
- Pinch strength, grab strength, pointing ray

## Runtime

- Backend discovery (ServiceLoader), single active session
- Lifecycle: INITIALIZED → RUNNING → FAULTED → CLOSED
- Telemetry snapshot, deterministic frame generation

## Replay

- Record/replay `SpatialInputFrame`
- Deterministic tick-aligned playback

## Backend: Ultraleap first

## Testing: Fake backend, determinism, jitter/noise, tracking loss

## Out of Scope: OpenXR, controllers, advanced gestures, full joint skeleton, haptics

## Proving Artifact: Hand cursor + pinch-to-select

## Success Criteria

- Canonical frame stable across sessions
- Replay deterministic
- Pinch/select demo works reliably
- System resilient to tracking loss

---

# 4. Version 2 — Gesture Semantics and Interaction Primitives

**Goal:** Provide stable, gameplay-usable gesture semantics.

## Scope

- Gesture states: pinch/grab/point started/held/released, open hand, fist
- Stability: hysteresis, smoothing, debouncing, confidence-aware suppression
- Expanded hand: wrist pose, fingertip positions, optional reduced joints
- Interaction primitives: spatial pointer, hand cursor, poke, hover, grab candidates
- Bridge to DynamisInput action system

## Out of Scope: OpenXR, controllers, gesture authoring, multi-user

## Proving Artifact: Stable gesture-driven object interaction

---

# 5. Version 3 — OpenXR and Tracked Controller Expansion

**Goal:** Multi-device spatial input system.

## Scope

- `dynamisspatialinput-backend-openxr`
- `TrackedController`: grip/aim pose, trigger/grip analog, buttons
- Optional head pose
- Mixed device support (hands + controllers), fallback
- Capability model per provider

## Out of Scope: Advanced haptics, full body, authoring tools

## Proving Artifact: Quest/OpenXR demo with controllers + hands sharing canonical frame

---

# 6. Version 4 — Tooling and Developer Experience

**Goal:** Make the system practical and debuggable.

- Debug visualization (skeleton, rays, confidence, gesture timeline)
- Telemetry (tracking quality, gesture rates, pointer stability)
- Replay tooling (scrubbing, inspection, regression comparison)
- Authoring support (threshold presets, smoothing presets)

---

# 7. Version 5 — Advanced Spatial Interaction

**Goal:** Enable novel interaction paradigms.

- Additional providers, full articulated models
- Bimanual gestures, gesture language/sequences
- Haptics abstraction, spatial UI contracts, tracked tools

---

# 8. Progression Rules

- Do not start v2 until v1 canonical model is stable and replay works
- Do not start v3 until v2 gesture semantics feel stable
- Do not start v4 until at least one backend is proven
- Do not start v5 until the subsystem is stable and developer-friendly

---

# 9. Summary

| Version | Focus |
|---------|-------|
| v1 | Canonical hand tracking foundation |
| v2 | Gesture semantics and interaction |
| v3 | OpenXR + controllers |
| v4 | Tooling and debugging |
| v5 | Advanced spatial interaction |
