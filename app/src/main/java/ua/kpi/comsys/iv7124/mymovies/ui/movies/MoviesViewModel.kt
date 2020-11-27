package ua.kpi.comsys.iv7124.mymovies.ui.movies

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ua.kpi.comsys.iv7124.mymovies.R
import java.io.*

class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val _movies = MutableLiveData(importFromJSON(getApplication()))
    val movies: LiveData<List<Movie>> = _movies
}

fun importFromJSON(context: Context): List<Movie> {
    var streamReader: InputStreamReader? = null
    var fileInputStream: InputStream? = null
    try {
        fileInputStream = context.resources.openRawResource(R.raw.movies)
        streamReader = InputStreamReader(fileInputStream)
        val gson = Gson()
        val data: Data = gson.fromJson(streamReader, Data::class.java)
        return data.Search
    } catch (ex: IOException) {
        ex.printStackTrace()
    } finally {
        if (streamReader != null) {
            try {
                streamReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    return listOf()
}

class Data {
    var Search: List<Movie> = listOf()
}