# Maven Central Publishing

This project publishes with [`com.vanniktech.maven.publish`](https://vanniktech.github.io/gradle-maven-publish-plugin/central/), targeting Sonatype Central Portal.

## 1. One-time prerequisites

1. Create/login to Sonatype Central Portal:
   - https://central.sonatype.com/
2. Claim a namespace you can verify.
3. Ensure `GROUP` in [`gradle.properties`](/Users/szipe/lvc/dev/projects/garnish/gradle.properties) matches that approved namespace.
   - Current value is `io.github.szijpeter`.
   - If your approved namespace differs, change `GROUP` before release.
4. Generate Central Portal user token (username + password).
5. Generate/choose a GPG key and export the ASCII-armored private key.

## 2. GitHub secrets required

Configure these repository secrets:

- `MAVEN_CENTRAL_USERNAME`
- `MAVEN_CENTRAL_PASSWORD`
- `SIGNING_KEY` (ASCII-armored private key)
- `SIGNING_KEY_PASSWORD`

The workflow maps them to Gradle properties expected by the publish plugin.

### Optional: set secrets via GitHub CLI

```bash
gh auth login -h github.com

gh secret set MAVEN_CENTRAL_USERNAME -R szijpeter/garnish
gh secret set MAVEN_CENTRAL_PASSWORD -R szijpeter/garnish
gh secret set SIGNING_KEY -R szijpeter/garnish
gh secret set SIGNING_KEY_PASSWORD -R szijpeter/garnish
```

## 3. GPG signing key (if you don't have one yet)

Create key:

```bash
gpg --full-generate-key
```

Export ASCII-armored private key for `SIGNING_KEY`:

```bash
gpg --armor --export-secret-keys <KEY_ID>
```

Find key id:

```bash
gpg --list-secret-keys --keyid-format LONG
```

## 4. Pre-release checks

Run locally:

```bash
./gradlew check --no-daemon
./gradlew apiCheck --no-daemon
```

Update version:

- Set `VERSION_NAME` in `gradle.properties` (or pass `-PVERSION_NAME=...` in workflow input).
- Use non-`SNAPSHOT` for stable releases.

## 5. Publish from GitHub Actions

Use workflow: `.github/workflows/publish.yml`

Inputs:

- `release_mode=publish-only`
  - uploads to Central and waits for manual release from portal.
- `release_mode=publish-and-release`
  - uploads and requests automatic release.
- `version_name` (optional)
  - temporary override for `VERSION_NAME`.

## 6. Local commands (maintainers)

```bash
# Publish (manual release flow)
./gradlew publishToMavenCentral --no-daemon

# Publish + auto-release
./gradlew publishAndReleaseToMavenCentral --no-daemon

# Local preflight only
./gradlew publishToMavenLocal --no-daemon
```

## 7. Troubleshooting

- Namespace rejected: `GROUP` does not match approved namespace in Central Portal.
- 401/403: invalid token secrets.
- Signing failures: key format or passphrase mismatch.
- Missing docs/sources jar: verify module applies `garnish.publishing`.
