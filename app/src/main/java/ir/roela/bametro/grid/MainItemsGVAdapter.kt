package ir.roela.bametro.grid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ir.roela.bametro.R

class MainItemsGVAdapter(context: Context, objects: List<MainItemModel?>) :
    ArrayAdapter<MainItemModel?>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView =
                LayoutInflater.from(context).inflate(R.layout.menu_card_item, parent, false)
        }
        val mainItemModel = getItem(position)
        val itemText = listItemView!!.findViewById<TextView>(R.id.txtItemTitle)
        itemText.text = context.getString(mainItemModel!!.itemName)
        itemText.setCompoundDrawablesWithIntrinsicBounds(0, mainItemModel.imageId, 0, 0)
        return listItemView
    }
}