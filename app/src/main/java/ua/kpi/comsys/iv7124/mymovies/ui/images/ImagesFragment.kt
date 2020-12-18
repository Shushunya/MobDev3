package ua.kpi.comsys.iv7124.mymovies.ui.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ua.kpi.comsys.iv7124.mymovies.R

class ImagesFragment : Fragment() {

    private lateinit var imagesViewModel: ImagesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        imagesViewModel =
                ViewModelProviders.of(this).get(ImagesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_images, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        imagesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}