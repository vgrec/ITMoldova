package com.itmoldova.list

import android.widget.ImageView
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.model.Category
import com.itmoldova.model.Article
import com.itmoldova.model.Rss
import com.itmoldova.util.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ArticlesPresenter(private val apiService: ITMoldovaService, private val view: ArticlesContract.View) : ArticlesContract.Presenter {
    private var disposable: Disposable? = null
    private var category: Category? = null

    @Inject
    lateinit var appSettings: AppSettings

    companion object {
        val TOP_ARTICLES_NUMBER = 10L
    }

    init {
        ITMoldova.appComponent.inject(this)
    }

    override fun loadArticles(category: Category, page: Int) {
        loadRssFeed(category, page, false)
    }

    override fun onArticleClicked(articles: List<Article>, article: Article, imageView: ImageView) {
        val topArticles = mutableListOf<Article>()

        Observable.fromIterable(articles)
                .take(TOP_ARTICLES_NUMBER) // we are interested only in the top 10 articles
                .subscribe({ item -> topArticles.add(item) })

        view.openArticleDetail(topArticles, article, imageView)
    }

    override fun refreshArticles(category: Category) {
        loadRssFeed(category, 0, true)
    }

    private fun loadRssFeed(category: Category, page: Int, clearDataSet: Boolean) {
        this.category = category
        disposable = getObservableByCategory(category, page)
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
            val articles = response.channel.articles
            updateLastPubDate(articles)
            view.showArticles(articles, clearDataSet)
        } else {
            view.showError()
        }
    }

    private fun updateLastPubDate(articles: List<Article>) {
        // Update last pub date only for articles that belong to HOME category
        if (category == Category.HOME) {
            val newLastPubDate = Utils.pubDateToMillis(articles[0].pubDate)
            appSettings.lastPubDate = newLastPubDate
        }
    }

    override fun cancel() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}
