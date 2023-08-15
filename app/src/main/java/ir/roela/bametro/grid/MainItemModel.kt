package ir.roela.bametro.grid

import ir.roela.bametro.utils.MapType

class MainItemModel(
    val mapType: MapType,
    var itemName: Int,
    var imageId: Int,
    var isOffline: Boolean = true
)