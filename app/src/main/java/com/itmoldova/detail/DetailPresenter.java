package com.itmoldova.detail;

import android.view.View;

import com.itmoldova.model.Item;
import com.itmoldova.parser.Block;
import com.itmoldova.parser.DetailViewCreator;
import com.itmoldova.parser.ContentParser;
import com.itmoldova.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author vgrec, on 24.07.16.
 */
public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private DetailViewCreator detailViewCreator;
    private List<Block> blocks;
    private String imageHeaderUrl;

    public DetailPresenter(DetailContract.View view, DetailViewCreator detailViewCreator) {
        this.view = view;
        this.detailViewCreator = detailViewCreator;
    }

    @Override
    public void loadArticleDetail(Item item) {
        view.showTitle(item.getTitle());

        ContentParser parser = new ContentParser(item.getContent());
        blocks = parser.normalize(parser.parse());
        imageHeaderUrl = parser.getHeaderImageFromBlocks(blocks);
        if (imageHeaderUrl != null) {
            view.showHeaderImage(imageHeaderUrl);
        } else {
            view.hideHeaderImage();
        }

        List<View> views = detailViewCreator.createViewsFrom(blocks);
        view.showArticleDetail(views);
    }

    @Override
    public void loadRelatedArticles(List<Item> items, Item item) {
        List<Item> relatedArticles = Utils.getRelatedArticles(items, item, 5);
        view.showRelatedArticles(relatedArticles);
    }

    @Override
    public List<String> extractPhotoUrlsFromArticle() {
        if (blocks == null) {
            return Collections.emptyList();
        }

        List<String> urls = new ArrayList<>();
        urls.add(imageHeaderUrl);
        for (Block block : blocks) {
            if (block.getType() == Block.Type.IMAGE) {
                urls.add(block.getContent());
            }
        }

        return urls;
    }

}
