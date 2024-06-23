package com.ps.tip.dark.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object DeviceUtils {

    @SuppressLint("HardwareIds")
    fun getUserId(context: Context): String {
        val userId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return hashString(userId)
    }

    /*private fun getUserId(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var userID = prefs.getString(IconsRequestBuilder.USER_ID, null)

        if (userID == null) {
            userID = UUID.randomUUID().toString()
            prefs.edit().putString(IconsRequestBuilder.USER_ID, userID).apply()
        }

        return userID
    }*/

    private fun convertToHex(data: ByteArray): String {
        return data.joinToString("") {
            "%02x".format(it)
        }
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun hashString(text: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val textBytes = text.toByteArray(charset("iso-8859-1"))
        val sha1hash = md.digest(textBytes)
        return convertToHex(sha1hash)
    }
}