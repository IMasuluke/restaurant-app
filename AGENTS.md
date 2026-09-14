# Agent Context

This file is the source of truth for coding-agent context in this repository.

`CLAUDE.md` is a symlink to this file. If a tool opens `CLAUDE.md`, treat it as this same context and follow the loading rules below.

## Start Here

This repository uses modular AI context under `.ai/`. Do not load everything by default. Start with this file, then load the smallest set of context files for the task.

Always load first:

- `.ai/architecture/project-overview.md`
- `.ai/conventions/docs-and-build.md`

These provide the project shape, verification command, and documentation rules.

## Load By Task

### Feature Or UI Changes

Load:

- `.ai/skills/add-feature.md`
- `.ai/architecture/mvi-flow.md`
- `.ai/conventions/android.md`
- `.ai/conventions/ui.md`

Use for browse/detail UI, MVI state, tabs, favorites, search, filters, and Compose work.

### Pull-To-Refresh, Loading, Retry, Or Error Bugs

Load:

- `.ai/skills/debug-refresh-or-errors.md`
- `.ai/architecture/issue-simulation.md`
- `.ai/architecture/mvi-flow.md`
- `.ai/conventions/ui.md`

Use for loading indicators, error states, retry, simulator behavior, and refresh issues.

### Persistence, Room, JSON, Repository Work

Load:

- `.ai/architecture/persistence.md`
- `.ai/architecture/di-and-tradeoffs.md`
- `.ai/conventions/android.md`

Use for Room, JSON seed data, repository contracts, favorites persistence, and data source boundaries.

### DI, Module Boundaries, Or Architecture Rationale

Load:

- `.ai/architecture/project-overview.md`
- `.ai/architecture/di-and-tradeoffs.md`
- `.ai/conventions/android.md`

Use for Koin/Hilt discussion, module structure, and dependency direction.

### Build, Verification, APK Upload, Or Submission Prep

Load:

- `.ai/conventions/docs-and-build.md`

Use before building, packaging, uploading, or updating reviewer-facing docs.

## Current Project In One Paragraph

Android take-home assessment for a senior Android developer. It is a single-activity Compose app with `:app`, `:core`, and `:feature:restaurants`; Koin DI; Room persistence; JSON seed data; MVI-style ViewModels; all/saved restaurant tabs; favorites; custom scheme deep links; and one-shot API issue simulation triggered by pull-to-refresh.

## Golden Rules

- Keep `:app` thin.
- Keep Room and JSON parsing out of `:feature:restaurants`.
- Preserve the MVI intent/state/ViewModel pattern.
- Do not add one-line pass-through use cases.
- Keep docs updated when behavior or architecture changes.
- Run `./gradlew assembleDebug` before considering code changes done.
- Do not hard-code local machine paths in docs or build files.
- Do not include `local.properties`, `.gradle`, module `build/` outputs, or IDE workspace state in archives/uploads.
