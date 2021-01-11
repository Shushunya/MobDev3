package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ua.kpi.comsys.iv7124.mymovies.R


class ImagesTableListAdapter(private var list: MutableList<ImagesTable>) : RecyclerView.Adapter<ImagesTableViewHolder>() {

    private var currentTablePosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesTableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImagesTableViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ImagesTableViewHolder, position: Int) {
        val imagesTable = list[position]
        holder.bind(imagesTable)
    }

    override fun getItemCount(): Int = list.size

    fun update(imageUri: Uri?) {
        val imagesTable = list[currentTablePosition]
        if (imagesTable.isFull()) {
            currentTablePosition++
            val newTable = ImagesTable()
            newTable.addImage(imageUri)
            list.add(newTable)
            notifyItemInserted(currentTablePosition)
        }
        else {
            imagesTable.addImage(imageUri)
            notifyItemChanged(currentTablePosition)
        }
    }
}

class ImagesTableViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.images_table_item, parent, false)){

    private var imageViews = mutableListOf<ImageView>()
    init {
        for (i in 1..IMAGES_COUNT) {
            imageViews.add(itemView.findViewWithTag("image$i"))
        }
    }

    fun bind(imagesTable: ImagesTable) {
        for (i in 0 until imageViews.count())
        {
            val currentImage = imagesTable.images[i]
            if (currentImage == null) {
                imageViews[i].setImageResource(android.R.color.transparent)
            } else {
                imageViews[i].setImageURI(currentImage)
            }
        }
    }
}