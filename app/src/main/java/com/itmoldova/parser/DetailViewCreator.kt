package com.itmoldova.parser

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.itmoldova.R
import com.itmoldova.model.Item
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Given as input a [], creates the associated views for
 * each [Block] and returns as result a [<] that is
 * to be rendered in the UI.
 *
 * Author vgrec, on 14.08.16.
 */
class DetailViewCreator(private val context: Context) {

    fun createViewsFrom(blocks: List<Block>): List<View> {
        val views = ArrayList<View>()
        for (block in blocks) {
            when (block.type) {
                Block.Type.TEXT -> views.add(createTextView(block.content))
                Block.Type.IMAGE -> views.add(createImageView(block.content))
            }
        }

        return views
    }

    private fun createTextView(text: String): View {
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.text = Html.fromHtml(text)
        textView.textSize = 16f
        textView.setLineSpacing(8f, 1f)
        textView.setTextColor(context.resources.getColor(R.color.item_title))
        textView.movementMethod = LinkMovementMethod.getInstance()
        return textView
    }

    private fun createImageView(url: String): View {
        val imageView = ImageView(context)
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER_HORIZONTAL
        params.setMargins(0, context.resources.getDimension(R.dimen.image_margin_top).toInt(), 0,
                context.resources.getDimension(R.dimen.image_margin_bottom).toInt())
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.adjustViewBounds = true
        imageView.tag = url
        Picasso.with(context).load(url).into(imageView)
        return imageView
    }

    private fun createVideoView(url: String): View? {
        return null
    }

    fun createRelatedViews(relatedArticles: List<Item>, clickListener: (Item) -> Unit): ViewGroup {
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
                    .load(ContentParser.extractFirstImage(item.content))
                    .into(view.findViewById<View>(R.id.image) as ImageView)
            view.setOnClickListener { v -> clickListener(item) }
            linearLayout.addView(view)
        }

        return linearLayout
    }

}
