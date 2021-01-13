package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.COUNT
import ua.kpi.comsys.iv7124.mymovies.IMAGES_API_KEY
import ua.kpi.comsys.iv7124.mymovies.IMAGES_REQUEST
import ua.kpi.comsys.iv7124.mymovies.R
import ua.kpi.comsys.iv7124.mymovies.ui.movies.Movie
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ImagesFragment : Fragment() {

    private lateinit var imagesAdapter: ImagesTableListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_images, container, false)

        imagesAdapter = ImagesTableListAdapter(mutableListOf(ImagesTable()))

        var images: List<Image>?
        lifecycleScope.launch(Dispatchers.IO) {
            images = tryGetImagesFromHttp()
            activity?.runOnUiThread {
                if (images == null) {
                    images = tryGetImagesFromRealm()
                }
                if (images == null) {
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(context, "No images found in storage", duration)
                    toast.show()
                } else {
                    val imagesTables = convertToTables(images!!)
                    imagesAdapter = ImagesTableListAdapter(imagesTables)
                    images_list.adapter = imagesAdapter
                    imagesAdapter.notifyDataSetChanged()
                }
            }
        }

        val recyclerView: RecyclerView = root.findViewById(R.id.images_list)
        recyclerView.adapter = imagesAdapter

        return root
    }

    private fun tryGetImagesFromHttp(): List<Image>? {
        val url = URL("https://pixabay.com/api/?key=$IMAGES_API_KEY&q=$IMAGES_REQUEST&image_type=photo&per_page=$COUNT")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val input = BufferedInputStream(urlConnection.inputStream)
            val streamReader = InputStreamReader(input)
            val gson = Gson()
            return gson.fromJson(streamReader,  ImageSearchResult::class.java).hits
        } catch(ex: IOException) {
            ex.printStackTrace()
        }finally {
            urlConnection.disconnect()
        }
        return null
    }

    private fun tryGetImagesFromRealm(): List<Image>? {
        val realm = Realm.getDefaultInstance()
        val fromRealm = realm.where(Image::class.java).findAll()
        return fromRealm?.map { it.deepCopy() }
    }
}