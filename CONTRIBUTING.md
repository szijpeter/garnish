# Contributing to Garnish

Thanks for your interest in contributing! Here's how to get started.

## Setup

1. Clone the repo
2. Open in Android Studio (latest stable) or IntelliJ IDEA
3. Sync Gradle — the `build-logic` convention plugins will be resolved automatically

## Code Style

- Follow the `.editorconfig` settings
- All library modules use **explicit API mode** — every public declaration needs an explicit visibility modifier
- Write KDoc for all public symbols

## Making Changes

1. Create a branch from `main`
2. Make your changes
3. Run `./gradlew check` to verify all tests pass
4. Run `./gradlew apiDump` if you changed public API, review the `.api` file diff
5. If your change touches OS-integrated behavior, run `./gradlew :androidApp:connectedDebugAndroidTest`
6. Open a PR

## PR Checklist

- [ ] Code compiles on both Android and iOS
- [ ] Tests added/updated
- [ ] KDoc on public API
- [ ] `apiDump` updated if public API changed
- [ ] Platform/instrumented tests added for platform adapter behavior changes
- [ ] CHANGELOG.md updated
