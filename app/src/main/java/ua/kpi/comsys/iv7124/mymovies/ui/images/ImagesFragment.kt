package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_images.*
import ua.kpi.comsys.iv7124.mymovies.R

const val IMAGE_REQUEST = 24

class ImagesFragment : Fragment() {

    private lateinit var imagesAdapter: ImagesTableListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_images, container, false)

        imagesAdapter = ImagesTableListAdapter(mutableListOf(ImagesTable()))

        val recyclerView: RecyclerView = root.findViewById(R.id.images_list)
        recyclerView.adapter = imagesAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addImageButton.setOnClickListener {innerView ->
            onAddImageClicked(innerView)
        }
    }

    private fun onAddImageClicked (view: View)
    {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST) {
            val imageUri = data?.data
            imagesAdapter.update(imageUri)
        }
    }
}