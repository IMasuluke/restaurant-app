# Restaurant App

The app implements a restaurant browsing take-home flow using Jetpack Compose, Koin, and MVI.

## Features

- Browse 220 generated nearby restaurants.
- Switch between all restaurants and saved restaurants.
- Search by restaurant name or cuisine.
- Filter to restaurants that are open now.
- Open restaurant details with core metadata and menu sections.
- Persist favorite restaurants across app restarts.
- Handle loading, empty, and error states.
- Simulate API-style issue states from the browse screen and trigger them with pull-to-refresh.
- Support restaurant deeplinks in the form `mrd://restaurants/r-0001`.

## Architecture

- `:app`: thin Android shell. Owns the manifest, application class, Koin graph assembly, and activity entry point.
- `:core`: shared data/domain foundation. Owns immutable models, repository contracts and implementation, JSON seed ingestion, Room persistence, and shared formatting.
- `:feature:restaurants`: vertical restaurant feature. Owns list/detail Compose screens, MVI contracts, ViewModels, and feature DI.

The feature follows MVI: UI emits intents, ViewModels reduce them into `StateFlow` UI state, and composables render state only.

## Running From Android Studio

1. Open this folder in Android Studio.
2. Let Gradle sync complete.
3. Select the `app` run configuration.
4. Run on an emulator or physical device.

Android Studio should use its bundled JDK by default. If Gradle asks for a JDK, choose the Android Studio bundled JDK or any JDK version supported by the Android Gradle Plugin.

## Building From Terminal

From the project root, run:

```sh
./gradlew assembleDebug
```

If your shell does not already have a compatible JDK configured, set `JAVA_HOME` to your local JDK installation first. For example:

```sh
export JAVA_HOME=/path/to/your/jdk
./gradlew assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Install And Deep Link Test

Install the debug APK with:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Test a restaurant deep link with:

```sh
adb shell am start -a android.intent.action.VIEW -d "mrd://restaurants/r-0001"
```

## Issue Simulation

Tap the `!` icon on the restaurants screen to open the issue simulator. Choose an API issue, tap `Arm`, go back to the restaurants list, then pull to refresh. The next repository refresh consumes the armed issue and shows the real browse error state once.

## Verification

The main verification command is:

```sh
./gradlew assembleDebug
```

This compiles all modules and produces a debug APK.
