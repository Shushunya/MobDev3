package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_movies.*
import ua.kpi.comsys.iv7124.mymovies.R
import ua.kpi.comsys.iv7124.mymovies.allMovies

const val selectedMovieId= "movieId"

class MoviesFragment : Fragment() {

    private lateinit var moviesAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)

        moviesAdapter = MovieListAdapter(allMovies){movie -> showExtendedMovie(movie) }

        val recyclerView: RecyclerView = root.findViewById(R.id.movie_list)
        recyclerView.adapter = moviesAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newMovieButton.setOnClickListener {innerView ->
            onNewMovieClicked(innerView)
        }

        movie_search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val recyclerView: RecyclerView = view.findViewById(R.id.movie_list)
                val adapter = recyclerView.adapter as MovieListAdapter
                adapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        moviesAdapter.updateDataSet()
    }

    private fun onNewMovieClicked (view: View) {
        val intent = Intent(activity?.applicationContext, NewMoviePopUp::class.java)
        startActivity(intent)
    }

    private fun showExtendedMovie(movie: Movie) {
        val intent = Intent(activity?.applicationContext, ExtendedMoviePopUp::class.java)
        intent.putExtra(selectedMovieId, movie.imdbID)
        startActivity(intent)
    }
}