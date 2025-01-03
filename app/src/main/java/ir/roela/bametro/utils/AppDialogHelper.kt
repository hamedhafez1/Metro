package ir.roela.bametro.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import ir.roela.bametro.BuildConfig
import ir.roela.bametro.R
import ir.roela.bametro.databinding.AboutUsBinding
import ir.roela.bametro.databinding.AboutUsMyketBinding

class AppDialogHelper(context: Context) : AlertDialog.Builder(context) {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    fun showHelpRequireInternet(isOffline: Boolean): AppDialogHelper {
        try {
            val message = if (isOffline) {
                R.string.this_map_not_require_internet
            } else {
                R.string.this_map_require_internet_for_first_time
            }
            setTitle(R.string.help)
            setMessage(message)
//            setNegativeButton(
//                R.string.close
//            ) { dialog, _ -> dialog.dismiss() }
        } catch (e: Exception) {
            Log.e(TAG, "showHelpDialog: ${e.message}")
        }
        return this
    }

    private fun showAboutUsDialog(view: View) {
        try {
            setTitle(
                context.getString(
                    R.string.version,
                    context.getString(R.string.thisAppVersion),
                    BuildConfig.VERSION_NAME
                )
            )
            setView(view)
            setNegativeButton(R.string.close) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        } catch (e: Exception) {
            Log.e(TAG, "showAboutUsDialog: ${e.message}", e)
        }
    }

    fun showAboutUsCafeBazaar() {
        val aboutUsViewBinding = AboutUsBinding.inflate(LayoutInflater.from(context))
        aboutUsViewBinding.btnOpenBazaarPage.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_EDIT)
                intent.data = Uri.parse("bazaar://details?id=" + BuildConfig.APPLICATION_ID)
                intent.setPackage("com.farsitel.bazaar")
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        aboutUsViewBinding.imgBazaarQrcode.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("bazaar://details?id=" + BuildConfig.APPLICATION_ID)
                intent.setPackage("com.farsitel.bazaar")
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        aboutUsViewBinding.btnOpenBazaarMyAppsPage.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("bazaar://collection?slug=by_author&aid=roela_apps")
                intent.setPackage("com.farsitel.bazaar")
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        showAboutUsDialog(aboutUsViewBinding.root)
    }

    fun showAboutUsMyket() {
        val aboutUsViewBinding = AboutUsMyketBinding.inflate(LayoutInflater.from(context))
        aboutUsViewBinding.btnOpenCommentPage.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("myket://comment?id=" + BuildConfig.APPLICATION_ID)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        aboutUsViewBinding.imgQrcode.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("myket://details?id=" + BuildConfig.APPLICATION_ID)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        aboutUsViewBinding.btnOpenMyAppsPage.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("myket://developer/" + BuildConfig.APPLICATION_ID)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("bametro", e.message.toString())
            }
        }
        showAboutUsDialog(aboutUsViewBinding.root)
    }

}