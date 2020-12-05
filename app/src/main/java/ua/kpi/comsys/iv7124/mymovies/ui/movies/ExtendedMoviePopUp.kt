package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.extended_movie.*
import ua.kpi.comsys.iv7124.mymovies.R
import ua.kpi.comsys.iv7124.mymovies.allMovies
import ua.kpi.comsys.iv7124.mymovies.importFromJSON

class ExtendedMoviePopUp  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extended_movie)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width: Int = (dm.widthPixels)
        val height: Int = (dm.heightPixels)

        window.setLayout(width, height)

        val selectedMovieId = intent.getStringExtra(selectedMovieId) ?: ""

        val movieFileId = getResId(selectedMovieId, R.raw::class.java)

        var movie: Movie

        movie = if (movieFileId == -1) {
            allMovies.firstOrNull { it.imdbID == selectedMovieId } ?: Movie()
        } else {
            importFromJSON(application, Movie::class.java, movieFileId) ?: Movie()
        }

        val posterId = getResId(movie.Poster, R.drawable::class.java)
        extended_movie_poster?.setImageResource(if (posterId == -1) R.drawable.ic_movies_black_24dp else posterId)

        extended_movie_title.text = getString(R.string.Title, movie.Title)
        extended_movie_year.text = getString(R.string.Year, movie.Year)
        extended_movie_genre.text = getString(R.string.Genre, movie.Genre)
        extended_movie_director.text = getString(R.string.Director, movie.Director)
        extended_movie_actors.text = getString(R.string.Actors, movie.Actors)
        extended_movie_country.text = getString(R.string.Country, movie.Country)
        extended_movie_language.text = getString(R.string.Language, movie.Language)
        extended_movie_production.text = getString(R.string.Production, movie.Production)
        extended_movie_released.text = getString(R.string.Released, movie.Released)
        extended_movie_runtime.text = getString(R.string.Runtime, movie.Runtime)
        extended_movie_awards.text = getString(R.string.Awards, movie.Awards)
        extended_movie_rating.text = getString(R.string.Rating, movie.imdbRating)
        extended_movie_plot.text = getString(R.string.Plot, movie.Plot)
    }
}