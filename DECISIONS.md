# Garnish Decisions

This document records stable product and API decisions to reduce repeated debates.

## Portfolio Scope (v0.x)

- Keep current modules:
  - `share`, `share-compose`
  - `haptic`, `haptic-compose`
  - `torch`, `torch-compose`
  - `screen`, `screen-compose`
  - `badge`
  - `clipboard`, `clipboard-compose`
  - `review`
- Do not add new modules before v1.0.
- Maintain two post-v1.0 candidate tracks:
  1. Low-competition ecosystem gaps.
  2. High-demand but crowded categories only when Garnish can be materially smaller/cleaner.

## API Behavior Principles

- Explicit API mode is mandatory for all published modules.
- Failures must be deterministic:
  - No silent no-op fallbacks for operational failures.
  - Throw typed module exceptions for runtime platform failures.
- Platform request semantics must be precise:
  - `ReviewResult.Requested` means platform API accepted a request.
  - It does not guarantee dialog visibility.
- Compose wrappers should reflect stable contracts:
  - `rememberTorch()` returns a non-null `Torch`.
  - Consumers check `torch.isAvailable` for capability.

## Testing & CI Principles

- Every platform adapter behavior change requires platform-level tests.
- Keep targeted Android instrumented tests for OS-integrated paths:
  - share chooser launch,
  - badge channel/setup path,
  - in-app review request path.
- CI baseline:
  - `./gradlew check --no-daemon`
  - `./gradlew apiCheck --no-daemon`
  - lint with zero errors.
- Keep a scheduled/on-demand emulator workflow for connected Android tests.
