package com.itmoldova.bookmarks

import com.itmoldova.db.ItemDao


class BookmarksPresenter(private val view: BookmarksContract.View,
                         private val itemDao: ItemDao) : BookmarksContract.Presenter {
    override fun loadBookmarks() {
        view.showBookmarks(itemDao.loadAllItems())
    }
}