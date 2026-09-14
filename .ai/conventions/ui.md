# UI Conventions

## Browse

Browse supports:

- All/Saved pager tabs.
- Search.
- Open-now switch.
- Pull-to-refresh.
- Issue simulator entry via `!`.

Pull-to-refresh uses the main loading state. The `PullToRefreshState` is explicitly hidden when loading becomes false.

## Detail

Detail supports:

- Back navigation.
- Favorite toggle.
- Error/not-found state for invalid restaurant ids.

## Interaction Rules

- Composables should render state and emit callbacks.
- Keep data work in ViewModels/repositories.
- Keep error, empty, loading, and success states distinct.
