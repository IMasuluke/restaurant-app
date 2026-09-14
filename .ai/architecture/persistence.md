# Persistence And Data

Restaurant seed data lives at:

```text
core/src/main/assets/restaurants.json
```

On first refresh, JSON is parsed and written to Room. From then on, the app reads restaurants from Room.

## Flow Usage

Room exposes restaurant and favorite data as Flow where ongoing updates matter:

- list data should update automatically,
- favorite toggles should update browse/detail UI without manual refresh callbacks,
- future remote sync could update Room and the UI would react to local DB emissions.

Use suspend functions for one-shot operations such as detail lookup by id.

## Boundaries

- Feature ViewModels depend on `RestaurantRepository`.
- `:feature:restaurants` must not depend on Room DAOs/entities or JSON parsing.
- Repository implementation and data source abstractions live in `:core`.
