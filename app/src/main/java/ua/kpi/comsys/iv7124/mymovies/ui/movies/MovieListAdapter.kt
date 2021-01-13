package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.R

class MovieListAdapter(
    private var list: MutableList<Movie>,
    private val clickListener: (Movie) -> Unit
)
    : RecyclerView.Adapter<MovieViewHolder>(){

    private var fullList: MutableList<Movie> = list.toMutableList()

    fun updateDataSet(){
        notifyDataSetChanged()
        fullList = list.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = list[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { clickListener(movie) }
    }

    override fun getItemCount(): Int = list.size
}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.movie_item, parent, false)) {

    private var titleView: TextView? = null
    private var yearView: TextView? = null
    private var typeView: TextView? = null
    private var posterView: ImageView? = null
    init {
        titleView = itemView.findViewById(R.id.list_movie_title)
        yearView = itemView.findViewById(R.id.list_movie_year)
        typeView = itemView.findViewById(R.id.list_movie_type)
        posterView = itemView.findViewById(R.id.list_movie_poster)
    }

    fun bind(movie: Movie) {
        titleView?.text = movie.Title
        yearView?.text = movie.Year
        typeView?.text = movie.Type
        if (movie.posterBitmap != null) {
            posterView?.setImageBitmap(movie.getPosterBipMap())
        } else {
            val movieCopy = movie.copy()
            GlobalScope.launch(Dispatchers.IO) {
                val posterBitmap = movieCopy.loadPosterBipMapFromUrl()
                Handler(Looper.getMainLooper()).post {
                    if (posterBitmap == null) {
                        posterView?.setImageResource(R.drawable.ic_movies_black_24dp)
                    } else {
                        posterView?.setImageBitmap(posterBitmap)
                        val realm = Realm.getDefaultInstance()
                        realm.beginTransaction()
                        movie.setPosterBipMap(posterBitmap)
                        realm.copyToRealmOrUpdate(movie)
                        realm.commitTransaction()
                    }
                }
            }
        }
    }
}