# DynamisSpatialInput Authority Model

Date: 2026-03-18
Status: RATIFIED — governs subsystem boundaries

---

# 1. What DynamisSpatialInput Owns

| Domain | Authority |
|--------|-----------|
| Canonical spatial input frame model | `SpatialInputFrame`, `TrackedHand`, `TrackedController` |
| Pose normalization | Provider-raw → canonical coordinate space |
| Gesture derivation | Pinch/grab/point from raw tracking data |
| Tracking state management | Confidence, tracking loss/recovery |
| Spatial pointer derivation | Pointing rays from hands and controllers |
| Smoothing and filtering | Noise reduction, jitter suppression |
| Backend lifecycle | Provider discovery, session management, fault handling |
| Replay and recording | Deterministic spatial frame capture and playback |
| Spatial input telemetry | Tracking quality, gesture rates, provider health |

---

# 2. What DynamisSpatialInput Does NOT Own

| Domain | Owner | Reason |
|--------|-------|--------|
| OS window events | DynamisWindow | Window/OS-level input capture |
| Keyboard/mouse/gamepad mapping | DynamisInput | Traditional gameplay input grammar |
| Action binding grammar (tap/hold/chord) | DynamisInput | Conventional input semantics |
| World interaction outcomes | Game logic / DynamisScripting | Spatial input proposes, game decides |
| UI widget behavior | DynamisUI | UI consumes spatial pointers, owns widget logic |
| Rendering of hands/controllers | DynamisLightEngine | Visual representation is rendering's domain |
| Physics interaction | DynamisPhysics | Physical collision from hand/controller volumes |
| Scene graph placement | DynamisSceneGraph | Spatial input reads scene, does not mutate it |

---

# 3. Ecosystem Boundary

```
DynamisWindow           → conventional raw OS/window/device input
DynamisInput            → keyboard/mouse/gamepad action mapping, snapshots, replay
DynamisSpatialInput     → tracked spatial entities, gesture derivation, spatial interaction
```

These three form the **input ecosystem**. They are adjacent but independent:

- DynamisWindow feeds raw events to DynamisInput
- DynamisSpatialInput operates independently (its own backends, its own frame model)
- DynamisInput and DynamisSpatialInput may share action IDs for bridge bindings (v2+)
- No circular dependencies between the three

---

# 4. Backend Responsibilities

Backends (Ultraleap, OpenXR) are mechanism, not policy:

| Must | Must Not |
|------|----------|
| Map vendor data to internal raw frame model | Define canonical types |
| Report provider capabilities | Implement gesture semantics |
| Manage native session lifecycle | Make policy decisions |
| Fire device connect/disconnect events | Hold references to runtime |
| Clean up native resources on close | Allocate on the hot path |

---

# 5. The Vendor Firewall

**No vendor types may appear in the public engine API.**

The subsystem defines its own canonical model:

```
Ultraleap SDK types → UltraleapBackend (maps to internal raw) → Core normalizer → SpatialInputFrame
OpenXR types        → OpenXRBackend    (maps to internal raw) → Core normalizer → SpatialInputFrame
```

Game code never sees Ultraleap or OpenXR types. Only `SpatialInputFrame`, `TrackedHand`, `SpatialPointer`, etc.
