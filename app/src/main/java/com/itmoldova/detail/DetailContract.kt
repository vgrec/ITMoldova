package com.itmoldova.detail

import com.itmoldova.model.Article

class DetailContract {

    interface View {
        fun showArticleDetail(content: String)

        fun showRelatedArticles(relatedArticles: List<Article>)

        fun showTitle(title: String)

        fun showHeaderImage(url: String)

        fun hideHeaderImage()

        fun updateFavoriteIcon(iconResId: Int)
    }

    interface Presenter {
        fun loadArticleDetail(article: Article)

        fun loadRelatedArticles(topArticles: List<Article>, article: Article)

        fun addOrRemoveFromBookmarks(isAlreadyAdded: Boolean, article: Article)

        fun setProperBookmarkIcon(article: Article)

        fun cancel()
    }
}
