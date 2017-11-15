package com.itmoldova.sync

import com.itmoldova.AppSettings
import com.itmoldova.controller.NotificationController
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.NetworkDetector
import com.itmoldova.model.Rss
import com.itmoldova.util.Utils
import rx.Scheduler
import rx.Subscription
import javax.inject.Inject

/**
 * Executes an http request against "http://itmoldova.com/feed/?paged=1" and in case
 * new articles were published since the last sync, fires a notifications.
 */
class RssChecker @Inject
constructor(private val notificationController: NotificationController,
            private val appSettings: AppSettings,
            private val service: ITMoldovaService,
            private val networkDetector: NetworkDetector) {

    private var subscription: Subscription? = null

    fun start(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler) {
        if (!networkDetector.hasInternetConnection()) {
            return
        }
        subscription = service
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

        val items = response.channel.itemList
        if (notificationController.shouldShowNotification(items)) {
            notificationController.showNotification(items)
            val newLastPubDate = Utils.pubDateToMillis(items[0].pubDate)
            appSettings.lastPubDate = newLastPubDate
        }
    }

    private fun onError(error: Throwable) {
        // Ignore errors during sync
    }

    fun cancel() {
        subscription?.let {
            if (it.isUnsubscribed) {
                it.unsubscribe()
            }
        }
    }

    companion object {

        // Always query the first page, this will give the latest articles.
        private val PAGE_NUMBER = 1
    }
}
