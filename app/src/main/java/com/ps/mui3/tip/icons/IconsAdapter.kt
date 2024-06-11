package com.ps.mui3.tip.icons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ps.mui3.tip.R

class IconsAdapter(
    private val context: Context,
    private val icons: List<IconInfo>
) : RecyclerView.Adapter<IconsAdapter.RVViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)
    private var filteredIcons: MutableList<IconInfo> = icons.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        return RVViewHolder(layoutInflater.inflate(R.layout.tile, parent, false))
    }

    override fun getItemCount() = filteredIcons.size

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        val icon = filteredIcons[position]

        holder.itemView.setOnClickListener {
            buildDialog(context, icon).show()
        }

        holder.iconLabel.text = icon.label
        loadIcon(holder.iconView, icon.drawableResId)
    }

    private fun buildDialog(context: Context, iconInfo: IconInfo): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_icon_info, null)

        val iconView: ImageView = dialogView.findViewById(R.id.icon)
        val iconLabel: TextView = dialogView.findViewById(R.id.icon_label)
        val iconDrawableName: TextView = dialogView.findViewById(R.id.icon_drawable_name)
        val iconPackageName: TextView = dialogView.findViewById(R.id.icon_package_name)

        loadIcon(iconView, iconInfo.drawableResId)
        iconLabel.text = iconInfo.label
        iconDrawableName.text = iconInfo.drawableName
        iconPackageName.text = iconInfo.packageName

        return MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    private fun loadIcon(imageView: ImageView, drawableRes: Int) {
        Glide.with(context)
            .load(drawableRes)
            .skipMemoryCache(true)
            .transition(DrawableTransitionOptions.withCrossFade(150))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    fun updateList(filter: String): Int {
        filteredIcons.clear()

        if (filter.isNotEmpty()) {
            icons.forEach {
                if (it.label.contains(filter.trim(), true))
                    filteredIcons.add(it)
                else if (it.drawableName.contains(filter.trim(), true))
                    filteredIcons.add(it)
            }
        }

        notifyDataSetChanged()
        return filteredIcons.size
    }

    class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconView: ImageView = itemView.findViewById(R.id.tile_icon)
        val iconLabel: TextView = itemView.findViewById(R.id.tile_label)
    }

}