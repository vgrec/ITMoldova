package com.itmoldova.detail;

import com.itmoldova.model.Item;

import java.util.List;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailContract {

    interface View {
        void showArticleDetail(List<android.view.View> views);

        void showTitle(String title);
    }

    interface Presenter {
        void loadArticle(Item item);
    }
}
