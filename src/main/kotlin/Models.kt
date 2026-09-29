enum class Genre {
    ACTION,
    ADVENTURE,
    COMEDY,
    DRAMA,
    FANTASY,
    HORROR,
    MYSTERY,
    ROMANCE,
    SCI_FI,
    THRILLER,
    DOCUMENTARY
}

interface Describable {
    fun describe(): String
}

interface Ratable {
    val rating: Double
    fun ratingStars(): String {
        val stars = (rating / 2 ).toInt().coerceIn(0, 5)
        return "*".repeat(stars) + "-".repeat(5 - stars)
    }
}

abstract class MediaItem(
    val id: Int,
    val title: String,
    val year: Int
) : Describable, Ratable {
    abstract val genre: Genre

    fun basicInfo(): String = "#$id \"title\" ($year) [$genre]"
}

data class Movie(
    val movieId: Int,
    val movieTitle: String,
    val movieYear: Int,
    override val genre: Genre,
    override val rating: Double,
    val durationMinutes: Int,
    var watched: Boolean = false

) : MediaItem(movieId, movieTitle, movieYear) {
    override fun describe(): String = "Movie : ${basicInfo()}, Duration: $durationMinutes minutes, Rating: ${ratingStars()}"
}

class Series(
    id: Int,
    title: String,
    year: Int,
    override val genre: Genre,
    override val rating: Double,
    val seasons: Int,
    var watched: Boolean = false
) : MediaItem(id, title, year) {
    override fun describe(): String = "Series : ${basicInfo()}, Seasons: $seasons, Rating: ${ratingStars()}"
}

sealed class SearchResult {
    data class Found(val item: MediaItem) : SearchResult()
    data class MultipleFound(val items: List<MediaItem>) : SearchResult()
    object NotFound: SearchResult()
    data class Error(val message: String) : SearchResult()
}
