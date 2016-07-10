package com.itmoldova.http;

import com.itmoldova.model.Rss;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Author vgrec, on 09.07.16.
 */
public interface ITMoldovaService {

    @GET("/feed")
    Observable<Rss> getRssFeed();
}