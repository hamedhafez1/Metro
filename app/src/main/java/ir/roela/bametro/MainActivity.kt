package ir.roela.bametro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ir.roela.bametro.databinding.AboutUsMyketBinding
import ir.roela.bametro.databinding.ActivityMainGridBinding
import ir.roela.bametro.fragment.MapFragment
import ir.roela.bametro.grid.MainItemsGVAdapter
import ir.roela.bametro.grid.getMainItemArray
import ir.roela.bametro.neshan.NeshanActivity
import ir.roela.bametro.utils.MapType

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainGridBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val maninToolbar = binding.mainToolbar
        setSupportActionBar(maninToolbar)
        val gridView = binding.gridview

        val menuItems = getMainItemArray()

        gridView.adapter = MainItemsGVAdapter(this, menuItems)

        gridView.setOnItemClickListener { _, _, position, _ ->
            val mapType = menuItems[position].mapType
            if (mapType == MapType.TEHRAN_MAP_ONLINE) {
                startActivity(Intent(this, NeshanActivity::class.java))
            } else {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_left,
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.exit_to_right
                    )
                    .replace(
                        android.R.id.content,
                        MapFragment.newInstance(menuItems[position]),
                        mapType.toString()
                    )
                    .addToBackStack(null).commit()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about_us -> {
                val aboutUsViewBinding = AboutUsMyketBinding.inflate(LayoutInflater.from(this))
                aboutUsViewBinding.btnOpenCommentPage.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("myket://comment?id=" + BuildConfig.APPLICATION_ID)
//                        intent.setPackage("com.farsitel.bazaar")
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("bametro", e.message.toString())
                    }
                }
                aboutUsViewBinding.imgQrcode.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("myket://details?id=" + BuildConfig.APPLICATION_ID)
//                        intent.setPackage("com.farsitel.bazaar")
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("bametro", e.message.toString())
                    }
                }
                aboutUsViewBinding.btnOpenMyAppsPage.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("myket://developer/" + BuildConfig.APPLICATION_ID)
//                        intent.setPackage("com.farsitel.bazaar")
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("bametro", e.message.toString())
                    }
                }
                val dialog = AlertDialog.Builder(this)
                    .setTitle(
                        getString(
                            R.string.version,
                            getString(R.string.thisAppVersion),
                            BuildConfig.VERSION_NAME
                        )
                    )
                    .setView(aboutUsViewBinding.root)
                    .setNegativeButton(R.string.close) { dialog, _ ->
                        dialog?.dismiss()
                    }
                dialog.show()
            }
        }
        return true
    }


}