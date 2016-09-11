package com.itmoldova.model;

import com.itmoldova.R;

/**
 * Category names as defined in ITMoldova.com site.
 * <p>
 * Author vgrec, on 11.09.16.
 */
public enum Category {
    HOME("home", R.string.home),
    IT_WORLD("it-world", R.string.it_world),
    IT_NEWS_MD("it-news-md", R.string.it_news_md),
    SOFTWARE("noutati-software", R.string.software),
    HARDWARE("noutati-hardware", R.string.hardware),
    JOCURI("game-news", R.string.jocuri),
    SECURITATE("securitate-2", R.string.securitate),
    SOFT_GRATIS("soft-gratis", R.string.soft_gratis),
    SFATURI_UTILE("sfaturi-utile", R.string.sfaturi_utile),
    ISTORIA_IT("istoria-it", R.string.istoria_it),
    FUNNY_IT("funny-it", R.string.funny_it);

    private String name;
    private int stringResId;

    Category(String name, int stringResId) {
        this.name = name;
        this.stringResId = stringResId;
    }

    public String getCategoryName() {
        return name;
    }

    public int getStringResId() {
        return stringResId;
    }
}
