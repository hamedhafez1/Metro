package ir.roela.bametro.grid

import ir.roela.bametro.utils.MapType
import ir.roela.bametro.R

fun getMainItemArray(): List<MainItemModel> {
    return listOf(
//            MainItemModel(MapType.TEHRAN_MAP_ONLINE, R.string.online_map, R.drawable.ic_map_24),
//        MainItemModel(MapType.TEHRAN_BOOK_FAIR, R.string.tehran_bok_fair, R.drawable.ic_book),
        MainItemModel(
            MapType.IRAN_NECESSARY_PHONES,
            R.string.iran_necessary_phones,
            R.drawable.ic_emergency_call
        ),
        MainItemModel(MapType.TEHRAN_METRO, R.string.tehran_metro, R.drawable.ic_subway_48),
        MainItemModel(MapType.TEHRAN_BRT_BUS, R.string.brt, R.drawable.ic_bus_48),
        MainItemModel(
            MapType.TEHRAN_METRO_BRT_COMBINED,
            R.string.metro_brt_combined,
            R.drawable.ic_bus_48,
            false
        ),
        MainItemModel(MapType.TEHRAN_MOUNT, R.string.kooh_tehran, R.drawable.ic_montain_48),
        MainItemModel(
            MapType.TEHRAN_CEMETERY,
            R.string.behesht_zahra,
            R.drawable.ic_flower_48
        ),
        MainItemModel(MapType.ISFAHAN_METRO, R.string.isfahan_metro, R.drawable.ic_subway_48),
        MainItemModel(MapType.TABRIZ_METRO, R.string.tabriz_metro, R.drawable.ic_subway_48),
        MainItemModel(
            MapType.ISTANBUL_METRO,
            R.string.istanbul_metro,
            R.drawable.ic_istanbul_galata_tower,
            false
        ),
        MainItemModel(
            MapType.DUBAI_METRO,
            R.string.dubai_metro,
            R.drawable.ic_dubai_burj_al_arab,
            false
        ),
    )
}