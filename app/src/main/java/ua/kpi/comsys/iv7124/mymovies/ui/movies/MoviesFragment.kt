package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.MOVIES_API_KEY
import ua.kpi.comsys.iv7124.mymovies.R
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val selectedMovieId= "movieId"

class MoviesFragment : Fragment() {

    private lateinit var moviesAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)

        moviesAdapter = MovieListAdapter(mutableListOf()){ movie -> showExtendedMovie(movie) }

        val recyclerView: RecyclerView = root.findViewById(R.id.movie_list)
        recyclerView.adapter = moviesAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesListProgress.visibility = View.GONE
        movie_search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                moviesListProgress.visibility = View.VISIBLE
                lifecycleScope.launch(Dispatchers.IO) {
                    val movies = getMovies(newText)
                    activity?.runOnUiThread { onHttpResult(view, movies) }
                }
                return false
            }
        })
    }

    private fun getMovies(request: String?): MutableList<Movie> {
        request ?: return mutableListOf()
        if (request.length < 3) return mutableListOf()

        val formattedRequest = request.replace(" ", "+")
        val url = URL("http://www.omdbapi.com/?apikey=$MOVIES_API_KEY&s=$formattedRequest&page=1")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val input = BufferedInputStream(urlConnection.inputStream)
            val streamReader = InputStreamReader(input)
            val gson = Gson()
            return gson.fromJson(streamReader,  MovieSearchResult::class.java).Search.toMutableList()
        } catch(ex: IOException) {
            ex.printStackTrace()
        }finally {
            urlConnection.disconnect()
        }
        return mutableListOf()
    }

    private fun onHttpResult(view: View, movies: MutableList<Movie>) {
        if (movies.isEmpty()) {
            noItemsText.visibility = View.VISIBLE
        } else {
            noItemsText.visibility = View.GONE
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.movie_list)
        moviesAdapter = MovieListAdapter(movies){movie -> showExtendedMovie(movie) }
        recyclerView.adapter = moviesAdapter
        moviesListProgress.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        moviesAdapter.updateDataSet()
    }

    private fun showExtendedMovie(movie: Movie) {
        val intent = Intent(activity?.applicationContext, ExtendedMoviePopUp::class.java)
        intent.putExtra(selectedMovieId, movie.imdbID)
        startActivity(intent)
    }
}