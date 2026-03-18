# DynamisSpatialInput

**DynamisSpatialInput** is a modular engine subsystem for **tracked spatial input and embodied interaction**.

It provides a canonical, device-independent model for:

- Hands (pose, pinch, grab, pointing)
- Tracked controllers (pose, triggers, buttons)
- Spatial pointers and rays
- Derived interaction semantics
- Deterministic, replayable spatial input frames

---

## Purpose

Traditional input systems (keyboard, mouse, gamepad) operate on **discrete controls**.

Spatial input introduces a different paradigm:

> **continuous, tracked, high-dimensional interaction**

DynamisSpatialInput exists to:

- Define **engine-owned spatial input models** (not vendor models)
- Unify multiple providers (Ultraleap, OpenXR, future devices)
- Support **deterministic replay and testing**
- Enable **gesture-based and spatial interaction systems**
- Provide a foundation for **next-generation input paradigms**

---

## Design Philosophy

DynamisSpatialInput follows the same principles as the rest of Dynamis:

- **Deterministic** — all state evolves per tick, not wall-clock
- **Replayable** — spatial input can be recorded and reproduced exactly
- **Modular** — providers are pluggable backends
- **Observable** — telemetry and debugability are first-class
- **Vendor-independent** — SDKs are adapters, not public API

---

## Architecture Overview

```
Provider Backend (Ultraleap / OpenXR)
        |
Raw Provider Frame
        |
Normalization (core)
        |
SpatialInputFrame (canonical model)
        |
Derived Semantics (gestures, pointers)
        |
Consumed by engine systems (UI, gameplay, tools)
```

---

## Modules

```
dynamisspatialinput-api               <- Public types and canonical spatial input model
    |
dynamisspatialinput-core              <- Normalization, filtering, gesture derivation
    |
dynamisspatialinput-runtime           <- Lifecycle, backend management, telemetry
    |
dynamisspatialinput-test              <- Fake backends, replay, chaos/stress tests

dynamisspatialinput-backend-ultraleap <- Ultraleap (Leap Motion) provider (depends on api only)
dynamisspatialinput-backend-openxr    <- OpenXR provider (depends on api only)
```

Backends depend ONLY on the API module. No vendor types leak into the public engine interface.

---

## Core Concepts

### SpatialInputFrame

The canonical per-tick snapshot of spatial input:

```
SpatialInputFrame
  - long tick
  - Optional<TrackedHand> leftHand
  - Optional<TrackedHand> rightHand
  - List<SpatialPointer> pointers
  - List<DerivedGesture> gestures
```

This is the **engine truth**, independent of device or SDK.

### TrackedHand

Represents a single tracked hand:

- Handedness (left/right)
- Tracking state and confidence
- Palm pose (position + orientation) and velocity
- Pinch strength [0.0, 1.0]
- Grab strength [0.0, 1.0]
- Pointing ray

### SpatialPointer

A generalized pointing construct:

- Origin + direction
- Confidence
- Source (hand point, controller aim, head gaze)

Used for UI interaction, object selection, and ray-based systems.

### DerivedGesture

Engine-level interaction signals derived from raw input:

- Pinch (started / held / released)
- Grab (started / held / released)
- Point (active / ended)

These are **derived by the engine**, not vendor-provided. Semantics are provisional until validated against real hardware.

---

## Roadmap

| Version | Focus | Status |
|---------|-------|--------|
| v1 | Canonical hand tracking + Ultraleap backend | Phase 1A complete |
| v2 | Gesture semantics + interaction primitives | Planned |
| v3 | OpenXR + tracked controllers | Planned |
| v4 | Tooling, debugging, replay inspection | Planned |
| v5 | Advanced spatial interaction frontier | Planned |

See `docs/dynamis-spatialinput-roadmap.md` for the full versioned roadmap with progression rules.

---

## Testing Strategy

DynamisSpatialInput is designed to be **tested without hardware**.

- Fake backend for deterministic input
- Chaos testing (noise, tracking loss, jitter)
- Replay verification for determinism
- Stress testing for frame stability

---

## Relationship to Other Subsystems

| Subsystem | Role |
|-----------|------|
| DynamisWindow | Raw OS/device capture (keyboard, mouse, gamepad events) |
| DynamisInput | Traditional input grammar (action mapping, snapshots, replay) |
| DynamisSpatialInput | Tracked spatial interaction (hands, controllers, gestures) |

These three form the **input ecosystem**. They are adjacent but independent — no circular dependencies.

---

## Documentation

| Document | Purpose |
|----------|---------|
| `docs/dynamis-spatialinput-roadmap.md` | Versioned roadmap (v1-v5) with progression rules |
| `docs/dynamis-spatialinput-architecture.md` | Module structure, data flow, invariants, threading |
| `docs/dynamis-spatialinput-authority-model.md` | Ownership boundaries and vendor firewall |
| `docs/dynamis-spatialinput-v1-plan.md` | Phase-by-phase v1 implementation plan |
| `docs/dynamis-spatialinput-replay-model.md` | Recording and replay determinism contract |

---

## Build

Requires JDK 25 with preview features, Maven 3.9+.

```bash
cd Layer6-Game-Services/DynamisSpatialInput
mvn clean install
```

---

## Guiding Principle

> **Concrete first, abstract later.**

Start with real devices, define canonical models, and expand only when justified by real usage.

---

## License

Apache 2.0
