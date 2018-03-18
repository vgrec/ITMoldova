package com.itmoldova.list

import android.widget.ImageView
import com.itmoldova.model.Category
import com.itmoldova.model.Article

/**
 * Author vgrec, on 09.07.16.
 */
interface ArticlesContract {

    interface View {
        fun showArticles(articles: List<Article>, clearDataSet: Boolean)

        fun setLoadingIndicator(loading: Boolean)

        fun showError()

        fun showNoInternetConnection()

        fun openArticleDetail(articles: List<Article>, article: Article, imageView: ImageView)

    }

    interface Presenter {
        fun loadArticles(category: Category, page: Int)

        fun refreshArticles(category: Category)

        fun cancel()

        fun onArticleClicked(articles: List<Article>, article: Article, imageView: ImageView)
    }

}
