# Solution

## Overview

This project implements the restaurant browsing and detail flow for the MrD senior Android take-home assessment.

The app is intentionally small, but structured to show production-oriented Android decisions: a thin app module, shared core data/domain boundaries, a focused restaurant feature module, Compose UI, Koin dependency injection, MVI-style state handling, Room persistence, and JSON seed data.

## What Is Included

- Browse a list of 220 restaurants.
- Switch between all restaurants and saved restaurants.
- Search by restaurant name or cuisine.
- Filter restaurants to "open now".
- Open a restaurant detail screen with metadata and menu sections.
- Persist favorites across app restarts.
- Support loading, empty, and error states.
- Simulate API-style issue states from the browse screen and trigger them with pull-to-refresh.
- Support custom scheme deep links such as `mrd://restaurants/r-0001`.
- Seed restaurant/menu data from `core/src/main/assets/restaurants.json`.

## Module Structure

### `:app`

The app module is deliberately thin. It owns:

- `MrDApplication`, where the Koin graph is assembled.
- `MainActivity`, the single Activity entry point.
- The Android manifest and deep link declaration.

Navigation is Compose-driven inside the single Activity. There are no fragments.

### `:core`

The core module owns shared foundations:

- Domain models such as `Restaurant`, `MenuCategory`, `MenuItem`, and `MenuOption`.
- The `RestaurantRepository` interface.
- The local repository implementation.
- Room database, DAOs, entities, and type converters.
- Local data source interfaces and Room-backed implementations.
- JSON asset ingestion for seed data.
- Shared formatting helpers.

The repository depends on interfaces for local restaurant storage, favorite storage, and seed data. This keeps the persistence choices replaceable without forcing the feature layer to know about Room or JSON parsing.

### `:feature:restaurants`

The restaurant feature owns the user-facing flow:

- Browse screen UI, state, intents, and ViewModel.
- Detail screen UI, state, intents, and ViewModel.
- Feature-level Koin bindings.
- Shared restaurant UI components and theme.

The feature ViewModels depend on the `RestaurantRepository` interface from `:core`, not on Room, DAOs, JSON, or concrete data sources.

## Architecture

The feature uses an MVI-inspired approach:

- UI emits `BrowseIntent` and `DetailIntent`.
- ViewModels process those intents.
- ViewModels expose immutable `StateFlow` UI state.
- Composables render state and do not perform data work directly.

For the browse screen, `Flow.combine` is used to derive the final list state from:

- restaurants from the repository,
- favorite ids,
- search text,
- the open-only filter,
- loading state,
- error state.

This keeps the screen state reactive. If the user changes the search text, toggles a favorite, or Room emits new restaurant data, the UI state is recalculated from the latest values rather than manually patched in multiple places.

## Why MVI Instead Of Plain MVVM

MVVM would be a reasonable choice for an app of this size, especially with Compose and `StateFlow`. I chose an MVI-inspired structure because the assignment asks for distinct screen states and user interactions, and MVI makes those flows explicit:

- screen state is represented by a single immutable UI state object,
- user actions are represented as typed intents,
- the ViewModel is the only place that translates intents into state changes,
- loading, empty, success, and error states are easier to reason about because the UI renders one state stream.

This is useful for the browse screen in particular, where restaurant data, favorites, search text, and the open-only filter all change independently. MVI keeps that complexity contained in the ViewModel and avoids spreading conditional state handling through the composables.

## Persistence And Data

Restaurant data is seeded from `restaurants.json` in assets. On first refresh, the repository loads the JSON seed data and writes it into Room. From then on, the app reads restaurants from Room.

Favorites are persisted in Room as well, so they survive app restarts.

I chose JSON seed data instead of Kotlin mocks because the assignment allows generated/mock data, and an asset file is closer to a real ingestion boundary. It also makes the data easy to inspect and replace.

## Flow In Persistence

Room exposes restaurant and favorite data as `Flow`, which fits the UI model well. The list screen should not have to manually reload whenever local data changes. Instead, the repository exposes observable data, and the ViewModel derives screen state from it.

This is especially useful for favorites. When a restaurant is saved or unsaved, Room updates the favorites table, the favorites `Flow` emits a new set of ids, and the browse/detail UI updates automatically through the existing combined state flow. There is no separate callback path or manual "refresh favorite button" logic.

Using `Flow` also keeps the repository API future-friendly. Today the data comes from JSON and Room, but the same observable contract would still work if a remote sync process updated the local database in the background. Room would emit the latest local state and the UI would react without needing to know whether the change came from seeding, user interaction, or network sync.

For one-off operations, such as loading a specific restaurant by id for the detail screen, the repository still uses a suspend function. That keeps the API clear: streams are used where the UI should continue observing changes, and suspend calls are used where a single result is enough.

## Issue Simulation

The browse screen includes a small issue simulator entry point. This is intentionally implemented as a one-shot failure injection rather than a static preview only.

The simulator can arm API-style failures such as timeout, server error, offline, or malformed response. When the user returns to the browse screen and pulls to refresh, the repository consumes the armed issue, throws a simulated API exception, clears the issue, and lets the existing ViewModel error handling render the real error state.

That means the simulation exercises the same path a real API refresh failure would use: UI intent, ViewModel refresh, repository failure, and MVI error state.

## Networking

The current implementation does not include networking.

I intentionally removed the network/OkHttp wrapper because the app is using local JSON seed data. Keeping an unused network client would add noise and make the solution look less deliberate. If a real API were introduced later, the repository boundary and seed/local data source interfaces provide a natural place to add a remote data source and sync policy.

## Deep Links

The app supports custom scheme links:

```text
mrd://restaurants/r-0001
```

This opens the app directly to the detail screen for the matching restaurant id. If the id does not exist, the detail screen displays the existing not-found error state.

Universal links would require a real owned domain plus `assetlinks.json`, so this implementation uses a custom scheme for the take-home scope.

In a production app, I would prefer Android App Links/universal links over a custom scheme. The owned domain would publish Digital Asset Links metadata, tied to the app signing certificate, so Android can verify that only the intended app handles those links. If link parameters carried sensitive or trusted data, I would also sign the payload, for example with an ECDSA-backed signature/JWT, so the app can detect tampering before acting on the deep link. For this assessment, the deep link only carries a restaurant id and the app validates it against local data.

## Tradeoffs

### Module Boundaries

I chose a small modular structure instead of a monolithic app module because the assignment is partly about demonstrating how the code would scale beyond two screens. A monolith would be faster to set up, but it would blur ownership between Android entry points, shared data/persistence, and restaurant-specific UI.

The current split keeps those responsibilities clear:

- `:app` is only the Android shell and graph assembly,
- `:core` owns shared models, repository contracts, persistence, and data ingestion,
- `:feature:restaurants` owns the restaurant user experience.

This gives clear dependency direction and makes it harder for feature UI to accidentally depend on Room, JSON parsing, or Android entry-point code.

I kept `:core` as one module instead of splitting separate `domain`, `data`, `database`, and `network` modules. In a production codebase, those splits can be useful when teams own different areas or when compile-time boundaries need to be stricter. For this timebox, `:app`, `:core`, and `:feature:restaurants` show modularisation without making the reviewer jump through too many files.

The important boundary is still present: the feature depends on `RestaurantRepository`, not Room, DAOs, JSON parsing, or concrete data sources. That keeps the feature testable and means the persistence implementation can change later without rewriting the UI.

### Koin Instead Of Hilt

I chose Koin because I prefer the lower boilerplate and accessibility it gives the team:

- modules are plain Kotlin and easy to read in one place,
- no annotation processing is needed for DI,
- feature modules can declare bindings without Hilt component ceremony,
- swapping implementations in tests is straightforward.

Hilt has strong advantages, especially compile-time graph validation and standard Android lifecycle integration. The tradeoff is more annotations, generated code, and component/module structure. For this project, I think Koin makes the dependency graph easier to inspect and reason about.

The main cost is that Koin resolves the graph at runtime. I consider that acceptable here: the startup overhead is usually only a small number of milliseconds for a graph of this size, and in a larger app I would load only the critical startup modules initially, then lazy-load feature modules as they are needed. That keeps the app startup path lean while preserving Koin's simpler developer experience.

### Persistence

Room is used instead of raw SQLite because it gives compile-time query validation, Flow support, schema management, and less boilerplate while still using SQLite underneath. Raw SQLite would offer maximum control, but it would make the solution more verbose without adding much value for this problem.

### Data Source

The app uses a JSON asset as seed input and then persists that data into Room. This is a deliberate middle ground: the dataset is easy to inspect and replace, but the app still exercises a realistic local storage path instead of rendering hard-coded Kotlin mocks.

I removed networking because there is no real API contract in the assessment. A dormant network client would make the architecture look less intentional. If a real endpoint existed, I would add a remote data source behind the repository and define sync/error/cache behavior explicitly.

### Images

Images are represented with deterministic generated visuals rather than loading remote image URLs, because the current data source is local and offline-friendly. In production, I would use a proper image loader such as Coil, with placeholders, caching, and error handling.

## Future Improvements

Given more time, I would add:

- focused unit tests for filtering and state mapping,
- repository tests for JSON seeding and favorite persistence,
- microbenchmark and macrobenchmark modules to track hot code paths, startup, scrolling, and screen transitions,
- CI/CD checks to run tests, static analysis, build verification, and benchmark/regression monitoring,
- observability with crash reporting, structured logging, performance metrics, and tracing around startup, data loading, and key user flows,
- a `build-logic` module with convention plugins once more feature/library modules exist and Gradle configuration starts repeating,
- Navigation Compose for a larger flow,
- a real remote data source and sync strategy if an API were available,
- proper image loading if remote images became part of the data contract.
