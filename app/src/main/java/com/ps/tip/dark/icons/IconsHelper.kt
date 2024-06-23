package com.ps.tip.dark.icons

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.ps.tip.dark.R
import org.xmlpull.v1.XmlPullParser
import java.util.Locale

object IconsHelper {
    @Throws(Exception::class)
    fun getIconsList(context: Context, sorted: Boolean = false): List<IconInfo> {
        val parser = context.resources.getXml(R.xml.appfilter)
        var eventType = parser.eventType
        val icons: MutableList<IconInfo> = ArrayList()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.name == "item") {
                    val itemComponentInfo = parser.getAttributeValue(null, "component")
                    val itemDrawableName = parser.getAttributeValue(null, "drawable")
                    val itemLabel = parser.getAttributeValue(null, "label")

                    val componentName = ComponentName.unflattenFromString(itemComponentInfo)
                    val drawableRes = getDrawableResByName(context, itemDrawableName)

                    if (itemLabel != null) {
                        val info = IconInfo(componentName, itemDrawableName, itemLabel, drawableRes)
                        icons.add(info)
                    }
                }
            }
            eventType = parser.next()
        }
        parser.close()

        if (sorted) {
            icons.sortBy { it.label.lowercase(Locale.ROOT) }
        }

        return icons
    }

    fun getInstalledApps(context: Context): List<ApplicationInfo> {
        val pm: PackageManager = context.packageManager

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)

        val userApps = apps.filter { resolveInfo ->
            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }

        return userApps.map { resolveInfo ->
            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo
            appInfo
        }
    }

    fun getMissingAppsList(context: Context): List<ApplicationInfo> {
        val installedAppsList = getInstalledApps(context)
        val availableAppsList = getIconsList(context).map { it.packageName }

        return installedAppsList
            .filter {
                !availableAppsList.contains(it.packageName)
            }
            .sortedBy {
                context.packageManager.getApplicationLabel(it).toString().lowercase()
            }
    }

    private fun getDrawableByName(context: Context, drawableName: String?): Drawable? {
        val drawableResourceId = getDrawableResByName(context, drawableName)
        return if (drawableResourceId != 0) {
            ContextCompat.getDrawable(context, drawableResourceId)
        } else {
            null
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getDrawableResByName(context: Context, drawableName: String?): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }
}
