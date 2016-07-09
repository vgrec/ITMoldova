package com.itmoldova.list;

import com.itmoldova.model.Article;
import com.itmoldova.BasePresenter;
import com.itmoldova.BaseView;

import java.util.List;

/**
 * Author vgrec, on 09.07.16.
 */
public interface ArticlesListContract {

    interface View extends BaseView<Presenter> {
        void showNews(List<Article> articles);

        void setLoadingIndicator(boolean loading);
    }

    interface Presenter extends BasePresenter {
        void loadNews();
    }

}
