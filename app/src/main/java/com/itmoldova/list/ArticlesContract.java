package com.itmoldova.list;

import com.itmoldova.model.Category;
import com.itmoldova.model.Item;

import java.util.List;

/**
 * Author vgrec, on 09.07.16.
 */
public interface ArticlesContract {

    interface View {
        void showArticles(List<Item> items, boolean clearDataSet);

        void setLoadingIndicator(boolean loading);

        void showError();

        void showNoInternetConnection();

        void openArticleDetail(List<Item> items, Item item);

    }

    interface Presenter {
        void loadArticles(Category category, int page);

        void refreshArticles(Category category);

        void cancel();

        void onArticleClicked(List<Item> items, Item item);
    }

}
