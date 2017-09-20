package com.itmoldova.list

import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.model.Category
import com.itmoldova.model.Item
import com.itmoldova.model.Rss
import com.itmoldova.util.Utils
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ArticlesPresenter(private val apiService: ITMoldovaService, private val view: ArticlesContract.View) : ArticlesContract.Presenter {
    private var subscription: Subscription? = null
    private var category: Category? = null

    @Inject
    lateinit var appSettings: AppSettings

    init {
        ITMoldova.appComponent.inject(this)
    }

    override fun loadArticles(category: Category, page: Int) {
        loadRssFeed(category, page, false)
    }

    override fun onArticleClicked(items: List<Item>, item: Item) {
        val selected = Observable.from(items)
                .take(10) // we are interested only in the top 10 articles
                .toList()
                .toBlocking()
                .single()
        view.openArticleDetail(selected, item)
    }

    override fun refreshArticles(category: Category) {
        loadRssFeed(category, 0, true)
    }

    private fun loadRssFeed(category: Category, page: Int, clearDataSet: Boolean) {
        this.category = category
        subscription = getObservableByCategory(category, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setLoadingIndicator(true) }
                .doOnTerminate { view.setLoadingIndicator(false) }
                .subscribe(
                        { rss -> processResponse(rss, clearDataSet) },
                        { error -> view.showError() }
                )
    }

    private fun getObservableByCategory(category: Category, page: Int): Observable<Rss> {
        return if (category == Category.HOME) {
            apiService.getDefaultRssFeed(page)
        } else {
            apiService.getRssFeedByCategory(category.categoryName, page)
        }
    }

    private fun processResponse(response: Rss?, clearDataSet: Boolean) {
        if (response != null && response.channel != null) {
            val items = response.channel.itemList
            updateLastPubDate(items)
            view.showArticles(items, clearDataSet)
        } else {
            view.showError()
        }
    }

    private fun updateLastPubDate(items: List<Item>) {
        // Update last pub date only for articles that belong to HOME category
        if (category == Category.HOME) {
            val newLastPubDate = Utils.pubDateToMillis(items[0].pubDate)
            appSettings.lastPubDate = newLastPubDate
        }
    }

    override fun cancel() {
        if (subscription?.isUnsubscribed == true) {
            subscription?.unsubscribe()
        }
    }
}
