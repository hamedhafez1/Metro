package ir.roela.bametro.grid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ir.roela.bametro.databinding.MenuCardItemBinding

class MainItemsGVAdapter(context: Context, mainItemModels: List<MainItemModel?>) :
    ArrayAdapter<MainItemModel?>(context, 0, mainItemModels) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: MenuCardItemBinding = if (convertView != null) {
            MenuCardItemBinding.bind(convertView)
        } else {
            MenuCardItemBinding.inflate(LayoutInflater.from(context), parent, false)
        }

        val mainItemModel = getItem(position)

        val itemText = binding.txtItemTitle
        itemText.text = context.getString(mainItemModel!!.itemName)
        itemText.setCompoundDrawablesWithIntrinsicBounds(0, mainItemModel.imageId, 0, 0)
        return binding.root
    }
}