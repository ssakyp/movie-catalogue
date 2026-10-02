# Movie Catalogue (Kotlin Console App)

A small console-based movie/series catalogue written in Kotlin. It is a plain
Kotlin/JVM program — **no Android UI, Activities, Fragments, or Jetpack
Compose are used.**

The app stores a catalogue of `Movie`s and `Series`, lets you browse it by
genre, filter and score items, and "search" the catalogue asynchronously
using Kotlin coroutines.

## Project structure

```
movie-catalogue/
├── build.gradle.kts
├── settings.gradle.kts
└── src/main/kotlin/
    ├── Models.kt          // enum, interfaces, abstract class, data class, sealed class
    ├── MovieCatalogue.kt  // collections + higher-order functions + suspend function
    └── Main.kt            // entry point (main()), loops, conditions, coroutine usage
```

## How to run

### Option A — IntelliJ IDEA (recommended)
1. Open IntelliJ IDEA → `File > Open` → select the `movie-catalogue` folder.
2. Let Gradle sync (it will download the Kotlin plugin and the
   `kotlinx-coroutines-core` dependency automatically).
3. Open `src/main/kotlin/Main.kt` and click the green ▶ run icon next to
   `fun main()`.

### Option B — Command line (Gradle installed locally)
```bash
cd movie-catalogue
gradle run
```
If you'd rather use the Gradle Wrapper, generate it once (requires Gradle
installed) and then use `./gradlew run` from then on:
```bash
gradle wrapper --gradle-version 8.6
./gradlew run
```

### Option C — Android Studio (no UI)
Open the folder the same way as in IntelliJ (Android Studio is built on
IntelliJ and can open plain Kotlin/JVM Gradle projects) and run `Main.kt`.

## Where each requirement is demonstrated

| Requirement | Where |
|---|---|
| Variables, data types, conditions, loops | `Main.kt` — `val`/`var` declarations, `if`, `when`, `for` loops over the catalogue |
| List, Set, Map | `MovieCatalogue.kt` — `items: MutableList<MediaItem>`, `availableGenres(): Set<Genre>`, `groupedByGenre(): Map<Genre, List<MediaItem>>` |
| `map`, `filter`, `reduce` | `MovieCatalogue.kt` — `titles()` (map), `watchedTitles()` (filter+map), `averageRating()` (reduce) |
| Functions, higher-order functions, lambdas | `MovieCatalogue.kt` — `filterMovies(predicate: (MediaItem) -> Boolean)`, `forEachItem(action: (MediaItem) -> Unit)`; called with lambdas from `Main.kt` |
| Classes and objects | `Models.kt` — `MediaItem`, `Movie`, `Series`, `MovieCatalogue` class |
| Inheritance | `Models.kt` — `Movie` and `Series` both extend abstract class `MediaItem` |
| Interfaces and polymorphism | `Models.kt` — `Describable` and `Rateable` interfaces; `describe()` behaves differently for `Movie` vs `Series` when called through the common `MediaItem` type (see the loop in `Main.kt`) |
| Data class | `Models.kt` — `data class Movie(...)` |
| Sealed class | `Models.kt` — `sealed class SearchResult` with `Found`, `MultipleFound`, `NotFound`, `Error`, consumed with an exhaustive `when` in `Main.kt` |
| Suspend function + coroutine | `MovieCatalogue.kt` — `suspend fun searchByTitle(...)`; driven from `Main.kt` inside `runBlocking { ... }` using `async`/`awaitAll` to run several searches concurrently |

## Sample output (truncated for brevity)

```
=== CampusFlix - Full Catalogue ===
Movie   : #1 "The Algorithm" (2021) [SCI_FI] - 118min - rating 8.4 - ****-
Series  : #4 "Backend Chronicles" (2020) [DRAMA] - 3 season(s) - rating 9.1 - ****-
...
=== Simulated async search (coroutines) ===
Search "chronicles" -> found 1 match: Backend Chronicles
Search "kotlin" -> found 1 match: Kotlin Nights
Search "unknown title" -> no matches.
Search "" -> error - Search query must not be empty.

Done. Thanks for browsing CampusFlix!
```