package com.ps.tip.dark.icons

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ps.tip.dark.R
import com.ps.tip.dark.utils.load

class RequestIconsListAdapter(
    context: Context,
    appsList: List<ApplicationInfo>
) : RecyclerView.Adapter<RequestIconsListAdapter.RVViewHolder>() {

    private var pm = context.packageManager
    private var layoutInflater = LayoutInflater.from(context)

    val appsList = appsList.map { RequestIconItem(it, false) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        return RVViewHolder(layoutInflater.inflate(R.layout.request_icon_list_item, parent, false))
    }

    override fun getItemCount() = appsList.size

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        val item = appsList[position]

        holder.itemView.setOnClickListener {
            holder.iconCheckbox.performClick()
        }

        holder.iconCheckbox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
        }

        holder.iconView.load(pm.getApplicationIcon(item.appInfo))
        holder.iconCheckbox.text = pm.getApplicationLabel(item.appInfo)
        holder.iconCheckbox.isChecked = item.isChecked
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleAllIconsCheckedState() {
        val allChecked = appsList.all { it.isChecked }
        val noneChecked = appsList.none { it.isChecked }

        val newState = !allChecked && !noneChecked
        appsList.forEach { it.isChecked = newState || noneChecked }
        notifyDataSetChanged()
    }

    class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconView: ImageView = itemView.findViewById(R.id.icon)
        val iconCheckbox: AppCompatCheckBox = itemView.findViewById(R.id.icon_checkbox)
    }
}

data class RequestIconItem(
    val appInfo: ApplicationInfo,
    var isChecked: Boolean = false
)
