package com.ps.tip.dark.icons

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
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

        if(sorted) {
            icons.sortBy { it.label.lowercase(Locale.ROOT) }
        }

        return icons
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
