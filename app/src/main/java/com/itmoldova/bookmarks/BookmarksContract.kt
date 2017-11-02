package com.itmoldova.bookmarks

class BookmarksContract {

    interface View {
        fun showBookmarks()
    }

    interface Presenter {
        fun loadBookmarks()
    }

}