package ir.roela.bametro.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.roela.bametro.R
import ir.roela.bametro.databinding.FragmentPhonesBinding
import ir.roela.bametro.databinding.NecessaryPhoneCardBinding
import org.json.JSONArray


class PhonesFragment : Fragment() {
    private lateinit var fragmentPhonesBinding: FragmentPhonesBinding

    companion object {
        @JvmStatic
        fun newInstance(): PhonesFragment {
            return PhonesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPhonesBinding = FragmentPhonesBinding.inflate(LayoutInflater.from(context))
        fragmentPhonesBinding.toolbar.apply {
            setTitle(R.string.iran_necessary_phones)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }, 100)
            }
        }

        return fragmentPhonesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lytPhones = fragmentPhonesBinding.lytPhones
        val jsonArray = loadJSONFromAsset()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val necessaryPhoneCard = NecessaryPhoneCardBinding.inflate(LayoutInflater.from(context))
            necessaryPhoneCard.txtPhoneName.text = item.getString("name")
            necessaryPhoneCard.txtPhoneTel.text = item.getString("phone")
            necessaryPhoneCard.txtPhoneTel.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:021${item.getString("phone")}")
                startActivity(intent)
            }
            lytPhones.addView(necessaryPhoneCard.root, i)
        }
    }

    private fun loadJSONFromAsset(): JSONArray {
        return try {
            val inputStream = requireContext().assets.open("necessary_phones.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))
            JSONArray(json)
        } catch (e: Exception) {
            Log.e("loadJSONFromAsset", e.message.toString())
            JSONArray()
        }
    }
}