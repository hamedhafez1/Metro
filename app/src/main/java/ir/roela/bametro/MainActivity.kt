package ir.roela.bametro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ir.roela.bametro.databinding.ActivityMainGridBinding
import ir.roela.bametro.grid.MainItemModel
import ir.roela.bametro.grid.MainItemsGVAdapter
import ir.roela.bametro.neshan.NeshanActivity
import ir.roela.bametro.neshan.NeshanMapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val binding = ActivityMainGridBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val gridView = binding.gridview
        val mainItemsList = ArrayList<MainItemModel>()
        val menuItems = arrayOf(
            MainItemModel(MapType.TEHRAN_MAP_ONLINE, R.string.online_map, R.drawable.ic_map_24),
            MainItemModel(MapType.TEHRAN_METRO, R.string.metro, R.drawable.ic_subway_24),
            MainItemModel(MapType.TEHRAN_BRT_BUS, R.string.brt, R.drawable.ic_bus_24),
            MainItemModel(MapType.TEHRAN_MAP_OFFLINE, R.string.tehran_map, R.drawable.ic_map_24),
            MainItemModel(MapType.TEHRAN_MOUNT, R.string.kooh_tehran, R.drawable.ic_landscape_24),
            MainItemModel(
                MapType.TEHRAN_CEMETERY,
                R.string.behesht_zahra,
                R.drawable.ic_vintage_24
            ),
            MainItemModel(MapType.ISFAHAN_METRO, R.string.isfahan_metro, R.drawable.ic_subway_24),
            MainItemModel(MapType.TABRIZ_METRO, R.string.tabriz_metro, R.drawable.ic_bus_24)
        )

        mainItemsList.addAll(menuItems)

        gridView.adapter = MainItemsGVAdapter(this, mainItemsList)

        gridView.setOnItemClickListener { parent, view, position, id ->
            val mapType = menuItems[position].mapType
            if (position == 0) {
                startActivity(Intent(this, NeshanActivity::class.java))
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MapFragment.newInstance(mapType), mapType.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null).commit()
            }
//            val fragment = if (position == 0) {
//                NeshanMapFragment.newInstance()
//            } else {
//                MapFragment.newInstance(mapType)
//            }

        }

//        val tabLayout = binding.tabLayout
//        val viewPager = binding.viewPager
//
//        viewPager.isUserInputEnabled = false
//
//        val viewPagerAdapter = ViewPagerAdapter(this)
//        viewPager.adapter = viewPagerAdapter
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//        })
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            /*tabLayout.getTabAt(0)?.view?.isEnabled = false*/
//            tabLayout.getTabAt(1)?.select()
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about_us -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.about_us)
                    .setMessage(
                        getString(
                            R.string.version,
                            getString(R.string.thisAppVersion),
                            BuildConfig.VERSION_NAME
                        )
                    )
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