import kotlinx.coroutines.delay
import kotlin.random.Random

class MovieCatalogue {
    private val items = mutableListOf<MediaItem>()

    fun add(item: MediaItem) {
        items.add(item)
    }

    fun all(): List<MediaItem> = items

    fun availableGenres(): Set<Genre> = items.map { it.genre }.toSet()

    fun groupedByGenre(): Map<Genre, List<MediaItem>> = items.groupBy { it.genre }

    fun filterMovies(predicate: (MediaItem) -> Boolean): List<MediaItem> = items.filter(predicate)

    fun forEachItem(action: (MediaItem) -> Unit) {
        for (item in items) {
            action(item)
        }
    }

    fun titles(): List<String> = items.map { it.title }

    fun averageRating(): Double {
        if (items.isEmpty()) return 0.0
        val total = items.map { it.rating }
            .reduce { sum, element -> sum + element }
        return total / items.size
    }

    fun watchedTitles() : List<String> =
        items.filter { it.isWatched() }.map { it.title.uppercase() }

    suspend fun searchByTitle(query: String): SearchResult {
        delay(Random.nextLong(200, 600))

        if (query.isBlank()) {
            return SearchResult.Error("Search query must not be empty.")
        }
        val matches = items.filter { it.title.contains(query, ignoreCase = true) }

        return when {
            matches.isEmpty() -> SearchResult.NotFound
            matches.size == 1 -> SearchResult.Found(matches.first())
            else -> SearchResult.MultipleFound(matches)
        }
    }
}


fun MediaItem.isWatched(): Boolean = when (this) {
    is Movie -> this.watched
    is Series -> this.watched
    else -> false
}