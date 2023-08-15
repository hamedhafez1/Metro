package ir.roela.bametro

import android.content.Context
import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    companion object {
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }
    }
}