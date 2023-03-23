package ir.roela.bametro.neshan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ir.roela.bametro.R
import ir.roela.bametro.databinding.FragmentNeshanMapBinding
import org.neshan.mapsdk.MapView

class NeshanMapFragment : Fragment() {

    private lateinit var fragmentNeshanMapBinding: FragmentNeshanMapBinding
    private lateinit var neshanMap: MapView

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val textView = TextView(requireContext())
            textView.setText(R.string.this_section_not_supported_on_your_device)
            textView.textSize = 20F
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            params.width = LayoutParams.MATCH_PARENT
            params.height = LayoutParams.MATCH_PARENT
            textView.layoutParams = params
            textView.gravity = Gravity.CENTER
            return textView
        }
        fragmentNeshanMapBinding = FragmentNeshanMapBinding.inflate(LayoutInflater.from(context))
        return fragmentNeshanMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkLocationPermission().let { isGranted ->
                if (!isGranted) {
                    startLocationPermissionRequest()
                }
            }

            neshanMap = fragmentNeshanMapBinding.neshanMap
            neshanMap.settings.isZoomControlsEnabled = true
            neshanMap.myLocationEnabled = true
            neshanMap.settings.isMyLocationButtonEnabled = true
            neshanMap.setOnMyLocationButtonClickListener {
                if (checkLocationPermission() && !isLocationEnabled()) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                false
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            "",
            Snackbar.LENGTH_SHORT
        )
        if (isGranted) {
            // PERMISSION GRANTED
            snackBar.setText(R.string.location_permission_granted)
        } else {
            // PERMISSION NOT GRANTED
            snackBar.setText(R.string.location_permission_not_granted)
        }
        snackBar.show()
    }

    private fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun isLocationEnabled(): Boolean {
        try {
            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!gpsEnabled && !networkEnabled) {
                return false
            }
        } catch (ex: Exception) {
            return true
        }
        return true
    }

}