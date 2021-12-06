package com.itmoldova.repository

import com.itmoldova.model.Rss
import io.reactivex.Observable

interface RssFeedRepository {
    fun getDefaultRssFeed(page: Int): Observable<Rss>

    fun getRssFeedByCategory(categoryName: String, page: Int): Observable<Rss>
}