import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/**
 * Main.kt - program entry point.
 *
 * This single, coherent "Movie Catalogue" program exercises every requested
 * concept together instead of as isolated snippets:
 *  - variables / data types / conditions / loops
 *  - List / Set / Map
 *  - map / filter / reduce
 *  - functions, higher-order functions, lambdas
 *  - classes, inheritance, interfaces, polymorphism
 *  - a data class and a sealed class
 *  - a suspend function driven by real coroutines
 */
fun main() {
    // ----- variables & data types -----
    val catalogueName: String = "KinoFlix"
    val minimumGoodRating: Double = 7.5
    var totalWatched: Int = 0

    val catalogue = MovieCatalogue()

    catalogue.add(Movie(1, "The Algorithm", 2021, Genre.SCI_FI, 8.4, 118, watched = true))
    catalogue.add(Movie(2, "Kotlin Nights", 2019, Genre.COMEDY, 6.9, 97, watched = false))
    catalogue.add(Movie(3, "Silent Compiler", 2023, Genre.HORROR, 7.8, 104, watched = true))
    catalogue.add(Series(4, "Backend Chronicles", 2020, Genre.DRAMA, 9.1, seasons = 3, watched = true))
    catalogue.add(Series(5, "Null Pointer", 2022, Genre.ACTION, 6.2, seasons = 1, watched = false))
    catalogue.add(Movie(6, "The Great Refactor", 2024, Genre.DOCUMENTARY, 8.9, 132, watched = false))

    println("=== $catalogueName - Full Catalogue ===")

    // ----- loop + polymorphism -----
    // describe() is defined differently in Movie and in Series (polymorphism),
    // yet we can loop over the common MediaItem type uniformly.
    for (item in catalogue.all()) {
        println(item.describe())
        // ----- condition -----
        if (item.isWatched()) {
            totalWatched++
        }
    }
    println("Watched so far: $totalWatched / ${catalogue.all().size}")

    // ----- Set -----
    println("\nGenres represented in the catalogue (Set, no duplicates):")
    println(catalogue.availableGenres().joinToString(", "))

    // ----- Map -----
    println("\nCatalogue grouped by genre (Map<Genre, List<MediaItem>>):")
    for ((genre, itemsInGenre) in catalogue.groupedByGenre()) {
        val titleList = itemsInGenre.joinToString(", ") { it.title }
        println("  $genre -> $titleList")
    }

    // ----- higher-order function + lambda -----
    println("\nHighly rated items (rating >= $minimumGoodRating), via higher-order filterMovies():")
    val topRated = catalogue.filterMovies { it.rating >= minimumGoodRating }
    topRated.forEach { println("  - ${it.title} (${it.rating})") }

    // higher-order function taking an action lambda
    println("\nApplying a lambda action to every item (mark long titles):")
    catalogue.forEachItem { item ->
        val tag = if (item.title.length > 12) "[long title]" else "[short title]"
        println("  $tag ${item.title}")
    }

    // ----- map / filter / reduce -----
    println("\nAll titles (map): ${catalogue.titles()}")
    println("Watched titles, uppercase (filter + map): ${catalogue.watchedTitles()}")
    println("Average rating across catalogue (reduce): %.2f".format(catalogue.averageRating()))

    // ----- when expression over the sealed class -----
    // A small local helper function using a lambda for scoring, showing
    // functions-as-values again in a different context.
    val scoreLabel: (Double) -> String = { r ->
        when {
            r >= 9.0 -> "Masterpiece"
            r >= 7.5 -> "Great"
            r >= 5.0 -> "Average"
            else -> "Skip it"
        }
    }
    println("\nQuick verdicts:")
    catalogue.all().forEach { println("  ${it.title}: ${scoreLabel(it.rating)}") }

    // ----- suspend function + coroutines -----
    println("\n=== Simulated async search (coroutines) ===")
    runBlocking {
        val queries = listOf("chronicles", "kotlin", "unknown title", "")
        // Launch every search concurrently using async, then wait for all results.
        val deferredResults = queries.map { query ->
            async { query to catalogue.searchByTitle(query) }
        }
        val results = deferredResults.awaitAll()

        for ((query, result) in results) {
            print("Search \"$query\" -> ")
            // Exhaustive `when` over the sealed class - the compiler enforces
            // that every subtype is handled, no `else` branch needed.
            when (result) {
                is SearchResult.Found ->
                    println("found 1 match: ${result.item.title}")
                is SearchResult.MultipleFound ->
                    println("found ${result.items.size} matches: ${result.items.joinToString { it.title }}")
                is SearchResult.NotFound ->
                    println("no matches.")
                is SearchResult.Error ->
                    println("error - ${result.message}")
            }
        }
    }

    println("\nDone. Thanks for browsing $catalogueName!")
}
