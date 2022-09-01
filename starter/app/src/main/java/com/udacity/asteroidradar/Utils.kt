package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

enum class AsteroidApiFilter(val value: String) {
    SHOW_TODAY("Today"), SHOW_WEEK("Week"), SHOW_SAVED("Saved")
}
class Utils {
    companion object {

        private fun getDay(time: Long): String =
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
                time
            )

        fun getTodayDate(): String {
            val currentTime = System.currentTimeMillis()
            return getDay(currentTime)
        }

        fun getDaysTo(days: Long): String {
            val diffDaysTo = TimeUnit.DAYS.toMillis(days)
            val time = System.currentTimeMillis() + diffDaysTo
            return getDay(time)
        }
    }
}



