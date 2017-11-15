package com.itmoldova.list

import android.widget.ImageView
import com.itmoldova.model.Category
import com.itmoldova.model.Item

/**
 * Author vgrec, on 09.07.16.
 */
interface ArticlesContract {

    interface View {
        fun showArticles(items: List<Item>, clearDataSet: Boolean)

        fun setLoadingIndicator(loading: Boolean)

        fun showError()

        fun showNoInternetConnection()

        fun openArticleDetail(items: List<Item>, item: Item, imageView: ImageView)

    }

    interface Presenter {
        fun loadArticles(category: Category, page: Int)

        fun refreshArticles(category: Category)

        fun cancel()

        fun onArticleClicked(items: List<Item>, item: Item, imageView: ImageView)
    }

}
