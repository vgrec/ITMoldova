package com.itmoldova.list

import android.widget.ImageView
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.NetworkDetector
import com.itmoldova.model.Article
import com.itmoldova.model.Category
import com.itmoldova.model.Rss
import com.itmoldova.repository.RssFeedRepository
import com.itmoldova.util.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TOP_ARTICLES_NUMBER = 10

class ArticlesPresenter(private val view: ArticlesContract.View) : ArticlesContract.Presenter {
    private var disposable: Disposable? = null
    private var category: Category? = null

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var apiService: ITMoldovaService

    @Inject
    lateinit var networkDetector: NetworkDetector

    @Inject // currently in mock mode: com.itmoldova.di.ApplicationModule
    lateinit var rssFeedRepository: RssFeedRepository

    init {
        ITMoldova.appComponent.inject(this) // ??
    }

    override fun loadArticles(category: Category, page: Int) {
        loadRssFeed(category, page, false)
    }

    override fun onArticleClicked(articles: List<Article>, article: Article, imageView: ImageView) {
        view.openArticleDetail(articles.take(TOP_ARTICLES_NUMBER), article, imageView)
    }

    override fun refreshArticles(category: Category) {
        loadRssFeed(category, 0, true)
    }

    private fun loadRssFeed(category: Category, page: Int, clearDataSet: Boolean) {
        if (!networkDetector.hasInternetConnection()) {
            view.showNoInternetConnection()
            return
        }

        this.category = category
        disposable = getObservableByCategory(category, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setLoadingIndicator(true) }
                .doOnTerminate { view.setLoadingIndicator(false) }
                .subscribe(
                        { rss -> processResponse(rss, clearDataSet) },
                        { error ->
                            view.showError()
                        }
                )
    }

    private fun getObservableByCategory(category: Category, page: Int): Observable<Rss> {
        return if (category == Category.HOME) {
            rssFeedRepository.getDefaultRssFeed(page)
        } else {
            rssFeedRepository.getRssFeedByCategory(category.categoryName, page)
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
