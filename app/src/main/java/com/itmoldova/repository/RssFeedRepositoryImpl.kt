package com.itmoldova.repository

import com.itmoldova.http.ITMoldovaService
import com.itmoldova.model.Rss
import io.reactivex.Observable
import javax.inject.Inject

class RssFeedRepositoryImpl @Inject constructor(
    private val itMoldovaService: ITMoldovaService,
) : RssFeedRepository {

    override fun getDefaultRssFeed(page: Int): Observable<Rss> {
        return itMoldovaService.getDefaultRssFeed(page)
    }

    override fun getRssFeedByCategory(categoryName: String, page: Int): Observable<Rss> {
        return itMoldovaService.getRssFeedByCategory(categoryName, page)
    }
}