package ir.roela.simpleweathermodule

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ir.roela.simpleweathermodule.databinding.SimpleWeatherBinding
import org.json.JSONException
import java.net.URL
import java.util.*
import kotlin.math.roundToInt

class SimpleWeatherFragment : Fragment() {

    private lateinit var txtCityTemp: TextView
    private lateinit var txtTodayInfo: TextView
    private lateinit var txtWeatherDesc: TextView
    private lateinit var imgWeatherState: ImageView
    private lateinit var progressBarWeather: ProgressBar
    private lateinit var lytDate: LinearLayout
    private val timerWeather = Timer()
    private val timerDate = Timer()

    companion object {
        private val TAG: String = this::class.java.simpleName
        private var openWeatherMapApiKey: String = "a9bc40e37b9e8727e004e89d553ecb84"

        @JvmStatic
        fun getInstance(): SimpleWeatherFragment {
            return SimpleWeatherFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            SimpleWeatherBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        txtCityTemp = binding.txtCityTemp
        txtTodayInfo = binding.txtBannerTodayInfo
        txtWeatherDesc = binding.txtWeatherDesc
        imgWeatherState = binding.imgWeatherState
        progressBarWeather = binding.progressBarWeather
        lytDate = binding.lytDate
        val spinnerCities: Spinner = binding.spinnerCities

//        txtTodayInfo.text = PersianCalendar().persianLongDate

//        timerDate.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                activity?.runOnUiThread { txtTodayInfo.text = PersianCalendar().persianLongDate }
//            }
//        }, 0, 60000)

        val adapter = SpinnerAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            listOf(*resources.getStringArray(R.array.iran_cities_fa))
        )
        adapter.setDropDownViewResource(R.layout.dropdown_view)
        spinnerCities.adapter = adapter
        val userCityIndex = getUserCityIndex()
        if (userCityIndex != 0) {
            spinnerCities.setSelection(userCityIndex)
        }
        spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val city = resources.getStringArray(R.array.iran_cities_en)[position]
                val oldUserCity = userCity
                if (city != null && oldUserCity != city)
                    userCity = city
                getWeatherData(250)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        checkConnectivityState()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timerWeather.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getWeatherData(0)
            }
        }, 100, 120000)
    }

    private fun checkConnectivityState() {
        try {
            ConnectivityStateMonitor(object : Callback {
                override fun onCallback(any: Any?) {
                    if (any as Boolean) {
                        getWeatherData(2000)
                    }
                }
            }).enable(requireContext())
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimers()
    }

    private fun cancelTimers() {
        timerDate.cancel()
        timerWeather.cancel()
    }

    private fun getUserCityIndex(): Int {
        val citiesArray = resources.getStringArray(R.array.iran_cities_en)
        for (city in citiesArray) {
            if (city == userCity) {
                return citiesArray.indexOf(city)
            }
        }
        return 0
    }

    private var userCity: String
        get() {
            return try {
                val prefs = requireContext().getSharedPreferences(
                    requireContext().packageName,
                    Context.MODE_PRIVATE
                )
                prefs.getString("userCity", "tehran").toString()
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                "tehran"
            }
        }
        set(userCity) {
            try {
                val editor = requireContext().getSharedPreferences(
                    requireContext().packageName,
                    Context.MODE_PRIVATE
                ).edit()
                editor.remove("userCity")
                editor.putString("userCity", userCity)
                editor.apply()
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }

    private fun getWeatherData(delayCall: Long) {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                getWeatherWebService()
            }, delayCall)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    private fun getWeatherWebService() {
        try {
            if (isNetworkConnected(requireContext())) {
                txtTodayInfo.text = ""
                lytDate.visibility = View.GONE
                val url =
                    "https://api.openweathermap.org/data/2.5/weather?units=metric&lang=fa&q=$userCity&appid=$openWeatherMapApiKey"
                val queue = Volley.newRequestQueue(requireContext())
                val request = JsonObjectRequest(Request.Method.GET, url, null, {
                    try {
                        val jsonObject = it.getJSONObject("main")
                        val temperature = jsonObject.getString("temp")
                        txtCityTemp.text = temperature.toDouble().roundToInt().toString()

                        val weatherObject = it.getJSONArray("weather").getJSONObject(0)
                        val iconCode = weatherObject.getString("icon")
                        var description = weatherObject.getString("description")
                        if (description.trim() == "ابرهای پارچه پارچه شده") {
                            description = "ابرهای پراکنده"
                        }
                        txtWeatherDesc.text = description
                        val iconUrl = URL("https://openweathermap.org/img/wn/$iconCode@2x.png")
                        try {
                            Glide.with(requireContext())
                                .load(iconUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgWeatherState)
                        } catch (e: Exception) {
                            Log.e(TAG, e.message.toString())
                        }
                    } catch (e: JSONException) {
                        Log.e(TAG, e.message.toString())
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    } finally {
                        activity?.runOnUiThread { progressBarWeather.visibility = View.GONE }
                    }
                }, {
                    Log.e(TAG, it.message.toString())
                    txtCityTemp.text = requireContext().getString(R.string.etc)
                })
                queue.add(request)
                activity?.runOnUiThread { progressBarWeather.visibility = View.VISIBLE }
            } else {
                activity?.runOnUiThread { progressBarWeather.visibility = View.GONE }
                txtTodayInfo.text = "به اینترنت متصل نیستید"
                lytDate.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

}