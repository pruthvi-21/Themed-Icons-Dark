package com.ps.mui3.tip.icons

import android.content.ComponentName

class IconInfo(
    private val componentName: ComponentName?,
    val drawableName: String,
    private val iconLabel: String?,
    val drawableResId: Int
) {
    val packageName: String
        get() {
            val name = componentName?.packageName ?: ""
            return name.run {
                if (startsWith(componentInfoLabel)) substring(componentInfoLabel.length) else this
            }
        }

    val label: String
        get() {
            return iconLabel ?: drawableName
        }

    private val componentInfoLabel = "ComponentInfo{"
}
