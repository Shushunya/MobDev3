package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.extended_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.MOVIES_API_KEY
import ua.kpi.comsys.iv7124.mymovies.R
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ExtendedMoviePopUp  : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extended_movie)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width: Int = (dm.widthPixels)
        val height: Int = (dm.heightPixels)
        window.setLayout(width, height)

        realm = Realm.getDefaultInstance()

        val selectedMovieId = intent.getStringExtra(selectedMovieId) ?: ""

        setTextVisibility(View.GONE)
        movieProgress.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            var movie = tryGetMovieFromHttp(selectedMovieId)
            var poster: Bitmap? = null
            runOnUiThread {
                if (movie == null) {
                    movie = tryGetMovieFromRealm(selectedMovieId)
                }
                else {
                    updateMovieInRealm(movie!!)
                }
                poster = movie?.getPosterBipMap()
            }
            if (poster == null) {
                poster = movie?.loadPosterBipMapFromUrl()
            }
            runOnUiThread {
                if (poster != null && movie != null) {
                    realm.beginTransaction()
                    movie?.setPosterBipMap(poster!!)
                    realm.copyToRealmOrUpdate(movie!!)
                    realm.commitTransaction()
                }
                onResult(movie, poster)
            }

        }
    }

    private fun setTextVisibility(v: Int) {
        extended_movie_poster.visibility = v
        extended_movie_title.visibility = v
        extended_movie_year.visibility = v
        extended_movie_genre.visibility = v
        extended_movie_director.visibility = v
        extended_movie_actors.visibility = v
        extended_movie_country.visibility = v
        extended_movie_language.visibility = v
        extended_movie_production.visibility = v
        extended_movie_released.visibility = v
        extended_movie_runtime.visibility = v
        extended_movie_awards.visibility = v
        extended_movie_rating.visibility = v
        extended_movie_plot.visibility = v
    }

    private fun onResult(movie: Movie?, poster: Bitmap?) {
        if (movie == null) {
            return
        }
        if (poster == null) {
            extended_movie_poster?.setImageResource(R.drawable.ic_movies_black_24dp)
        } else {
            extended_movie_poster.setImageBitmap(poster)
        }
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

        setTextVisibility(View.VISIBLE)
        movieProgress.visibility = View.GONE
    }

    fun onBackButtonClicked(view: View) {
        finish()
    }

    fun tryGetMovieFromHttp(id: String): Movie? {
        val url = URL("http://www.omdbapi.com/?apikey=$MOVIES_API_KEY&i=$id")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val input = BufferedInputStream(urlConnection.inputStream)
            val streamReader = InputStreamReader(input)
            val gson = Gson()
            return gson.fromJson(streamReader, Movie::class.java)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }finally {
            urlConnection.disconnect()
        }
        return null
    }

    private fun updateMovieInRealm(m: Movie) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(m)
        realm.commitTransaction()
    }

    private fun tryGetMovieFromRealm(id: String): Movie? {
        return realm.where(Movie::class.java).equalTo("imdbID", id).findFirst()
    }
}