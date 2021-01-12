package ua.kpi.comsys.iv7124.mymovies

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import java.io.IOException
import java.net.URL

const val MOVIES_API_KEY = "7e9fe69e"
const val IMAGES_API_KEY = "19193969-87191e5db266905fe8936d565"
const val COUNT = 27
const val IMAGES_REQUEST = "yellow+flowers"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_movies, R.id.navigation_images, R.id.navigation_paint))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

fun getBitmap(imageUrl: String): Bitmap? {
    if (imageUrl == "N/A") return null
    val url = URL(imageUrl)
    try {
        val inputStream = url.openStream()
        return BitmapFactory.decodeStream(inputStream)
    } catch(ex: IOException) {
        ex.printStackTrace()
    }
    return null
}
