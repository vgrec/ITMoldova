package com.itmoldova.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.itmoldova.R
import com.itmoldova.model.Item
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup

class UiUtils {

    companion object {
        fun createRelatedViews(context: Context, relatedArticles: List<Item>, clickListener: (Item) -> Unit): ViewGroup {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, context.resources.getDimensionPixelSize(R.dimen.related_item_margin), 0, 0)
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = params
            linearLayout.orientation = LinearLayout.VERTICAL


            for (item in relatedArticles) {
                val view = LayoutInflater.from(context).inflate(R.layout.item_related, null, false)
                view.layoutParams = params
                (view.findViewById<View>(R.id.title) as TextView).text = item.title
                (view.findViewById<View>(R.id.date) as TextView).text = item.pubDate

                Picasso.with(context)
                        .load(extractFirstImage(item.content))
                        .into(view.findViewById<View>(R.id.image) as ImageView)
                view.setOnClickListener { v -> clickListener(item) }
                linearLayout.addView(view)
            }

            return linearLayout
        }

        fun extractFirstImage(content: String): String? {
            val document = Jsoup.parse(content)
            val images = document.select("img[src]")
            return images?.first()?.absUrl("src")
        }

        fun extractPhotoUrlsFromArticle(content: String): List<String> {
            val document = Jsoup.parse(content)
            val images = document.select("img[src]")
            return images.map { it.absUrl("src") }
        }
    }
}