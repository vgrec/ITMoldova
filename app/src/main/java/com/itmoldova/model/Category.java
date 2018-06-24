package com.itmoldova.model;

import com.itmoldova.R;

/**
 * Category names as defined in ITMoldova.com site.
 * <p>
 * Author vgrec, on 11.09.16.
 */
public enum Category {
    HOME("home", R.string.category_home),
    IT_WORLD("it-world", R.string.category_it_world),
    IT_NEWS_MD("it-news-md", R.string.category_it_news_md),
    SOFTWARE("noutati-software", R.string.category_software),
    HARDWARE("noutati-hardware", R.string.category_hardware),
    JOCURI("game-news", R.string.category_games),
    SECURITATE("securitate-2", R.string.category_security),
    SOFT_GRATIS("soft-gratis", R.string.category_free_software),
    SFATURI_UTILE("sfaturi-utile", R.string.category_tips),
    ISTORIA_IT("istoria-it", R.string.category_history),
    FUNNY_IT("funny-it", R.string.category_funny);

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
