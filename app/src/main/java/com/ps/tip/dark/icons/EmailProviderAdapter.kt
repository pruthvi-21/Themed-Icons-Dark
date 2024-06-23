package com.ps.tip.dark.icons

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ps.tip.dark.R

class EmailProviderAdapter(
    private val context: Context,
    private val emailProviders: List<ResolveInfo>
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = emailProviders.size

    override fun getItem(position: Int): Any = emailProviders[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_email_provider, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.email_provider_icon),
                view.findViewById(R.id.email_provider_name)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val emailProvider = emailProviders[position]
        viewHolder.name.text = emailProvider.loadLabel(context.packageManager)
        viewHolder.icon.setImageDrawable(emailProvider.loadIcon(context.packageManager))

        return view
    }

    private data class ViewHolder(val icon: ImageView, val name: TextView)
}
