package ua.kpi.comsys.iv7124.mymovies.ui.paint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_paint.*
import ua.kpi.comsys.iv7124.mymovies.R

class PaintFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paint, container, false)



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        graphChip.setOnCheckedChangeListener { _, isChecked ->
            graph_view.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        diagramChip.setOnCheckedChangeListener { _, isChecked ->
            diagram_view.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }
}

