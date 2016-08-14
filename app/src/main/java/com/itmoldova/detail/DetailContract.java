package com.itmoldova.detail;

import com.itmoldova.BasePresenter;
import com.itmoldova.BaseView;

import java.util.List;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailContract {

    interface View extends BaseView<Presenter> {
        void showArticleDetail(List<android.view.View> views);

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {

    }
}
