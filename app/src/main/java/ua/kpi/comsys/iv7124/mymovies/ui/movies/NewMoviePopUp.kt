package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.new_movie_pop_up.*
import ua.kpi.comsys.iv7124.mymovies.R
import ua.kpi.comsys.iv7124.mymovies.allMovies

class NewMoviePopUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_movie_pop_up)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width: Int = (dm.widthPixels)
        val height: Int = (dm.heightPixels)

        window.setLayout(width, height)
    }

    fun onAddClicked(view: View) {
        val title = titleText.text.toString()
        val type = typeText.text.toString()
        val year = yearText.text.toString()
        val newMovie = Movie(title, year, "", type, "")
        allMovies.add(newMovie)
        finish()
    }
}