# Garnish Ecosystem Audit (2026-02-26)

## Positioning
- Keep Garnish as a **modular primitives suite**.
- Do not collapse into a single dependency by default.
- Add an optional BOM/meta-artifact later for teams that want one-line adoption.
- Freeze module scope until v1.0; prioritize quality hardening over breadth expansion.

## Definitive Portfolio Decision

### Keep
- `share` / `share-compose`
- `haptic` / `haptic-compose`
- `torch` / `torch-compose`
- `screen` / `screen-compose`
- `badge`
- `clipboard` / `clipboard-compose`
- `review`

Reason:
- All modules solve common platform friction.
- `torch`, `screen`, and `badge` still show weaker direct KMP competition than higher-level features.

### Drop
- None right now.

Reason:
- Current surface is still lean.
- No module looks redundant enough to remove yet.

### Add (priority order)
- None before v1.0.

Post-v1.0 candidate tracks:
1. Low-competition-first gaps:
   - `network-connectivity`
   - `file-picker` / storage bridge
   - `biometric` (if coverage remains thin)
2. High-demand/crowded categories (only with clear differentiation):
   - `permissions`
   - `notifications`
   - `secure-storage`

## Competitor Snapshot (verified 2026-02-26)

### Suite-level
- KmpEssentials: https://github.com/Ares-Defence-Labs/KmpEssentials
  - Broad "40+ modules" positioning, active updates.

### Direct/near module competitors
- Share:
  - https://github.com/software-mansion/kmp-sharing
- Haptic:
  - https://github.com/xfqwdsj/multihaptic
  - https://github.com/LexiLabs-App/basic-haptic
- Review (low traction):
  - https://github.com/santimattius/kmp-store-review

### High-demand missing primitives (strong ecosystem signal)
- Permissions:
  - https://github.com/icerockdev/moko-permissions
- Notifications:
  - https://github.com/mirzemehdi/KMPNotifier
- File handling:
  - https://github.com/vinceglb/FileKit
- Secure storage:
  - https://github.com/ioannisa/KSafe
- Curated ecosystem index:
  - https://github.com/terrakok/kmp-awesome

## Strategic Call
- Differentiation should be:
  - tiny dependency slices,
  - strict API stability,
  - predictable behavior under platform edge cases,
  - clean split: primitive APIs vs Compose adapters.

- This keeps migration from "all-in-one" suites easy while preserving small dependency footprints.
- For v0.x, avoid module sprawl: quality and trust signal come first.
