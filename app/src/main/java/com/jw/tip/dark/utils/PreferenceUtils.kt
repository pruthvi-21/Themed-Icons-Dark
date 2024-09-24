package com.jw.tip.dark.utils

import android.content.SharedPreferences
import com.jw.tip.dark.BuildConfig
import java.util.Calendar

class PreferenceUtils(
    private val preferences: SharedPreferences
) {
    companion object {
        private const val LAST_REQUEST_DATE_KEY = "last_request_date"
        private const val REQUEST_COUNT_KEY = "request_count"
        private const val DEFAULT_REQUEST_COUNT = 0
    }

    fun canMakeRequest(userId: String, onComplete: (Boolean) -> Unit) {
        val lastRequestDate = preferences.getLong(LAST_REQUEST_DATE_KEY, 0L)
        val requestCount = preferences.getInt(REQUEST_COUNT_KEY, DEFAULT_REQUEST_COUNT)

        val calendar = Calendar.getInstance().apply { timeInMillis = lastRequestDate }
        val currentDate = Calendar.getInstance()
        val isSameDay = currentDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                currentDate.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

        if (!isSameDay) {
            // If it's a new day, reset request count
            preferences.edit().remove(REQUEST_COUNT_KEY).apply()
            onComplete(true)
        } else {
            // Check the request count against the limit
            if (requestCount < BuildConfig.REQUEST_LIMIT) {
                // Proceed to check Firebase
//                FirebaseDatabaseHelper.hasSentRequestToday(userId) { hasRequestedToday ->
//                    onComplete(!hasRequestedToday)
//                }
                onComplete(true)
            } else {
                onComplete(false) // Exceeded request limit
            }
        }
    }

    fun updateRequestData() {
        val currentDate = System.currentTimeMillis()
        preferences.edit()
            .putLong(LAST_REQUEST_DATE_KEY, currentDate)
            .putInt(
                REQUEST_COUNT_KEY,
                preferences.getInt(REQUEST_COUNT_KEY, DEFAULT_REQUEST_COUNT) + 1
            )
            .apply()
    }
}