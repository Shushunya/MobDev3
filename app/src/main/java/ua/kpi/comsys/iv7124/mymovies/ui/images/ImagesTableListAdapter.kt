package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.R
import java.lang.Exception


class ImagesTableListAdapter(private var list: MutableList<ImagesTable>) : RecyclerView.Adapter<ImagesTableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesTableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImagesTableViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ImagesTableViewHolder, position: Int) {
        val imagesTable = list[position]
        holder.bind(imagesTable, this)
    }

    override fun getItemCount(): Int = list.size
}

class ImagesTableViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.images_table_item, parent, false)){

    private var imageViews = mutableListOf<ImageView>()
    private var bars = mutableListOf<ProgressBar>()
    init {
        for (i in 1..IMAGES_COUNT) {
            imageViews.add(itemView.findViewWithTag("image$i"))
            bars.add(itemView.findViewWithTag("progressBar$i"))
        }
    }

    fun bind(imagesTable: ImagesTable, adapter: ImagesTableListAdapter) {
        for (i in 0 until imageViews.count())
        {
            bars[i].visibility = View.VISIBLE
            val currentImage = imagesTable.images[i]
            if (currentImage == null) {
                imageViews[i].setImageResource(android.R.color.transparent)
            } else {
                var imageBitmap = currentImage.getBipMap()
                if (imageBitmap == null) {
                    GlobalScope.launch(Dispatchers.IO) {
                        imageBitmap = currentImage.loadBipMapFromUrl()
                        Handler(Looper.getMainLooper()).post {
                            if (imageBitmap == null) {
                                imageViews[i].setImageResource(android.R.color.transparent)
                            } else {
                                displayBitmap(imageBitmap!!, i, adapter)
                                val realm = Realm.getDefaultInstance()
                                realm.beginTransaction()
                                currentImage.setBipMap(imageBitmap!!)
                                realm.copyToRealmOrUpdate(currentImage)
                                realm.commitTransaction()
                            }
                        }
                    }
                } else {
                    displayBitmap(imageBitmap!!, i, adapter)
                }

            }
        }
    }

    private fun displayBitmap(bmp: Bitmap, i: Int, adapter: ImagesTableListAdapter) {
        imageViews[i].requestLayout()
        imageViews[i].layoutParams.height = imageViews[i].width
        imageViews[i].setImageBitmap(bmp)
        bars[i].visibility = View.GONE
        try {
            adapter.notifyDataSetChanged()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}