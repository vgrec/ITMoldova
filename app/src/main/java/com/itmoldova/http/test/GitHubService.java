package com.itmoldova.http.test;

import com.itmoldova.model.Rss;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Author vgrec, on 09.07.16.
 */
public interface GitHubService {
    @GET("/users/{login}")
    Observable<Github> getUser(@Path("login") String login);

    @GET("/feed")
    Observable<Rss> getArticles();
}