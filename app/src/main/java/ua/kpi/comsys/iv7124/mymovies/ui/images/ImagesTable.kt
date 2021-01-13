package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.ua_kpi_comsys_iv7124_mymovies_ui_images_ImageRealmProxy
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

const val IMAGES_COUNT = 9

class ImagesTable {
    val images = MutableList<Image?>(IMAGES_COUNT){null}
    var nextImageIdx = 0

    fun isFull(): Boolean {return nextImageIdx == IMAGES_COUNT}

    fun addImage(image: Image?): Boolean {
        if (nextImageIdx == IMAGES_COUNT)
            return false
        images[nextImageIdx] = image
        nextImageIdx++
        return true
    }
}

fun convertToTables(images: List<Image>): MutableList<ImagesTable> {
    val result = mutableListOf(ImagesTable())
    for (image in images) {
        val lastTable = result.last()
        if (lastTable.isFull()) {
            val newTable = ImagesTable()
            newTable.addImage(image)
            result.add(newTable)
        } else {
            lastTable.addImage(image)
        }
    }
    return result
}

open class Image : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var webformatURL: String = ""
    var imageBitmap: ByteArray? = null

    fun getBipMap(): Bitmap? {
        imageBitmap ?: return null
        val stream = ByteArrayInputStream(imageBitmap)
        return BitmapFactory.decodeStream(stream)
    }

    fun loadBipMapFromUrl(): Bitmap? {
        val url = URL(webformatURL)
        try {
            val inputStream = url.openStream()
            return BitmapFactory.decodeStream(inputStream)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    fun setBipMap(bmp: Bitmap) {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        imageBitmap = stream.toByteArray()
    }

    fun deepCopy(): Image {
        val im = Image()
        if (this is ua_kpi_comsys_iv7124_mymovies_ui_images_ImageRealmProxy) {
            val realmObject = this as ua_kpi_comsys_iv7124_mymovies_ui_images_ImageRealmProxy
            im.id = realmObject.`realmGet$id`()
            im.webformatURL = realmObject.`realmGet$webformatURL`()
            im.imageBitmap = realmObject.`realmGet$imageBitmap`().clone()
        }
        return this
    }
}

class ImageSearchResult {
    var hits: List<Image> = listOf()
}