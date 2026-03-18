# DynamisSpatialInput

Tracked spatial input for the Dynamis World Engine.

Canonical hand/controller/pose models, gesture derivation, spatial pointers, and replayable spatial interaction frames. Modular backend architecture with Ultraleap and OpenXR providers.

---

## Module Map

```
dynamisspatialinput-api               ← canonical types, public contracts (no implementation)
    ↑
dynamisspatialinput-core              ← normalization, gesture derivation, filtering, replay
    ↑
dynamisspatialinput-runtime           ← subsystem lifecycle, backend management, telemetry
    ↑
dynamisspatialinput-test              ← fake backend, chaos tests, determinism tests

dynamisspatialinput-backend-ultraleap ← Ultraleap SDK integration (depends on api only)
dynamisspatialinput-backend-openxr    ← OpenXR integration (depends on api only)
```

Backends depend ONLY on the API module. No vendor types leak into the public engine interface.

---

## Status

| Version | Focus | Status |
|---------|-------|--------|
| v1 | Canonical hand tracking + Ultraleap | PLANNED |
| v2 | Gesture semantics + interaction | PLANNED |
| v3 | OpenXR + tracked controllers | PLANNED |

See `docs/` for architecture, authority model, roadmap, and v1 plan.

---

## Build

```bash
cd Layer6-Game-Services/DynamisSpatialInput
mvn clean install
```

Requires JDK 25, Maven 3.9+.
