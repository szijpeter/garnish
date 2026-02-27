# Maven Central Publishing

Garnish publishes with [`com.vanniktech.maven.publish`](https://vanniktech.github.io/gradle-maven-publish-plugin/central/) to Sonatype Central Portal.

## Coordinates

- Group: `io.github.szijpeter`
- Artifact pattern: `garnish-<module>`
- Package names: `dev.garnish.*`

Examples:

- `io.github.szijpeter:garnish-share`
- `io.github.szijpeter:garnish-review`
- `io.github.szijpeter:garnish-clipboard-compose`

## One-Time Setup

1. Create/login to [Central Portal](https://central.sonatype.com/).
2. Verify namespace ownership for `io.github.szijpeter`.
3. Confirm `GROUP=io.github.szijpeter` in [`gradle.properties`](../gradle.properties).
4. Create Central Portal user token.
5. Prepare GPG key and ASCII-armored private key export.

## GitHub Secrets

Set repository secrets:

- `MAVEN_CENTRAL_USERNAME`
- `MAVEN_CENTRAL_PASSWORD`
- `SIGNING_KEY` (ASCII-armored private key)
- `SIGNING_KEY_PASSWORD`

Optional setup with GitHub CLI:

```bash
gh secret set MAVEN_CENTRAL_USERNAME -R szijpeter/garnish
gh secret set MAVEN_CENTRAL_PASSWORD -R szijpeter/garnish
gh secret set SIGNING_KEY -R szijpeter/garnish
gh secret set SIGNING_KEY_PASSWORD -R szijpeter/garnish
```

## Release Mode

Current default is manual release from Central Portal.

- Workflow: [`.github/workflows/publish.yml`](../.github/workflows/publish.yml)
- Default input: `release_mode=publish-only`
- Optional: `release_mode=publish-and-release`

## First Stable Release Runbook

1. Set non-snapshot version in `gradle.properties` (`VERSION_NAME=x.y.z`).
2. Run validation locally:

```bash
./gradlew check apiCheck --no-daemon
```

3. Trigger GitHub `Publish` workflow with:
- `release_mode=publish-only`
- `version_name=x.y.z` (optional override)
4. Verify uploaded artifacts in Central Portal.
5. Release from Central Portal UI.
6. Verify coordinates resolve from Maven Central.
7. Tag release and update changelog.

## Maintainer Local Testing

`mavenLocal()` is maintainer-only for local iteration, not the primary consumer path.

```bash
./gradlew publishToMavenLocal --no-daemon
```

## Troubleshooting

- Namespace rejected: `GROUP` and verified namespace mismatch.
- 401/403: invalid Central token credentials.
- Signing failures: private key or passphrase mismatch.
- Missing docs/sources jars: module missing `garnishPublishing` plugin.
