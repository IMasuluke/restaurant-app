# Issue Simulation

The issue simulator arms one-shot API-style failures that are consumed by the real refresh path.

## Flow

1. User taps `!` on the browse screen.
2. User selects an API issue.
3. User taps `Arm`.
4. User goes back to browse.
5. User pulls to refresh.
6. `LocalRestaurantRepository.refresh()` consumes the armed issue, throws a simulated exception once, clears it, and `BrowseViewModel` renders the normal error state.

## Armable Scenarios

- API timeout.
- HTTP 500.
- Network unavailable.
- Malformed response.

## Relevant Files

- `core/src/main/java/com/example/mrd/debug/IssueSimulationController.kt`
- `core/src/main/java/com/example/mrd/debug/ApiIssueScenario.kt`
- `core/src/main/java/com/example/mrd/debug/SimulatedApiException.kt`
- `feature/restaurants/src/main/java/com/example/mrd/ui/simulation/SimulationRoute.kt`
- `feature/restaurants/src/main/java/com/example/mrd/ui/simulation/SimulationViewModel.kt`

Do not reintroduce static previews for non-armable states on the simulator screen.
