package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.COUNT
import ua.kpi.comsys.iv7124.mymovies.IMAGES_API_KEY
import ua.kpi.comsys.iv7124.mymovies.IMAGES_REQUEST
import ua.kpi.comsys.iv7124.mymovies.R
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

        lifecycleScope.launch(Dispatchers.IO) {
            val imagesTables = getImages()
            activity?.runOnUiThread {
                imagesAdapter = ImagesTableListAdapter(imagesTables)
                images_list.adapter = imagesAdapter
            }
        }

        val recyclerView: RecyclerView = root.findViewById(R.id.images_list)
        recyclerView.adapter = imagesAdapter

        return root
    }

    private fun getImages(): MutableList<ImagesTable> {
        val url = URL("https://pixabay.com/api/?key=$IMAGES_API_KEY&q=$IMAGES_REQUEST&image_type=photo&per_page=$COUNT")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val input = BufferedInputStream(urlConnection.inputStream)
            val streamReader = InputStreamReader(input)
            val gson = Gson()
            val images = gson.fromJson(streamReader,  ImageSearchResult::class.java).hits
            return convertToTables(images)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }finally {
            urlConnection.disconnect()
        }
        return mutableListOf()
    }
}