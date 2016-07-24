package com.itmoldova.detail;

import com.itmoldova.BasePresenter;
import com.itmoldova.BaseView;
import com.itmoldova.model.Item;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailContract {

    interface View extends BaseView<Presenter> {
        void showArticleDetail(Item item);
    }

    interface Presenter extends BasePresenter {

    }
}
