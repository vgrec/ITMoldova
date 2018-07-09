package com.itmoldova.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.itmoldova.R
import com.itmoldova.model.Article
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup

class UiUtils {

    companion object {
        fun createRelatedViews(context: Context, relatedArticles: List<Article>, darkModeEnabled: Boolean, clickListener: (Article) -> Unit): ViewGroup {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = params
            linearLayout.orientation = LinearLayout.VERTICAL
            val drawable = context.getDrawable(R.drawable.list_divider)
            drawable.setTint(getDividerColor(context, darkModeEnabled))
            linearLayout.dividerDrawable = drawable
            linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE


            for (article in relatedArticles) {
                val view = LayoutInflater.from(context).inflate(R.layout.item_related, null, false)
                view.layoutParams = params
                (view.findViewById<View>(R.id.title) as TextView).text = article.title
                (view.findViewById<View>(R.id.date) as TextView).text = Utils.formatPubDate(article.pubDate)

                Picasso.with(context)
                        .load(extractFirstImage(article.content))
                        .into(view.findViewById<View>(R.id.image) as ImageView)
                view.setOnClickListener { v -> clickListener(article) }
                linearLayout.addView(view)
            }

            return linearLayout
        }

        fun extractFirstImage(content: String): String? {
            val document = Jsoup.parse(content)
            val images = document.select("img[src]")
            return images?.first()?.absUrl("src")?.replace("http://", "https://")
        }

        fun extractPhotoUrlsFromArticle(content: String): List<String> {
            val document = Jsoup.parse(content)
            val images = document.select("img[src]")
            return images.map { it.absUrl("src").replace("http://", "https://") }
        }

        fun getColorResFromAttribute(context: Context, attribute: Int): Int {
            val attrs: IntArray = intArrayOf(attribute)
            val typedArray = context.obtainStyledAttributes(attrs)
            val color = typedArray.getResourceId(0, android.R.color.darker_gray)
            typedArray.recycle()
            return color
        }

        fun getDividerColor(context: Context, darkModeEnabled: Boolean): Int {
            return if (darkModeEnabled) context.resources.getColor(R.color.list_divider_dark)
            else context.resources.getColor(R.color.list_divider_light)
        }
    }
}