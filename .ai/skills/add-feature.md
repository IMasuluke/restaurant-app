# Skill: Add Or Change A Feature

Use this when adding user-facing behavior.

1. Read `.ai/architecture/project-overview.md`.
2. Read `.ai/conventions/android.md`.
3. Read `.ai/conventions/ui.md` for Compose behavior.
4. Inspect the existing feature package before editing.
5. Prefer existing MVI patterns: intent, ViewModel state update, composable render.
6. Update docs if behavior changes.
7. Run `./gradlew assembleDebug`.

Guardrails:

- Keep `:app` thin.
- Do not make feature UI depend on Room/JSON concrete types.
- Do not add broad abstractions for one call site.
