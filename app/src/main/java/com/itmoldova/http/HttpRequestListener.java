package com.itmoldova.http;

import com.itmoldova.model.Rss;

/**
 * Author vgrec, on 10.07.16.
 */
public interface HttpRequestListener {
    void onStart();

    void onTerminate();

    void onSuccess(Rss response);

    void onError(Throwable e);
}
