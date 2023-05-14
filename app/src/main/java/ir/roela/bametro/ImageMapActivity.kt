package ir.roela.bametro

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ir.roela.bametro.databinding.ActivityImageMapBinding
import ir.roela.bametro.utils.AsyncCall
import ir.roela.bametro.utils.DataListener
import java.io.File

class ImageMapActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityImageMapBinding
    private lateinit var mapImageView: ImageView

    //    private val DUBAI_METRO_MAP = "https://db3pap003files.storage.live.com/y4mFCXYNUeQeib8SyrVbuipPEe5VU7a0jE-p4D-PAQF3aL-i8MGSJzT5Tui4dpXYsydhE44Ocd9kfTxvg-yu6YHlgdArY0g7ZYuIGv_CAlfGtL6LyYqIs6dUxLZBj9fXNo2-ufTzB8cFb_BoisBXitUZI_9Nu0W8NNS_DZNYXrGgukPtmGhaWqcIi_dUhVD5SfB?width=4967&height=6993&cropmode=none"
    private val DUBAI_METRO_MAP =
        "https://github.com/hamedhafez1/bametropics/blob/main/rail-network-map-august-2021_3.jpg?raw=true"
    private val TEHRAN_METRO_MAP =
        "https://github.com/hamedhafez1/bametropics/blob/main/tehran-metro-subway-map.jpg?raw=true"

    private fun policyConfig() { //for run app on android 5 and up
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        policyConfig()
        viewBinding = ActivityImageMapBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val cacheFolder = File("${filesDir}/cache/"/*Constants.DIR_CACHE*/)
        if (!cacheFolder.exists()) {
            try {
                cacheFolder.mkdirs()
            } catch (e: Exception) {
                Log.e("ee", e.message.toString())
            }
        }

        val mapToolbar = viewBinding.mapToolbar
        mapImageView = viewBinding.mapImgView

//        mapToolbar.setTitle(mainItemModel.itemName)
        mapToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mapToolbar.setNavigationOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                onBackPressedDispatcher.onBackPressed()
            }, 100)
        }
//        configImageMap("dubai_metro_map")


//        Glide.with(this)
//            .load("https://github.com/hamedhafez1/bametropics/blob/main/tehran-metro-subway-map.jpg?raw=true")
//            .placeholder(R.drawable.ic_dubai_burj_al_arab)
//            .into(mapImageView)

//        Picasso.get()
//            .load("https://github.com/hamedhafez1/bametropics/blob/main/tehran-metro-subway-map.jpg?raw=true")
//            .fit().into(mapImageView)
//        val config = ImageLoaderConfiguration.Builder(this).build()
//        val imageLoader = ImageLoader.getInstance()
//        imageLoader.init(config)
//        imageLoader.displayImage(TEHRAN_METRO_MAP, mapImageView)
    }

    private fun configImageMap(imageName: String) {
        val imgPath = "${filesDir}/cache/" + imageName
        if (!loadImage(imgPath)) {
            val task = AsyncCall(AsyncCall.TaskWS.downloadImage)
            AsyncCall.executeAsyncTask(task, imageName, TEHRAN_METRO_MAP)
            task.dataListener = DataListener { data ->
                if (data == "1") {
                    Toast.makeText(this@ImageMapActivity, "downloaded", Toast.LENGTH_SHORT).show()
                    loadImage(imgPath)
                }
            }
        }

    }

    private fun loadImage(imagePath: String): Boolean {
        val file = File(imagePath)
        if (file.exists()) {
            val recBmp = BitmapFactory.decodeFile(imagePath)
            if (recBmp != null) {
                mapImageView.setImageBitmap(recBmp)
                mapImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                return true
            }
        }
        return false
    }

    private fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }
}