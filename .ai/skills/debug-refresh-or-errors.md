# Skill: Debug Refresh Or Error Handling

Use this for pull-to-refresh, loading, retry, issue simulation, or error-state bugs.

1. Read `.ai/architecture/issue-simulation.md`.
2. Read `.ai/architecture/mvi-flow.md`.
3. Check `BrowseViewModel` loading/error handling.
4. Check `BrowseRoute` pull-to-refresh and error rendering.
5. Check `IssueSimulationController` only if simulated API failures are involved.
6. Run `./gradlew assembleDebug`.

Current expectation:

- Pull-to-refresh triggers `BrowseIntent.RefreshRequested`.
- Refresh uses the same loading state as normal load/retry.
- The Material pull-to-refresh state is explicitly hidden when loading becomes false.
- Armed issue scenarios are consumed once by repository refresh.
