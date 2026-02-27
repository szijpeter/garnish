# Garnish Agent Guide

## Project Goal
- Build small, focused Kotlin Multiplatform primitives for common mobile system tasks.
- Prefer thin modules over a single "kitchen sink" dependency.
- Keep APIs explicit, stable, and easy to adopt independently.

## Module Layout
- Primitive modules: `share`, `haptic`, `torch`, `screen`, `badge`, `clipboard`, `review`
- Compose wrappers: `*-compose` modules
- Build conventions: `build-logic/convention`
- Sample apps: `composeApp`, `androidApp`, `iosApp`

## Local Workflow
- Fast confidence pass: `./gradlew check --no-daemon`
- API compatibility: `./gradlew apiCheck --no-daemon`
- Publish preflight locally: `./gradlew publishToMavenLocal --no-daemon`
- Publish to Central Portal: `./gradlew publishToMavenCentral --no-daemon`
- iOS klib smoke build: `./gradlew :composeApp:iosSimulatorArm64MainKlibrary --no-daemon`

## Quality Rules
- Public API must stay explicit and documented.
- No placeholder runtime behavior in production APIs.
- Favor deterministic behavior over silent fallbacks.
- Do not silently swallow runtime platform failures in production APIs.
  Throw typed module exceptions so callers can handle failure paths explicitly.
- Keep modules decoupled: a module should not force unrelated dependencies.

## Testing Strategy (System Primitives)
- `commonTest`: API contracts, invariants, pure mapping logic.
- Platform unit tests (`androidUnitTest`, iOS tests): platform adapter behavior and edge cases.
- Every behavior change in a platform adapter must include a platform test in that module.
- Instrumented/UI tests: only for OS-integrated flows that cannot be validated in pure unit tests
  (share sheet launch, review prompt request path, permission interactions).
- Keep targeted Android instrumented coverage for share chooser launch, badge channel wiring,
  and in-app review request path.
- Screenshot tests: optional for sample app demos, not for primitive module correctness.

## Known Pitfalls And Fixes
- Android camera permission lint:
  - If `CAMERA` permission is declared, also declare
    `<uses-feature android:name="android.hardware.camera" android:required="false" />`.
- Compose context assumptions:
  - `LocalContext.current` is not always an `Activity`.
  - Resolve activity through `ContextWrapper` chain and fail with a clear message if absent.
- Compose `remember` pitfalls:
  - For Android adapters created from `LocalContext` / `LocalView`, key `remember(...)`
    with that input (`remember(context)`, `remember(view)`) to avoid stale instances.
- iOS in-app review:
  - Prefer `SKStoreReviewController.requestReviewInScene(...)` when a `UIWindowScene` is available.
- Review result semantics:
  - `ReviewResult.Requested` means request submission accepted by platform API, not guaranteed UI display.
  - Return `ReviewResult.NotAvailable` for concrete platform unavailability branches.
- iOS byte bridging:
  - Avoid `ByteArray.refTo(0)` on empty arrays when creating `NSData` — handle empty arrays explicitly.
- File sharing safety:
  - Sanitize user-provided file names before writing temp files (drop path segments).
- Torch compose contract:
  - `rememberTorch()` is non-null. Use `torch.isAvailable` as the capability check.
- Clipboard HTML parity:
  - Preserve HTML with `public.html` on iOS and keep plain-text fallback for interoperability.

## CI Expectations
- `check` must remain green on Android and iOS jobs.
- Keep `api` dumps updated when public signatures change.
- Keep Android instrumented tests runnable in CI (scheduled or on-demand emulator lane).
- Do not merge behavior changes without tests that fail before and pass after.

## Pre-Publish Checklist
- Run `check`, `apiCheck`, and a local sample launch.
- Verify docs in module `README.md` files match actual behavior.
- Confirm Maven coordinates, version, and POM metadata are correct.
- Follow `docs/MAVEN_CENTRAL.md` for namespace, credentials, signing, and release steps.
