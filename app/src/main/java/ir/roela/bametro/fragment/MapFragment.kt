package ir.roela.bametro.fragment

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ir.roela.bametro.R
import ir.roela.bametro.databinding.FragmentMapBinding
import ir.roela.bametro.grid.MainItemModel
import ir.roela.bametro.utils.AppDialogHelper
import ir.roela.bametro.utils.MapType


class MapFragment : Fragment() {

    private lateinit var fragmentMapBinding: FragmentMapBinding
    private lateinit var mapWebView: WebView
    private lateinit var loadingSnackBar: Snackbar

    companion object {
        private val TAG = this::class.java.simpleName
        private lateinit var mainItemModel: MainItemModel

        @JvmStatic
        fun newInstance(mainItemModel: MainItemModel): MapFragment {
            this.mainItemModel = mainItemModel
            return MapFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingSnackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            R.string.loading,
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(context))
        fragmentMapBinding.mapToolbar.apply {
            setTitle(mainItemModel.itemName)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }, 100)
            }
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_help) {
                    AppDialogHelper(requireContext()).showHelpRequireInternet(mainItemModel.isOffline).show()
                }
                true
            }
        }
        mapWebView = fragmentMapBinding.mapWebView
        return fragmentMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapWebView.settings.setSupportZoom(true)
        mapWebView.settings.builtInZoomControls = true
        mapWebView.settings.displayZoomControls = false

        when (mainItemModel.mapType) {
            MapType.TEHRAN_METRO -> {
                loadMap("file:///android_asset/metro/metro.html")
            }

            MapType.TEHRAN_BRT_BUS -> {
                loadMap("file:///android_asset/brt/brt.html")
            }

            MapType.TEHRAN_METRO_BRT_COMBINED -> {
                requireInternetConnection()
                loadMap("file:///android_asset/tehran_map/tehran_metro_brt_combined/tehran_metro_brt_combined.html")
            }

            MapType.TEHRAN_MOUNT -> {
                loadMap("file:///android_asset/kooh_tehran/kooh_tehran.html")
            }

            MapType.TEHRAN_CEMETERY -> {
                loadMap("file:///android_asset/behesht_zahra/behesht_zahra.html")
            }

            MapType.TEHRAN_BOOK_FAIR -> {
                loadMap("file:///android_asset/tehran_map/tehran_book_fair/tehran_book_fair.html")
            }

            MapType.ISFAHAN_METRO -> {
                loadMap("file:///android_asset/isfahan/isfahan_metro.html")
            }

            MapType.TABRIZ_METRO -> {
                loadMap("file:///android_asset/tabriz/tabriz_metro.html")
            }

            MapType.ISTANBUL_METRO -> {
                requireInternetConnection()
                loadMap("file:///android_asset/istanbul/istanbul_metro.html")
            }

            MapType.DUBAI_METRO -> {
                requireInternetConnection()
                loadMap("file:///android_asset/dubai/dubai_metro.html")
            }

            else -> {
                loadMap("file:///android_asset/metro/metro.html")
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
                if (loadingSnackBar.isShown)
                    loadingSnackBar.dismiss()
            }
        }
    }

    private fun loadMap(url: String) {
        try {
            mapWebView.loadUrl(url)
        } catch (e: Exception) {
            Log.e(TAG, "loadMap: ${e.message}")
        }
    }

    private fun requireInternetConnection() {
        try {
            loadingSnackBar.setText(R.string.internet_is_require)
            loadingSnackBar.show()
        } catch (e: Exception) {
            Log.e(TAG, "requireInternetConnection: ${e.message}")
        }
    }

}