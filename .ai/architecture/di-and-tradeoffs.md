# DI And Tradeoffs

## Koin

Koin is used instead of Hilt because it keeps the graph accessible and low-boilerplate:

- modules are plain Kotlin,
- no annotation processing for DI,
- feature bindings are easy to inspect,
- implementation swapping in tests is straightforward.

Runtime graph resolution is an acceptable cost for this project. In a larger app, load critical startup modules first and lazy-load feature modules as needed.

## Room

Room is used instead of raw SQLite because it provides:

- Flow support,
- query validation,
- schema management,
- less boilerplate while still using SQLite underneath.

## Networking

There is no real API contract in the assessment, so OkHttp/networking was removed. If a real API exists later, add a remote data source behind `RestaurantRepository` and define sync/error/cache behavior explicitly.
