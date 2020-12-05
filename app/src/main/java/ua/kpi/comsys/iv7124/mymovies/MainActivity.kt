package ua.kpi.comsys.iv7124.mymovies

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import ua.kpi.comsys.iv7124.mymovies.ui.movies.Movie
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

var allMovies = mutableListOf<Movie>()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_movies, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        allMovies = importFromJSON(applicationContext, Data::class.java, R.raw.movies)?.Search?.toMutableList() ?: mutableListOf()
    }
}

fun <T> importFromJSON(context: Context, classT: Class<T>, fileId: Int): T? {
    var streamReader: InputStreamReader? = null
    var fileInputStream: InputStream? = null
    try {
        fileInputStream = context.resources.openRawResource(fileId)
        streamReader = InputStreamReader(fileInputStream)
        val gson = Gson()
        return gson.fromJson(streamReader, classT)
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
    return null
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

class Data {
    var Search: List<Movie> = listOf()
}