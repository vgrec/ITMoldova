package com.itmoldova.bookmarks

import com.itmoldova.model.Item

class BookmarksContract {

    interface View {
        fun showBookmarks(items: List<Item>)
    }

    interface Presenter {
        fun loadBookmarks()
    }

}