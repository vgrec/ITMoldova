package com.itmoldova.util

import android.text.format.DateUtils

import com.itmoldova.model.Item

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale

import rx.Observable

object Utils {

    private val PUB_DATE_FORMATTER = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)

    /**
     * Formats the publication date.
     *
     * @param pubDate the publication date.
     * @return the publication date formatted or the original one in case
     * an exception occurred
     */
    fun formatPubDate(pubDate: String): String {
        try {
            val date = PUB_DATE_FORMATTER.parse(pubDate)

            val flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_ABBREV_MONTH

            return DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS, flags).toString()
        } catch (e: ParseException) {
            Logs.e("Exception while formatting the pubDate", e)
            return pubDate
        }

    }

    fun pubDateToMillis(pubDate: String): Long {
        try {
            val date = PUB_DATE_FORMATTER.parse(pubDate)
            return date.time
        } catch (e: ParseException) {
            Logs.e("Exception while formatting the pubDate", e)
            return -1
        }

    }

    /**
     * If item is NOT in the list -> return the first six items
     * If item IS in the list -> replace it with the item at position 0 and
     * return the first six items from the list
     *
     * @param items a list of [Item]s
     * @param item  the item for which the related articles should be retrieved
     */
    fun getRelatedArticles(items: List<Item>?, item: Item?, n: Int): List<Item> {
        if (items == null || item == null) {
            return emptyList()
        }

        val selectedItems = Observable.from(items)
                .take(n)
                .toList().toBlocking().single()

        if (selectedItems.contains(item)) {
            selectedItems[selectedItems.indexOf(item)] = selectedItems[0]
            selectedItems.removeAt(0)
        } else {
            selectedItems.removeAt(selectedItems.size - 1)
        }

        return selectedItems
    }
}
