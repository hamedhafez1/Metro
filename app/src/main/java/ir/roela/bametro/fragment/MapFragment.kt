package ir.roela.bametro.fragment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ir.roela.bametro.App
import ir.roela.bametro.R
import ir.roela.bametro.databinding.FragmentMapBinding
import ir.roela.bametro.grid.MainItemModel
import ir.roela.bametro.utils.AppDialogHelper
import ir.roela.bametro.utils.MapType
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


class MapFragment : Fragment() {

    private lateinit var fragmentMapBinding: FragmentMapBinding
    private lateinit var mapWebView: WebView
    private lateinit var loadingSnackBar: Snackbar
    private lateinit var wifiSettingsLauncher: ActivityResultLauncher<Intent>
    private var isShowDialog = false

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
        // Initialize the ActivityResultLauncher
        wifiSettingsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                // This block is executed when the user returns from Wi-Fi settings
                if (App.isNetworkConnected(requireContext())) {
                    // Refresh or reload your activity
                    loadMapType()
                } else {
                    // Remove the fragment
                    closeMapFragment()
                    Toast.makeText(
                        requireContext(),
                        R.string.internet_is_require,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

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
                    AppDialogHelper(requireContext()).showHelpRequireInternet(mainItemModel.isOffline)
                        .show()
                }
                true
            }
        }
        mapWebView = fragmentMapBinding.mapWebView
        return fragmentMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapWebView.settings.apply {
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            domStorageEnabled = true
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
                }, 2000)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (!mainItemModel.isOffline) {
                    val url = request?.url.toString()
                    if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif")) {
                        val cachedFile = File(requireContext().cacheDir, "${url.hashCode()}.img")
                        if (cachedFile.exists()) {
                            // Serve from cache
                            return try {
                                WebResourceResponse(
                                    "image/*",
                                    "UTF-8",
                                    FileInputStream(cachedFile)
                                )
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                                Log.e(TAG, e.message.toString())
                                null
                            }
                        } else {
                            // Download and cache the image
                            App.isNetworkConnected(requireContext()).let {
                                if (!it && !isShowDialog) {
                                    isShowDialog = true
                                    showRequiredInternetDialog()
                                } else {
                                    try {
                                        val connection =
                                            URL(url).openConnection() as HttpURLConnection
                                        connection.connect()
                                        val inputStream = connection.inputStream
                                        val outputStream = FileOutputStream(cachedFile)
                                        inputStream.copyTo(outputStream)
                                        outputStream.close()
                                        inputStream.close()
                                        return WebResourceResponse(
                                            "image/*",
                                            "UTF-8",
                                            FileInputStream(cachedFile)
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        return null
                                    }
                                }
                            }
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        loadMapType()
    }

    private fun loadMapType() {
        when (mainItemModel.mapType) {
            MapType.TEHRAN_METRO -> {
                loadMap("file:///android_asset/metro/metro.html")
            }

            MapType.TEHRAN_BRT_BUS -> {
                loadMap("file:///android_asset/brt/brt.html")
            }

            MapType.TEHRAN_METRO_BRT_COMBINED -> {
//                requireInternetConnection()
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
//                requireInternetConnection()
                loadMap("file:///android_asset/istanbul/istanbul_metro.html")
            }

            MapType.DUBAI_METRO -> {
//                requireInternetConnection()
                loadMap("file:///android_asset/dubai/dubai_metro.html")
            }

            MapType.NAJAF_KARBALA_MAP -> {
//                requireInternetConnection()
                loadMap("file:///android_asset/iraq/najaf_karbala.html")
            }

            else -> {
                closeMapFragment()
//                loadMap("file:///android_asset/metro/metro.html")
            }
        }
    }

    private fun showRequiredInternetDialog() {
        requireActivity().runOnUiThread {
            AppDialogHelper(requireContext()).showHelpRequireInternet(false)
                .setPositiveButton(R.string.open_wireless_settings,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(
                            p0: DialogInterface?,
                            p1: Int
                        ) {
                            p0?.dismiss()
                            wifiSettingsLauncher.launch(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        }
                    })
                .setNegativeButton(R.string.close,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(
                            p0: DialogInterface?,
                            p1: Int
                        ) {
                            p0?.dismiss()
                            closeMapFragment()
                        }
                    })
                .setOnDismissListener {
                    isShowDialog = false
                }.setOnCancelListener {
                    isShowDialog = false
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun reloadMapFragment() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }

    private fun closeMapFragment() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.exit_to_left,
                R.anim.exit_to_right
            )
            .remove(this) // Removes the current fragment
            .commit()
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
            loadingSnackBar.duration = 3000
            loadingSnackBar.show()
        } catch (e: Exception) {
            Log.e(TAG, "requireInternetConnection: ${e.message}")
        }
    }

}