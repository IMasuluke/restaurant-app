# Android Conventions

- Compose only. No fragments.
- Single-activity app.
- Keep `:app` thin.
- Keep Room/JSON implementation details in `:core`.
- Keep restaurant UI, ViewModels, intents, and UI states in `:feature:restaurants`.
- Use Koin modules for DI.
- Prefer Flow for observable data and suspend functions for one-shot operations.
- Keep each data class in its own file where practical.
- Do not add pass-through use cases unless they contain real policy or orchestration.
