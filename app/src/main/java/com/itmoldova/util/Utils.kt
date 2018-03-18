package com.itmoldova.util

import android.text.format.DateUtils
import android.transition.Transition
import com.itmoldova.model.Article
import io.reactivex.Observable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
     * If article is NOT in the list -> return the first six articles
     * If article IS in the list -> replace it with the article at position 0 and
     * return the first six articles from the list
     *
     * @param articles a list of [Article]s
     * @param article  the article for which the related articles should be retrieved
     */
    fun getRelatedArticles(articles: List<Article>?, article: Article?, n: Long): List<Article> {
        if (articles == null || article == null) {
            return emptyList()
        }

        val selectedArticles = mutableListOf<Article>()
        Observable.fromIterable(articles)
                .take(n)
                .subscribe({item -> selectedArticles.add(item)})

        if (selectedArticles.contains(article)) {
            selectedArticles[selectedArticles.indexOf(article)] = selectedArticles[0]
            selectedArticles.removeAt(0)
        } else {
            selectedArticles.removeAt(selectedArticles.size - 1)
        }

        return selectedArticles
    }

    open class TransactionListenerAdapter : Transition.TransitionListener {
        override fun onTransitionEnd(transition: Transition?) {
        }

        override fun onTransitionResume(transition: Transition?) {
        }

        override fun onTransitionPause(transition: Transition?) {
        }

        override fun onTransitionCancel(transition: Transition?) {
        }

        override fun onTransitionStart(transition: Transition?) {
        }
    }
}
