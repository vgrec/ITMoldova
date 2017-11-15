package com.itmoldova.sync

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class ITMoldovaJobCreator(private var rssChecker: RssChecker) : JobCreator {

    override fun create(tag: String): Job? = when (tag) {
        SyncJob.TAG -> SyncJob(rssChecker)
        else -> null
    }

}