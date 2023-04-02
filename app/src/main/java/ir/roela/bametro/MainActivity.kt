package ir.roela.bametro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ir.roela.bametro.databinding.ActivityMainGridBinding
import ir.roela.bametro.grid.MainItemModel
import ir.roela.bametro.grid.MainItemsGVAdapter
import ir.roela.bametro.neshan.NeshanActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainGridBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val maninToolbar = binding.mainToolbar
        setSupportActionBar(maninToolbar)
        val gridView = binding.gridview
        val mainItemsList = ArrayList<MainItemModel>()
        val menuItems = arrayOf(
//            MainItemModel(MapType.TEHRAN_MAP_ONLINE, R.string.online_map, R.drawable.ic_map_24),
            MainItemModel(MapType.TEHRAN_METRO, R.string.tehran_metro, R.drawable.ic_subway_48),
            MainItemModel(MapType.TEHRAN_BRT_BUS, R.string.brt, R.drawable.ic_bus_48),
            MainItemModel(MapType.TEHRAN_MAP_OFFLINE, R.string.tehran_map, R.drawable.ic_map_48),
            MainItemModel(MapType.TEHRAN_MOUNT, R.string.kooh_tehran, R.drawable.ic_montain_48),
            MainItemModel(
                MapType.TEHRAN_CEMETERY,
                R.string.behesht_zahra,
                R.drawable.ic_flower_48
            ),
            MainItemModel(MapType.ISFAHAN_METRO, R.string.isfahan_metro, R.drawable.ic_subway_48),
            MainItemModel(MapType.TABRIZ_METRO, R.string.tabriz_metro, R.drawable.ic_subway_48)
        )

        mainItemsList.addAll(menuItems)

        gridView.adapter = MainItemsGVAdapter(this, mainItemsList)

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
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.version, getString(R.string.thisAppVersion), BuildConfig.VERSION_NAME))
                    .setView(R.layout.about_us)
                    .setNegativeButton(R.string.close) { dialog, _ ->
                        dialog?.dismiss()
                    }
                    .show()
            }
        }
        return true
    }


}