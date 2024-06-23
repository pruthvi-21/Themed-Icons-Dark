package com.ps.tip.dark.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object Constants {
    const val REQUEST_TIME_OUT_DURATION = 10000L //in millis
}

fun ImageView.load(drawable: Drawable) {
    Glide.with(context)
        .load(drawable)
        .skipMemoryCache(true)
        .transition(DrawableTransitionOptions.withCrossFade(150))
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}

fun ImageView.load(@DrawableRes drawableRes: Int) {
    ResourcesCompat.getDrawable(resources, drawableRes, context.theme)?.let { load(it) }
}