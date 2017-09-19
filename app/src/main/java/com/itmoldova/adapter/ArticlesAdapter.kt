package com.itmoldova.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.itmoldova.R
import com.itmoldova.list.ItemClickListener
import com.itmoldova.model.Item
import com.itmoldova.parser.ContentParser
import com.itmoldova.util.Utils
import com.squareup.picasso.Picasso

class ArticlesAdapter(private val context: Context,
                      private val items: List<Item>,
                      private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        val itemLayout = if (viewType == VIEW_TYPE_HEADER) R.layout.item_list_header else R.layout.item_list_normal
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleView.text = item.title
        holder.dateView.text = Utils.formatPubDate(item.pubDate)
        Picasso.with(context).load(ContentParser.extractFirstImage(item.content)).into(holder.previewImage)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
            if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_NORMAL

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var titleView: TextView = view.findViewById(R.id.title)
        var dateView: TextView = view.findViewById(R.id.date)
        var previewImage: ImageView = view.findViewById(R.id.preview_image)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.row) {
                itemClickListener.onItemClicked(items[adapterPosition])
            }
        }
    }

    companion object {
        private val VIEW_TYPE_HEADER = 0
        private val VIEW_TYPE_NORMAL = 1
    }
}
