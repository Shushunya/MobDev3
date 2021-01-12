package ua.kpi.comsys.iv7124.mymovies.ui.images

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

class Image {
    var id: String = ""
    var webformatURL: String = ""
}

class ImageSearchResult {
    var hits: List<Image> = listOf()
}