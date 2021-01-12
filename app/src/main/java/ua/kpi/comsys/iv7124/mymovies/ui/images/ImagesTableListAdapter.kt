package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.kpi.comsys.iv7124.mymovies.R
import ua.kpi.comsys.iv7124.mymovies.getBitmap


class ImagesTableListAdapter(private var list: MutableList<ImagesTable>) : RecyclerView.Adapter<ImagesTableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesTableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImagesTableViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ImagesTableViewHolder, position: Int) {
        val imagesTable = list[position]
        holder.bind(imagesTable)
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

    fun bind(imagesTable: ImagesTable) {
        for (i in 0 until imageViews.count())
        {
            bars[i].visibility = View.VISIBLE
            val currentImage = imagesTable.images[i]
            if (currentImage == null) {
                imageViews[i].setImageResource(android.R.color.transparent)
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    val imageBitmap = getBitmap(currentImage.webformatURL)
                    Handler(Looper.getMainLooper()).post {
                        if (imageBitmap == null) {
                            imageViews[i].setImageResource(android.R.color.transparent)
                        } else {
                            imageViews[i].requestLayout()
                            imageViews[i].layoutParams.height = imageViews[i].width
                            imageViews[i].setImageBitmap(imageBitmap)
                            bars[i].visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}