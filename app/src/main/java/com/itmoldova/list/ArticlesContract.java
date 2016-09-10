package com.itmoldova.list;

import com.itmoldova.model.Item;

import java.util.List;

/**
 * Author vgrec, on 09.07.16.
 */
public interface ArticlesContract {

    interface View {
        void showArticles(List<Item> items);

        void setLoadingIndicator(boolean loading);

        void showError();

        void showNoInternetConnection();
    }

    interface Presenter {
        void loadArticles();

        void cancel();
    }

}
