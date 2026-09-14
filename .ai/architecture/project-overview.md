# Project Overview

This is an Android take-home assessment for a senior Android developer. The app implements an MrD-style restaurant browsing and detail flow using Jetpack Compose, Koin, Room, Kotlin coroutines/Flow, and an MVI-inspired presentation layer.

## Current Scope

- Browse 220 seeded restaurants.
- Search by restaurant name or cuisine.
- Filter to restaurants that are open now.
- Switch between all restaurants and saved restaurants.
- View restaurant details and menu sections.
- Save/unsave restaurants, persisted with Room.
- Trigger API-style issue simulation from the browse screen, then pull to refresh to exercise the real error path.
- Open detail via custom scheme deep link: `mrd://restaurants/r-0001`.

## Modules

- `:app`: thin Android shell. Contains `MrDApplication`, `MainActivity`, manifest, app resources, and Koin graph startup.
- `:core`: shared domain/data foundation. Contains domain models, repository contract, Room database/DAOs/entities, JSON seed ingestion, persistence abstractions, formatting, and the one-shot API issue simulation controller.
- `:feature:restaurants`: restaurant feature. Contains browse/detail/simulation Compose UI, MVI intents/states, ViewModels, feature DI, theme, and shared UI components.

Keep this module split. It is intentionally small: enough to demonstrate modularisation without over-engineering the timebox.
