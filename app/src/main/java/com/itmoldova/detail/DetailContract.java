package com.itmoldova.detail;

import android.view.View;

import com.itmoldova.model.Item;

import java.util.List;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailContract {

    interface View {
        void showArticleDetail(List<android.view.View> views);

        void showRelatedArticles(android.view.View relatedArticles);

        void showTitle(String title);

        void showHeaderImage(String url);

        void hideHeaderImage();
    }

    interface Presenter {
        void loadArticleDetail(Item item);

        void loadRelatedArticles(List<Item> items, Item item);

        List<String> extractPhotoUrlsFromArticle();
    }
}
