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
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ir.roela.bametro.R
import ir.roela.bametro.databinding.FragmentNeshanMapBinding
import org.neshan.mapsdk.MapView

class NeshanActivity : AppCompatActivity() {
    private lateinit var neshanMap: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val textView = TextView(this)
            textView.setText(R.string.this_section_not_supported_on_your_device)
            textView.textSize = 20F
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            textView.layoutParams = params
            textView.gravity = Gravity.CENTER
            setContentView(textView)
        } else {

            val binding = FragmentNeshanMapBinding.inflate(LayoutInflater.from(this))
            setContentView(binding.root)

            checkLocationPermission().let { isGranted ->
                if (!isGranted) {
                    startLocationPermissionRequest()
                }
            }

            neshanMap = binding.neshanMap
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
            findViewById(android.R.id.content),
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
                this,
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
            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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