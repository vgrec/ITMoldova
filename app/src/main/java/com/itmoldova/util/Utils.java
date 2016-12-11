package com.itmoldova.util;

import android.text.format.DateUtils;
import android.util.Log;

import com.itmoldova.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
}
