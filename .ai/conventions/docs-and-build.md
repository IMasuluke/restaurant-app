# Docs And Build Conventions

## Build

Primary verification:

```sh
./gradlew assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

If Gradle cannot find a JDK, configure `JAVA_HOME` locally. Do not hard-code a machine-specific `JAVA_HOME` into project docs or build files.

## Docs

- Update `README.md` when run/install/user-facing behavior changes.
- Update `SOLUTION.md` when architecture, tradeoffs, data flow, persistence, DI, deep links, issue simulation, or future-improvement rationale changes.
- Keep `AGENTS.md` as the source of truth for context-loading instructions.
- Keep `CLAUDE.md` as a symlink to `AGENTS.md`.
- Keep reusable agent rules in `.ai`.

## Files To Avoid Shipping

- `local.properties`
- `.gradle/`
- build outputs
- IDE workspace state
