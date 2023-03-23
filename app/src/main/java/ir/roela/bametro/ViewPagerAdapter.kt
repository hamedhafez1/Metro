package ir.roela.bametro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ir.roela.bametro.neshan.NeshanMapFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val tabsArray = arrayOf(
        MapType.METRO_TIMES,
        MapType.TEHRAN_BRT_BUS,
        MapType.TEHRAN_MAP_ONLINE,
        MapType.TEHRAN_MAP_OFFLINE,
        MapType.TEHRAN_MOUNT,
        MapType.TEHRAN_CEMETERY,
        MapType.ISFAHAN_METRO,
        MapType.TABRIZ_METRO,
    )

    override fun createFragment(position: Int): Fragment {
        return if (position == 2) {
            NeshanMapFragment.newInstance()
        } else {
            MapFragment.newInstance(tabsArray[position])
        }
        /*return when (position) {
            0 -> MapFragment.newInstance(tabsArray[0])
            1 -> MapFragment.newInstance(tabsArray[1])
            2 -> MapFragment.newInstance(tabsArray[2])
            3 -> NeshanMapFragment.newInstance()
            4 -> MapFragment.newInstance(tabsArray[4])
            5 -> MapFragment.newInstance(tabsArray[5])
            6 -> MapFragment.newInstance(tabsArray[6])
            else -> MapFragment.newInstance(tabsArray[0])
        }*/
    }

    override fun getItemCount(): Int {
        return tabsArray.size
    }

}