package ir.roela.bametro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import ir.roela.bametro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.isUserInputEnabled = false

        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

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
                    .setNegativeButton(R.string.close) { dialog, p1 ->
                        dialog?.dismiss()
                    }
                    .show()
            }
        }
        return true
    }


}