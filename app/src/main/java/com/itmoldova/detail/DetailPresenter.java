package com.itmoldova.detail;

import android.view.View;

import com.itmoldova.model.Item;
import com.itmoldova.parser.Block;
import com.itmoldova.parser.DetailViewCreator;
import com.itmoldova.parser.ContentParser;

import java.util.List;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private DetailViewCreator detailViewCreator;

    public DetailPresenter(DetailContract.View view, DetailViewCreator detailViewCreator) {
        this.view = view;
        this.detailViewCreator = detailViewCreator;
    }

    @Override
    public void loadArticle(Item item) {
        view.showTitle(item.getTitle());

        List<Block> blocks = ContentParser.parse(item.getContent());
        List<View> views = detailViewCreator.createViewsFrom(blocks);
        view.showArticleDetail(views);
    }
}