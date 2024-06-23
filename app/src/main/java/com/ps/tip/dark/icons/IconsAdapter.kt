package com.ps.tip.dark.icons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ps.tip.dark.R
import com.ps.tip.dark.utils.load

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
        holder.iconView.load(icon.drawableResId)
    }

    private fun buildDialog(context: Context, iconInfo: IconInfo): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_icon_info, null)

        val iconView: ImageView = dialogView.findViewById(R.id.icon)
        val iconLabel: TextView = dialogView.findViewById(R.id.icon_label)
        val iconDrawableName: TextView = dialogView.findViewById(R.id.icon_drawable_name)
        val iconPackageName: TextView = dialogView.findViewById(R.id.icon_package_name)

        iconView.load(iconInfo.drawableResId)
        iconLabel.text = iconInfo.label
        iconDrawableName.text = iconInfo.drawableName
        iconPackageName.text = iconInfo.packageName

        return MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok, null)
            .create()
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