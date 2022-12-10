package ir.roela.bametro

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ir.roela.bametro.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private lateinit var fragmentMapBinding: FragmentMapBinding
    private lateinit var mapWebView: WebView
    private lateinit var loadingSnackBar: Snackbar

    companion object {
        private lateinit var mapType: MapType

        @JvmStatic
        fun newInstance(mapType: MapType): MapFragment {
            this.mapType = mapType
            return MapFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingSnackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            R.string.loading,
            Snackbar.LENGTH_INDEFINITE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(context))
        return fragmentMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapWebView = fragmentMapBinding.mapWebView
        mapWebView.settings.setSupportZoom(true)
        mapWebView.settings.builtInZoomControls = true
        mapWebView.settings.displayZoomControls = false
        when (mapType.value) {
            MapType.METRO.value -> {
                loadMap("file:///android_asset/metro/metro.html")
            }
            MapType.METRO_TIMES.value -> {
                loadMap("file:///android_asset/metro_times/metro_times.html")
            }
            MapType.BUS.value -> {
                loadMap("file:///android_asset/brt/brt.html")
            }
            MapType.CEMETERY.value -> {
                loadMap("file:///android_asset/behesht_zahra/behesht_zahra.html")
            }
            else -> {
                loadMap("file:///android_asset/metro.html")
            }
        }
        mapWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (!loadingSnackBar.isShown) {
                    loadingSnackBar.setText(R.string.loading)
                    loadingSnackBar.show()
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingSnackBar.setText(R.string.loaded)
                Handler(Looper.getMainLooper()).postDelayed({
                    if (loadingSnackBar.isShown)
                        loadingSnackBar.dismiss()
                }, 500)
            }
        }
    }

    private fun loadMap(url: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            mapWebView.loadUrl(url)
        }, 500)
    }


}