package com.itmoldova.util;

import android.text.format.DateUtils;

import com.itmoldova.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;

public class Utils {

    public static final SimpleDateFormat PUB_DATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    /**
     * Formats the publication date.
     *
     * @param pubDate the publication date.
     * @return the publication date formatted or the original one in case
     * an exception occurred
     */
    public static String formatPubDate(String pubDate) {
        try {
            Date date = PUB_DATE_FORMATTER.parse(pubDate);

            int flags = DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_SHOW_TIME
                    | DateUtils.FORMAT_SHOW_YEAR
                    | DateUtils.FORMAT_ABBREV_MONTH;

            return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS, flags).toString();
        } catch (ParseException e) {
            Logs.e("Exception while formatting the pubDate", e);
            return pubDate;
        }
    }

    public static long pubDateToMillis(String pubDate) {
        try {
            Date date = PUB_DATE_FORMATTER.parse(pubDate);
            return date.getTime();
        } catch (ParseException e) {
            Logs.e("Exception while formatting the pubDate", e);
            return -1;
        }
    }

    /**
     * If item is NOT in the list -> return the first six items
     * If item IS in the list -> replace it with the item at position 0 and
     * return the first six items from the list
     *
     * @param items a list of {@link Item}s
     * @param item  the item for which the related articles should be retrieved
     */
    public static List<Item> getRelatedArticles(List<Item> items, Item item) {
        if (items == null || item == null) {
            return Collections.emptyList();
        }

        List<Item> selectedItems = Observable.from(items)
                .take(7)
                .toList().toBlocking().single();

        if (selectedItems.contains(item)) {
            selectedItems.set(selectedItems.indexOf(item), selectedItems.get(0));
            selectedItems.remove(0);
        } else {
            selectedItems.remove(selectedItems.size() - 1);
        }

        return selectedItems;
    }
}
