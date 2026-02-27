# Garnish Testing Strategy

## Goal
Validate platform primitives with the cheapest test that can prove correctness.

## Current Baseline (2026-02-26)
- Build and checks are green across Android + iOS simulator.
- Coverage is currently weighted toward `commonTest` contract/enumeration tests.
- Platform test coverage has been expanded, with additional work still expected
  for every adapter behavior change.

## Test Layers

### 1) Common unit tests (required for every module)
- Verify API contracts, enums, sealed classes, and pure mapping logic.
- No platform runtime assumptions.
- Fast, deterministic, always on in CI.

### 2) Platform unit tests (required for non-trivial adapters)
- Android unit tests for intent/config wiring, permission gates, and error paths.
- iOS tests for API branching and fallback logic.
- Use small wrappers around platform calls to enable mocking/faking.
- Requirement: behavior changes in platform adapters must include at least one
  platform test in that module.

### 3) Instrumented/device tests (targeted, high value only)
- Required where OS behavior cannot be trusted from pure unit tests:
  - launching share sheets,
  - torch toggling on real hardware,
  - review flow request path,
  - badge interactions with notification channel setup.
- Keep these minimal and smoke-oriented.
- Keep connected Android tests runnable through a dedicated emulator CI lane.

### 4) UI/screenshot tests (optional)
- Use only for sample/demo app regressions.
- Do not treat screenshot tests as primitive correctness coverage.

## Per-Module Guidance
- `share`: instrumented tests for chooser launch + URI grants.
- `haptic`: platform tests for haptic type mapping; optional device smoke.
- `torch`: device smoke test on hardware; verify permission-denied behavior.
- `screen`: platform tests for orientation/brightness API mapping.
- `badge`: platform tests for channel/config + clear behavior.
- `clipboard`: platform tests for supported content and unsupported/image behavior.
- `review`: platform tests for scene/activity selection and fallback.

## CI Recommendations
- Run `./gradlew check` on every PR.
- Keep dedicated iOS test execution in CI (not only klib compilation).
- Keep API checks (`apiCheck`) in CI gates.
- Keep nightly/on-demand Android emulator lane for instrumented tests.
- Add hardware device tests only where emulator behavior is insufficient.
