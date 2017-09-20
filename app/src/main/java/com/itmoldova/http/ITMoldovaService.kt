package com.itmoldova.http

import com.itmoldova.model.Rss

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Author vgrec, on 09.07.16.
 */
interface ITMoldovaService {
    @GET("/feed")
    fun getDefaultRssFeed(@Query("paged") page: Int): Observable<Rss>

    @GET("/category/{category_name}/feed")
    fun getRssFeedByCategory(@Path("category_name") category: String, @Query("paged") page: Int): Observable<Rss>
}