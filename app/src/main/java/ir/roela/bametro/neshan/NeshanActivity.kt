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
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView

class NeshanActivity : AppCompatActivity() {

    /*private val TAG: String = NeshanActivity::class.java.name


    // location updates interval - 1 sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

    // fastest updates interval - 1 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

    // used to track request permissions
    private val REQUEST_CODE = 123


    private var userLocation: Location? = null*/
    private lateinit var neshanMap: MapView

    /*private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private var locationSettingsRequest: LocationSettingsRequest? = null
    private var locationCallback: LocationCallback? = null
    private var lastUpdateTime: String? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null
    private var marker: Marker? = null*/

    private lateinit var binding: FragmentNeshanMapBinding

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
            binding = FragmentNeshanMapBinding.inflate(LayoutInflater.from(this))
            setContentView(binding.root)

            checkLocationPermission().let { isGranted ->
                if (!isGranted) {
                    startLocationPermissionRequest()
                }
            }

            initMap()

        }
    }

    //    override fun onStart() {
//        super.onStart()
//        // everything related to ui is initialized here
//        initLayoutReferences()
//        // Initializing user location
//        initLocation()
//        startReceivingLocationUpdates()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        startLocationUpdates()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopLocationUpdates()
//    }
//
//    // Initializing layout references (views, map and map events)
//    private fun initLayoutReferences() {
//        // Initializing views
//        initViews()
//        // Initializing mapView element
//        initMap()
//    }
//
    private fun initMap() {
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
        // Setting map focal position to a fixed position and setting camera zoom
        neshanMap.moveCamera(LatLng(35.701, 51.419), 0f)
        neshanMap.setZoom(14f, 0f)
    }
//
//    private fun initViews() {
//        neshanMap = findViewById(R.id.neshan_map)
//    }
//
//    private fun initLocation() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        settingsClient = LocationServices.getSettingsClient(this)
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                // location is received
//                userLocation = locationResult.lastLocation
//                lastUpdateTime = DateFormat.getTimeInstance().format(Date())
//                onLocationChange()
//            }
//        }
//        mRequestingLocationUpdates = false
//        locationRequest = LocationRequest()
//        locationRequest.numUpdates = 10
//        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
//        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(locationRequest)
//        locationSettingsRequest = builder.build()
//    }
//
//    private fun startReceivingLocationUpdates() {
//        // Requesting ACCESS_FINE_LOCATION using Dexter library
//        Dexter.withContext(this)
//            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//                    mRequestingLocationUpdates = true
//                    startLocationUpdates()
//                }
//
//                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                    if (response.isPermanentlyDenied) {
//                        // open device settings when the permission is
//                        // denied permanently
//                        openSettings()
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: PermissionRequest?,
//                    token: PermissionToken
//                ) {
//                    token.continuePermissionRequest()
//                }
//            }).check()
//    }
//
//    /**
//     * Starting location updates
//     * Check whether location settings are satisfied and then
//     * location updates will be requested
//     */
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        settingsClient.checkLocationSettings(locationSettingsRequest!!).addOnSuccessListener(this) {
//            Log.i(TAG, "All location settings are satisfied.")
//            fusedLocationClient.requestLocationUpdates(
//                locationRequest,
//                locationCallback!!,
//                Looper.myLooper()
//            )
//            onLocationChange()
//        }.addOnFailureListener(this) { e ->
//            val statusCode = (e as ApiException).statusCode
//            when (statusCode) {
//                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                    Log.i(
//                        TAG,
//                        "Location settings are not satisfied. Attempting to upgrade " +
//                                "location settings "
//                    )
//                    if (mRequestingLocationUpdates == true) {
//                        try {
//                            // Show the dialog by calling startResolutionForResult(), and check the
//                            // result in onActivityResult().
//                            val rae = e as ResolvableApiException
//                            rae.startResolutionForResult(this, REQUEST_CODE)
//                        } catch (sie: IntentSender.SendIntentException) {
//                            Log.i(
//                                TAG,
//                                "PendingIntent unable to execute request."
//                            )
//                        }
//                    }
//                }
//                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                    val errorMessage = "Location settings are inadequate, and cannot be " +
//                            "fixed here. Fix in Settings."
//                    Log.e(
//                        TAG,
//                        errorMessage
//                    )
//                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
//                }
//            }
//            onLocationChange()
//        }
//    }
//
//    private fun stopLocationUpdates() {
//        // Removing location updates
//        fusedLocationClient.removeLocationUpdates(locationCallback!!).addOnCompleteListener(this) {
//            Toast.makeText(applicationContext, "Location updates stopped!", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//
//    private fun onLocationChange() {
//        if (userLocation != null) {
//            addUserMarker(LatLng(userLocation!!.latitude, userLocation!!.longitude))
//        }
//    }
//
//    private fun openSettings() {
//        val intent = Intent()
//        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//        val uri = Uri.fromParts(
//            "package",
//            BuildConfig.APPLICATION_ID, null
//        )
//        intent.data = uri
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//    }
//
//    private fun addUserMarker(loc: LatLng) {
//        //remove existing marker from map
//        if (marker != null) {
//            neshanMap.removeMarker(marker)
//        }
//        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
//        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
//        val markStCr = MarkerStyleBuilder()
//        markStCr.size = 30f
//        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
//            BitmapFactory.decodeResource(
//                resources, R.drawable.ic_marker
//            )
//        )
//        val markSt = markStCr.buildStyle()
//
//        // Creating user marker
//        marker = Marker(loc, markSt)
//
//        // Adding user marker to map!
//        neshanMap.addMarker(marker)
//    }
//
//    fun focusOnUserLocation(view: View?) {
//        if (userLocation != null) {
//            neshanMap.moveCamera(
//                LatLng(userLocation!!.latitude, userLocation!!.longitude), 0.25f
//            )
//            neshanMap.setZoom(15f, 0.25f)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQUEST_CODE -> when (resultCode) {
//                RESULT_OK -> Log.e(
//                    TAG,
//                    "User agreed to make required location settings changes."
//                )
//                RESULT_CANCELED -> {
//                    Log.e(
//                        TAG,
//                        "User choose not to make required location settings changes."
//                    )
//                    mRequestingLocationUpdates = false
//                }
//            }
//        }
//    }

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