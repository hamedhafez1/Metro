package ir.roela.bametro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MapFragment.newInstance(MapType.METRO)
            1 -> MapFragment.newInstance(MapType.BUS)
            2 -> MapFragment.newInstance(MapType.CEMETERY)
            else -> MapFragment.newInstance(MapType.METRO)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}