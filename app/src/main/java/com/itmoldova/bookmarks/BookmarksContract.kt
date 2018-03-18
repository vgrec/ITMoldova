package com.itmoldova.bookmarks

import com.itmoldova.model.Article

class BookmarksContract {

    interface View {
        fun showBookmarks(articles: List<Article>)
    }

    interface Presenter {
        fun loadBookmarks()
    }

}