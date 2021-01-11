package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.net.Uri

const val IMAGES_COUNT = 9

class ImagesTable {
    val images = MutableList<Uri?>(IMAGES_COUNT){null}
    var nextImageIdx = 0

    fun isFull(): Boolean {return nextImageIdx == IMAGES_COUNT}

    fun addImage(image: Uri?): Boolean {
        if (nextImageIdx == IMAGES_COUNT)
            return false
        images[nextImageIdx] = image
        nextImageIdx++
        return true
    }
}