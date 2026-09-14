# MVI Flow

The restaurant feature uses an MVI-inspired approach:

- UI emits typed intents such as `BrowseIntent` and `DetailIntent`.
- ViewModels process intents.
- ViewModels expose immutable `StateFlow` UI state.
- Composables render state and do not perform data work directly.

## Browse State

`BrowseViewModel` combines:

- restaurants from `RestaurantRepository.observeRestaurants()`,
- favorite ids,
- search text,
- open-only filter,
- selected page,
- loading/error state.

This keeps the screen reactive. Search, favorites, filters, and seeded Room data can change independently while producing one UI state stream.

## Rules

- Keep user events as typed intent sealed interfaces.
- Keep UI state as immutable data classes.
- Avoid leaking repository/data details into composables.
- Avoid adding use cases that only forward one repository call.
