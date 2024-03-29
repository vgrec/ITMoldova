package com.itmoldova.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itmoldova.R
import com.itmoldova.model.Article
import com.itmoldova.util.UiUtils
import com.itmoldova.util.Utils
import com.squareup.picasso.Picasso

class ArticlesAdapter(private val context: Context,
                      private val articles: List<Article>,
                      private val itemClickListener: (article: Article, imageView: ImageView) -> Unit) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    private var showingBookmarks: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        val itemLayout = if (viewType == VIEW_TYPE_HEADER) R.layout.item_list_header else R.layout.item_list_normal
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.titleView.text = article.title
        holder.dateView.text = Utils.formatPubDate(article.pubDate)
        Picasso.with(context).load(UiUtils.extractFirstImage(article.content)).into(holder.previewImage)
    }

    override fun getItemCount(): Int = articles.size

    override fun getItemViewType(position: Int): Int =
            when {
                showingBookmarks -> VIEW_TYPE_NORMAL
                position == 0 -> VIEW_TYPE_HEADER
                else -> VIEW_TYPE_NORMAL
            }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var titleView: TextView = view.findViewById(R.id.title)
        var dateView: TextView = view.findViewById(R.id.date)
        var previewImage: ImageView = view.findViewById(R.id.preview_image)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.row) {
                itemClickListener(articles[adapterPosition], previewImage)
            }
        }
    }

    companion object {
        private val VIEW_TYPE_HEADER = 0
        private val VIEW_TYPE_NORMAL = 1
    }

    fun setShowingBookmarks(showingBookmarks: Boolean) {
        this.showingBookmarks = showingBookmarks
    }
}
