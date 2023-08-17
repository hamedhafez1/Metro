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
import ir.roela.bametro.databinding.AboutUsMyketBinding

class AppDialogHelper(context: Context) : AlertDialog.Builder(context) {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    fun showHelpRequireInternet(isOffline: Boolean): AppDialogHelper {
        try {
            val message = if (isOffline) {
                "نمایش این نقشه به اینترنت نیاز ندارد."
            } else {
                "نمایش این نقشه به اینترنت نیاز دارد."
            }
            setTitle(R.string.help)
            setMessage(message)
            setNegativeButton(
                R.string.close
            ) { dialog, _ -> dialog.dismiss() }
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

    fun showAboutUs() {
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