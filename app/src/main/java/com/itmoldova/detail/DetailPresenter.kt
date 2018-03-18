package com.itmoldova.detail

import com.itmoldova.R
import com.itmoldova.db.ItemDao
import com.itmoldova.model.Item
import com.itmoldova.util.HtmlParser
import com.itmoldova.util.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Author vgrec, on 24.07.16.
 */
class DetailPresenter(private val view: DetailContract.View,
                      private val itemDao: ItemDao,
                      private val htmlParser: HtmlParser) : DetailContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadArticleDetail(item: Item) {
        view.showTitle(item.title)

        val content = htmlParser.parse(item.content)
        view.showArticleDetail(content)

        val imageUrl = htmlParser.getHeaderImageUrl()
        if (imageUrl != null) {
            view.showHeaderImage(imageUrl)
        } else {
            view.hideHeaderImage()
        }
    }

    override fun loadRelatedArticles(items: List<Item>, item: Item) {
        val relatedArticles = Utils.getRelatedArticles(items, item, 5)
        view.showRelatedArticles(relatedArticles)
    }

    override fun addOrRemoveFromBookmarks(isAlreadyAdded: Boolean, item: Item) {
        if (isAlreadyAdded) {
            disposables.add(Completable.fromAction { itemDao.deleteItem(item) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.updateStarIcon(R.drawable.ic_star_outline) }))
        } else {
            disposables.add(Completable.fromAction { itemDao.insertItem(item) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.updateStarIcon(R.drawable.ic_star_full) }))
        }
    }

    override fun setProperBookmarkIcon(item: Item) {
        disposables.add(itemDao.getItemById(item.guid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> view.updateStarIcon(R.drawable.ic_star_full) },
                        { _ -> view.updateStarIcon(R.drawable.ic_star_outline) }))
    }

    override fun cancel() {
        disposables.clear()
    }

}
