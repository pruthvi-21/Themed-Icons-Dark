package com.jw.tip.dark.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import com.jw.tip.dark.models.FirebaseRequestData
import com.jw.tip.dark.models.FirebaseRequestItemData

object IconsRequestBuilder {

    fun build(context: Context, list: List<ApplicationInfo>): FirebaseRequestData {
        val packageManager = context.packageManager
        val requestItems = ArrayList<FirebaseRequestItemData>()

        list.forEach {
            val requestItem = FirebaseRequestItemData(
                packageManager.getApplicationLabel(it).toString(),
                "${it.packageName}/${it.className}"
            )
            requestItems.add(requestItem)
        }

        return FirebaseRequestData(
            userId = DeviceUtils.getUserId(context),
            data = requestItems,
            timestamp = System.currentTimeMillis()
        )
    }
}