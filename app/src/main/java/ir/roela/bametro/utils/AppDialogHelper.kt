package ir.roela.bametro.utils

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import ir.roela.bametro.BuildConfig
import ir.roela.bametro.R

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

    fun showAboutUsDialog(view: View) {
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

}