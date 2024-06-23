package com.ps.tip.dark.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri

object EmailUtils {
    private val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
    }

    fun getEmailProviders(context: Context): List<ResolveInfo> {
        return context.packageManager.queryIntentActivities(
            emailIntent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
    }

    fun openEmailApp(context: Context, chosenProvider: ResolveInfo) {
        val emailIntent = this.emailIntent.setPackage(chosenProvider.activityInfo.packageName)
        context.startActivity(emailIntent)
    }
}
