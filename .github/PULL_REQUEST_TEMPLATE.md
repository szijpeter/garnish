## Summary

Describe what changed and why.

## Validation

- [ ] `./gradlew check --no-daemon`
- [ ] `./gradlew apiCheck --no-daemon` (if public API changed)
- [ ] Android instrumented tests (if OS-integrated behavior changed)

## Checklist

- [ ] Tests added or updated
- [ ] Docs updated (`README.md` and affected module README)
- [ ] `.api` dumps updated when public signatures changed
- [ ] Changelog updated when behavior or API changed
- [ ] No silent no-op runtime behavior introduced
