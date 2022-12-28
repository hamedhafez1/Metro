package ir.roela.bametro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.roela.bametro.databinding.FragmentNeshanMapBinding

class NeshanMapFragment : Fragment() {

    private lateinit var fragmentNeshanMapBinding: FragmentNeshanMapBinding

    companion object {

        @JvmStatic
        fun newInstance(): NeshanMapFragment {
            return NeshanMapFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentNeshanMapBinding = FragmentNeshanMapBinding.inflate(LayoutInflater.from(context))
        return fragmentNeshanMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val neshanMap = fragmentNeshanMapBinding.neshanMap
        neshanMap.myLocationEnabled = true
        neshanMap.settings.isZoomControlsEnabled = true
    }

}