package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.kpi.comsys.iv7124.mymovies.R
import java.util.*

class MovieListAdapter(private val list: List<Movie>)
    : RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = list[position]
        holder.bind(movie)
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
        val posterId = getResId(movie.Poster, R.drawable::class.java)
        posterView?.setImageResource(if (posterId == -1) R.drawable.ic_movies_black_24dp else posterId)
    }
}

fun getResId(resName: String, c: Class<*>): Int {
    val dotIndex = resName.indexOf(".")
    val formattedName = if (dotIndex == -1) resName.toLowerCase(Locale.getDefault()) else resName.substring(0 until dotIndex)
        .toLowerCase(Locale.getDefault())
    return try {
        val idField = c.getDeclaredField(formattedName)
        idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}