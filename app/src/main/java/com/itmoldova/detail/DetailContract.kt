package com.itmoldova.detail

import com.itmoldova.model.Item

/**
 * Author vgrec, on 24.07.16.
 */
class DetailContract {

    interface View {
        fun showArticleDetail(views: List<android.view.View>)

        fun showArticleDetail(content: String)

        fun showRelatedArticles(relatedItems: List<Item>)

        fun showTitle(title: String)

        fun showHeaderImage(url: String)

        fun hideHeaderImage()

        fun updateStarIcon(iconResId: Int)
    }

    interface Presenter {
        fun loadArticleDetail(item: Item)

        fun loadRelatedArticles(items: List<Item>, item: Item)

        fun addRemoveFromBookmarks(item: Item)

        fun isItemBookmarked(item: Item): Boolean
    }
}
