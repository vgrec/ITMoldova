package com.itmoldova.sync

import com.itmoldova.AppSettings
import com.itmoldova.notifications.NotificationsController
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.NetworkDetector
import com.itmoldova.model.Rss
import com.itmoldova.util.Utils
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Executes an http request against "http://itmoldova.com/feed/?paged=1" and in case
 * new articles were published since the last sync, fires a notifications.
 */
class RssChecker @Inject
constructor(private val notificationsController: NotificationsController,
            private val appSettings: AppSettings,
            private val service: ITMoldovaService,
            private val networkDetector: NetworkDetector) {

    private var disposable: Disposable? = null

    fun start(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler) {
        if (!networkDetector.hasInternetConnection()) {
            return
        }
        disposable = service
                .getDefaultRssFeed(PAGE_NUMBER)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                        { rss -> onSuccess(rss) },
                        { error -> onError(error) })
    }

    private fun onSuccess(response: Rss?) {
        if (response == null || response.channel == null) {
            return
        }

        val articles = response.channel.articles
        if (notificationsController.shouldShowNotification(articles)) {
            notificationsController.showNotification(articles)
            val newLastPubDate = Utils.pubDateToMillis(articles[0].pubDate)
            appSettings.lastPubDate = newLastPubDate
        }
    }

    private fun onError(error: Throwable) {
        // Ignore errors during sync
    }

    fun cancel() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    companion object {

        // Always query the first page, this will give the latest articles.
        private val PAGE_NUMBER = 1
    }
}
