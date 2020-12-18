package ua.kpi.comsys.iv7124.mymovies.ui.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImagesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is images Fragment"
    }
    val text: LiveData<String> = _text
}