package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

open class Movie : RealmObject() {
    @PrimaryKey
    var imdbID: String = ""
    var Title: String = ""
    var Year: String = ""
    var Rated: String = ""
    var Released: String = ""
    var Runtime: String = ""
    var Genre: String = ""
    var Director: String = ""
    var Writer: String = ""
    var Actors: String = ""
    var Plot: String = ""
    var Language: String = ""
    var Country: String = ""
    var Awards: String = ""
    var Poster: String = ""
    var imdbRating: String = ""
    var imdbVotes: String = ""
    var Type: String = ""
    var Production: String = ""
    var posterBitmap: ByteArray? = null

    fun getPosterBipMap(): Bitmap? {
        posterBitmap ?: return null
        val stream = ByteArrayInputStream(posterBitmap)
        return BitmapFactory.decodeStream(stream)
    }

    fun loadPosterBipMapFromUrl(): Bitmap? {
        if (Poster == "N/A") return null
        val url = URL(Poster)
        try {
            val inputStream = url.openStream()
            return BitmapFactory.decodeStream(inputStream)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    fun setPosterBipMap(bmp: Bitmap) {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        posterBitmap = stream.toByteArray()
    }

    fun copy(): Movie {
        val m = Movie()
        m.imdbID = imdbID
        m.Title = Title
        m.Year = Year
        m.Rated = Rated
        m.Released = Released
        m.Runtime = Runtime
        m.Genre = Genre
        m.Director = Director
        m.Writer = Writer
        m.Actors = Actors
        m.Plot = Plot
        m.Language = Language
        m.Country = Country
        m.Awards = Awards
        m.Poster = Poster
        m.imdbRating = imdbRating
        m.imdbVotes = imdbVotes
        m.Type = Type
        m.Production = Production
        m.posterBitmap = posterBitmap
        return m
    }
}

// for each request store IDs of movies it returns
open class MovieRequest(@PrimaryKey var request: String, var movies: RealmList<String>) : RealmObject() {
    constructor() : this("", RealmList())
}

class MovieSearchResult {
    var Search: List<Movie> = listOf()
}


