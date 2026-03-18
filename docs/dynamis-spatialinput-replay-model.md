# DynamisSpatialInput Replay Model

Date: 2026-03-18
Status: PROPOSED

---

# 1. Purpose

Spatial input replay enables:

- Deterministic testing without hardware
- Regression testing (same hand motion → same gesture output)
- Debugging (scrub through a recorded session)
- Tuning (replay same motion, adjust thresholds, compare results)

---

# 2. What Is Recorded

A `SpatialRecording` is a sequence of `SpatialInputFrame` records with tick stamps:

```
[tick=1, frame], [tick=2, frame], [tick=3, frame], ...
```

Each `SpatialInputFrame` contains:

- Tick number
- Optional<TrackedHand> leftHand
- Optional<TrackedHand> rightHand
- List<SpatialPointer> pointers

This is the canonical frame — post-normalization, pre-gesture-derivation.

---

# 3. What Is NOT Recorded

- Raw vendor frames (Ultraleap LEAP_HAND, OpenXR poses)
- Derived gestures (these are re-derived from replayed frames)
- Filtered/smoothed values (these are re-computed from replayed frames)

This means replay exercises the full pipeline from canonical frame through gesture derivation, proving that the derivation logic is deterministic.

---

# 4. Determinism Contract

Given the same `SpatialRecording` replayed through the same `GestureDeriver` configuration:

- The same `DerivedGesture` sequence is produced
- The same `SpatialPointer` values are computed
- The same filtering output is produced (if filters are tick-based, not wall-clock)

This requires:

- No wall-clock time in gesture detection (use tick count)
- No random state in filtering (use deterministic coefficients)
- No external state dependency in derivation

---

# 5. Recording API

```java
SpatialRecorder recorder = new SpatialRecorder();
recorder.record(spatialInputFrame);  // call each tick
SpatialRecording recording = recorder.finish();
```

---

# 6. Replay API

```java
SpatialReplayer replayer = new SpatialReplayer(recording, gestureDeriver);
for (long tick = recording.startTick(); tick <= recording.endTick(); tick++) {
    SpatialInputFrame frame = replayer.frameAt(tick);
    List<DerivedGesture> gestures = replayer.gesturesAt(tick);
}
```

---

# 7. Serialization (Future)

v1: in-memory recording only (list of records).
v2+: serialization to disk (JSON or binary format) for saved test fixtures and regression suites.
