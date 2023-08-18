package ir.roela.bametro.utils

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class WelcomeText {

    fun getWelcome(): String {
        val hour =
            Calendar.getInstance(TimeZone.getTimeZone("GMT+3:30"), Locale.US)
                .get(Calendar.HOUR_OF_DAY)
        val welcomeText: String
        when (hour) {
            in 0..5 -> {
                /*بامداد*/
                welcomeText = "بامداد نکو"
            }

            in 6..11 -> {
                /*صبح*/
                welcomeText = "صبحتون بخیر"
            }

            in 12..13 -> {
                /*ظهر*/
                welcomeText = "ظهرتون بخیر"
            }

            in 14..16 -> {
                /*بعذ از ظهر*/
                welcomeText = "بعد از ظهرتون بخیر"
            }

            in 17..18 -> {
                /*عصر*/
                welcomeText = "عصرتون بخیر"
            }

            in 19..22 -> {
                /*شب*/
                welcomeText = "شب تون بخیر"
            }

            else -> {
                /*وقت بخیر*/
                welcomeText = "وقتتون بخیر"
            }
        }
        return welcomeText
    }
}