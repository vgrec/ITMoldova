package com.itmoldova.sync.v14

import android.app.IntentService
import android.content.Intent

import com.itmoldova.ITMoldova
import com.itmoldova.sync.SyncRunner

import javax.inject.Inject

import rx.schedulers.Schedulers

/**
 * Implementation of [IntentService] used on Pre-Lollipop devices
 * to retrieve the latest rss feed.
 */
class SyncArticlesServiceV14 : IntentService("SyncArticlesServiceV14") {

    @Inject
    lateinit var syncRunner: SyncRunner

    override fun onCreate() {
        super.onCreate()
        ITMoldova.appComponent.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        syncRunner.start(Schedulers.immediate(), Schedulers.immediate())
    }

    override fun onDestroy() {
        super.onDestroy()
        syncRunner.cancel()
    }
}
