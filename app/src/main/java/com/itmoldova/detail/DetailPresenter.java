package com.itmoldova.detail;

import com.itmoldova.model.Item;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private Item item;

    public DetailPresenter(DetailContract.View view, Item item) {
        this.view = view;
        this.view.setPresenter(this);
        this.item = item;
    }

    @Override
    public void start() {
        view.showArticleDetail(item);
    }
}
