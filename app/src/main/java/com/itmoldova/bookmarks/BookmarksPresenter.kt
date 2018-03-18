package com.itmoldova.bookmarks

import com.itmoldova.db.ArticleDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookmarksPresenter(private val view: BookmarksContract.View,
                         private val articleDao: ArticleDao) : BookmarksContract.Presenter {

    private var disposable: Disposable? = null

    override fun loadBookmarks() {
        disposable = articleDao.loadAllArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> view.showBookmarks(result) })
    }

    fun cancel() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}