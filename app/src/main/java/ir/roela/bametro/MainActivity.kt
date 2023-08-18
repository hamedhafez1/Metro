package ir.roela.bametro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ir.roela.bametro.databinding.ActivityMainGridBinding
import ir.roela.bametro.fragment.MapFragment
import ir.roela.bametro.fragment.PhonesFragment
import ir.roela.bametro.grid.MainItemsGVAdapter
import ir.roela.bametro.grid.getMainItemArray
import ir.roela.bametro.neshan.NeshanActivity
import ir.roela.bametro.utils.AppDialogHelper
import ir.roela.bametro.utils.MapType
import ir.roela.bametro.utils.WelcomeText
import ir.roela.simpleweathermodule.SimpleWeatherFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainGridBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        mainToolbar = binding.mainToolbar
        setSupportActionBar(mainToolbar)
        val gridView = binding.gridview

        val menuItems = getMainItemArray()

        gridView.adapter = MainItemsGVAdapter(this, menuItems)

        gridView.setOnItemClickListener { _, _, position, _ ->
            when (val mapType = menuItems[position].mapType) {
                MapType.TEHRAN_MAP_ONLINE -> {
                    startActivity(Intent(this, NeshanActivity::class.java))
                }

                MapType.IRAN_NECESSARY_PHONES -> {
                    openFragment(
                        PhonesFragment.newInstance(),
                        "necessaryPhones"
                    )
                }

                else -> {
                    if (!menuItems[position].isOffline) {
                        App.isNetworkConnected(this).let {
                            if (it) {
                                openFragment(
                                    MapFragment.newInstance(menuItems[position]),
                                    mapType.toString()
                                )
                            } else {
                                AppDialogHelper(this).showHelpRequireInternet(false)
                                    .setPositiveButton(
                                        R.string.enter
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        openFragment(
                                            MapFragment.newInstance(menuItems[position]),
                                            mapType.toString()
                                        )
                                    }.show()
                            }
                        }
                    } else {
                        openFragment(
                            MapFragment.newInstance(menuItems[position]),
                            mapType.toString()
                        )
                    }
                }
            }
        }
        addWeatherFragment()
    }

    override fun onResume() {
        super.onResume()
        WelcomeText().getWelcome().let { welcomeText ->
            mainToolbar.title = "${getString(R.string.app_name_fa)} :  $welcomeText"
        }
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.exit_to_right
            )
            .replace(android.R.id.content, fragment, tag)
            .addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about_us -> {
                AppDialogHelper(this).showAboutUsCafeBazaar()
            }
        }
        return true
    }

    private fun addWeatherFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.weather_container, SimpleWeatherFragment.getInstance())
            .commit()
    }

    private fun removeWeatherFragment() {
        val fragmentWeather = supportFragmentManager.findFragmentById(R.id.weather_container)
        if (fragmentWeather != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragmentWeather)
                .commit()
        }
    }


}