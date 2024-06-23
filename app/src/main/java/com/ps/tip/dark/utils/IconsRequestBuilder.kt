package com.ps.tip.dark.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import org.json.JSONArray
import org.json.JSONObject

object IconsRequestBuilder {
    private const val ITEM_LABEL = "item_label"
    private const val ITEM_PACKAGE_NAME = "item_package_name"
    private const val USER_ID = "user_id"
    private const val DATA = "data"
    private const val TIMESTAMP = "timestamp"

    fun build(context: Context, list: List<ApplicationInfo>): JSONObject {
        val pm = context.packageManager
        val jsonArray = JSONArray()

        list.forEach {
            val dataObject = JSONObject()
                .put(ITEM_LABEL, pm.getApplicationLabel(it))
                .put(ITEM_PACKAGE_NAME, "${it.packageName}/${it.className}")

            jsonArray.put(dataObject)
        }

        return wrapWithMetadata(context, jsonArray)
    }

    private fun wrapWithMetadata(context: Context, data: JSONArray): JSONObject {
        val userId = DeviceUtils.getUserId(context)
        val currentTime = System.currentTimeMillis()

        return JSONObject()
            .put(USER_ID, userId)
            .put(DATA, data)
            .put(TIMESTAMP, currentTime)
    }
}