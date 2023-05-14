package ir.roela.bametro.utils

import android.os.Environment
import android.provider.ContactsContract
import ir.roela.bametro.BuildConfig

class Constants {

    companion object {
        @JvmStatic
        private val DIR_ROOT = (Environment.getExternalStorageDirectory().toString()
                + "/Android/data/" + BuildConfig.APPLICATION_ID + "/")
        @JvmField
        val DIR_CACHE = DIR_ROOT + "cache" + "/"

        @JvmStatic
        fun getFileExtension(fileName: String): String {
            var extension = ""
            try {
                val i = fileName.lastIndexOf('.')
                val p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'))
                if (i > p) {
                    extension = fileName.substring(i + 1)
                }
            } catch (ex: Exception) {
            }
            return extension
        }

    }
}
