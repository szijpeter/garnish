# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).

## [0.1.0-SNAPSHOT] — Unreleased

### Added
- **Build infrastructure**: AGP 9.0.1, Kotlin 2.3.10, Compose Multiplatform 1.10.1, Gradle 9.1.0
- **Convention plugins**: `garnish.kmp`, `garnish.cmp`, `garnish.publishing` in `build-logic/`
- **share** / **share-compose**: Cross-platform share sheet (text, URL, image, file)
- **haptic** / **haptic-compose**: Haptic feedback (7 types: Click, DoubleClick, HeavyClick, Tick, Reject, Success, Warning)
- **torch**: Flashlight control without camera preview
- **screen** / **screen-compose**: Brightness, keep-screen-on, orientation lock with `KeepScreenOn()` and `LockOrientation()` effects
- **badge**: App icon badge count management
- **clipboard** / **clipboard-compose**: Rich clipboard (PlainText, Html, Uri, Image)
- **review**: In-app review (iOS: SKStoreReviewController, Android: Google Play In-App Review API)
- **Sample app** with interactive demos for Share, Haptic, and Clipboard
- **CI**: GitHub Actions workflows for check (android + ios) and publish

### Changed
- **review**:
  - `ReviewResult.Shown` renamed to `ReviewResult.Requested`
  - Added concrete `NotAvailable` mapping branches for Android/iOS availability checks
- **torch-compose**:
  - `rememberTorch()` now returns non-null `Torch`
  - Availability is represented only by `torch.isAvailable`
- **Failure semantics**:
  - Added typed runtime exceptions in `share`, `badge`, and `torch`
  - Removed silent no-op fallbacks for key runtime failure paths
- **clipboard (iOS)**:
  - Preserves HTML using `public.html` pasteboard type with plain-text fallback
- **Publishing**:
  - Switched `garnish.publishing` to `com.vanniktech.maven.publish` for Central Portal compatibility
  - Added Maven Central workflow modes: `publish-only` and `publish-and-release`
  - Added maintainer publishing guide at `docs/MAVEN_CENTRAL.md`

### Fixed
- Android launcher resources:
  - Removed obsolete `-v24` / `-v26` resource folder usage for minSdk 26
  - Added monochrome adaptive icon layer to satisfy modern launcher lint checks

### Testing
- Added iOS unit tests for review mode resolution and clipboard content precedence.
- Added Android instrumented tests for share chooser launch, review request path, and badge channel setup.
- Added dedicated Android emulator CI workflow for connected tests.
