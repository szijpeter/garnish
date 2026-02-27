# Garnish Launch Kit

## Positioning

Garnish is a Kotlin Multiplatform suite of small, explicit mobile system primitives. Instead of one large "kitchen sink" dependency, teams can add only the modules they need (`share`, `haptic`, `torch`, `screen`, `badge`, `clipboard`, `review`) with deterministic runtime behavior and clear API contracts.

## Why Modular Over Fat Dependencies

- Smaller dependency surface and faster adoption.
- Easier API discovery: one module, one job.
- Better long-term maintainability: each primitive can evolve independently.
- Lower integration risk for teams that only need one or two capabilities.

## Post Templates

### X (Twitter)

```text
Introducing Garnish: focused Kotlin Multiplatform primitives for mobile system tasks.

No kitchen sink dependency. Just pick what you need:
share, haptic, torch, screen, badge, clipboard, review.

Repo: https://github.com/szijpeter/garnish
```

### LinkedIn

```text
I’ve open sourced Garnish, a Kotlin Multiplatform library suite for common mobile system primitives.

The goal is simple: keep dependencies small and explicit. Instead of one large essentials package, Garnish ships focused modules teams can adopt independently.

Current modules:
- share
- haptic
- torch
- screen
- badge
- clipboard
- review

Feedback and contributions are welcome.
https://github.com/szijpeter/garnish
```

### Reddit (`r/Kotlin` / `r/androiddev`)

```text
I built Garnish: a modular KMP primitives suite (share, haptic, torch, screen, badge, clipboard, review)

I wanted thin, focused dependencies instead of one large essentials package. Each module is independent, with deterministic API behavior and Android/iOS support.

Would love feedback on API ergonomics and missing primitives.
Repo: https://github.com/szijpeter/garnish
```

## Launch Checklist

1. `./gradlew check apiCheck --no-daemon`
2. Publish first non-snapshot release to Maven Central.
3. Verify Maven Central coordinates resolve.
4. Confirm README badges and links are valid.
5. Make repository public.
6. Enable branch protections on `main`.
7. Add repository topics.
8. Publish launch posts.
9. Monitor issues and discussion for first-week feedback.

## Useful Links

- Repository: https://github.com/szijpeter/garnish
- Maven Central guide: ./MAVEN_CENTRAL.md
- Contributing: ../CONTRIBUTING.md
- Security policy: ../SECURITY.md
