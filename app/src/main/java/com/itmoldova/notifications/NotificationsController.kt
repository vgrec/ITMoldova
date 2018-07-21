package com.itmoldova.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.VisibleForTesting
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat
import android.text.Html
import com.bumptech.glide.Glide
import com.itmoldova.AppSettings
import com.itmoldova.Extra
import com.itmoldova.ITMoldova
import com.itmoldova.R
import com.itmoldova.detail.DetailActivity
import com.itmoldova.kotlinex.runOnVersion
import com.itmoldova.list.MainActivity
import com.itmoldova.model.Article
import com.itmoldova.util.Logs
import com.itmoldova.util.UiUtils
import com.itmoldova.util.Utils
import java.util.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * Helper class that shows a status bar notification in different styles
 * depending on the number of new articles published.
 *
 *
 * <pre>
 * One article:
 * if the article contains an image then use [BigPictureStyle]
 * otherwise use [BigTextStyle].
 *
 * More than one article:
 * display multiline notification using [InboxStyle].
</pre> *
 */
class NotificationsController @Inject constructor(private val context: Context,
                                                  private val notificationManager: NotificationManager,
                                                  private val appSettings: AppSettings) {

    enum class NotificationType {
        MULTILINE,
        BIG_IMAGE,
        BIG_TEXT
    }

    fun shouldShowNotification(articles: List<Article>): Boolean = getNumberOfNewArticles(articles) > 0

    fun showNotification(articles: List<Article>) {
        val type = detectNotificationTypeToShow(articles)
        when (type) {
            NotificationsController.NotificationType.MULTILINE -> showMultilineNotification(articles)
            NotificationsController.NotificationType.BIG_IMAGE -> showBigImageNotification(articles)
            NotificationsController.NotificationType.BIG_TEXT -> showBigTextNotification(articles)
        }
    }

    @VisibleForTesting
    fun detectNotificationTypeToShow(articles: List<Article>): NotificationType {
        if (getNumberOfNewArticles(articles) > 1) {
            return NotificationType.MULTILINE
        }

        val hasImage = UiUtils.extractFirstImage(articles[0].content) != null
        return if (hasImage) NotificationType.BIG_IMAGE else NotificationType.BIG_TEXT
    }

    private fun showMultilineNotification(articles: List<Article>) {
        val firstArticle = articles[0]

        val inboxStyle = InboxStyle()
        inboxStyle.setBigContentTitle(getNumberOfNewArticles(articles).toString() + " " + context.getString(R.string.new_articles))
        (0 until getNumberOfNewArticles(articles))
                .map { articles[it] }
                .forEach { inboxStyle.addLine(it.title) }

        val builder = createBaseBuilder(firstArticle.title, firstArticle.description)
        builder.setStyle(inboxStyle)
        builder.setContentIntent(createMainActivityPendingIntent())
        notificationManager.notify(MULTILINE_NOTIFICATION_ID, builder.build())
    }

    fun showBigTextNotification(articles: List<Article>) {
        val firstArticle = articles[0]

        val textStyle = BigTextStyle()
        textStyle.setBigContentTitle(firstArticle.title)
        textStyle.bigText(htmlToPlainText(firstArticle.description))

        val builder = createBaseBuilder(firstArticle.title, firstArticle.description)
        builder.setStyle(textStyle)
        builder.setContentIntent(createDetailActivityPendingIntent(firstArticle))
        notificationManager.notify(generateId(), builder.build())
    }

    private fun showBigImageNotification(articles: List<Article>) {
        val firstArticle = articles[0]
        val bitmap = loadBitmap(firstArticle)
        if (bitmap != null) {
            val pictureStyle = BigPictureStyle()
            pictureStyle.setBigContentTitle(firstArticle.title)
            pictureStyle.bigPicture(bitmap)

            val builder = createBaseBuilder(firstArticle.title, firstArticle.description)
            builder.setStyle(pictureStyle)
            builder.setContentIntent(createDetailActivityPendingIntent(firstArticle))
            notificationManager.notify(generateId(), builder.build())
        }
    }

    private fun loadBitmap(firstArticle: Article): Bitmap? {
        val futureTarget = Glide.with(context.applicationContext)
                .load(UiUtils.extractFirstImage(firstArticle.content))
                .asBitmap()
                .fitCenter()
                .into(NOTIFICATION_IMAGE_WIDTH, NOTIFICATION_IMAGE_HEIGHT)

        var bitmap: Bitmap? = null
        try {
            bitmap = futureTarget.get()
        } catch (e: InterruptedException) {
            Logs.e("Error while loading image for notification", e)
        } catch (e: ExecutionException) {
            Logs.e("Error while loading image for notification", e)
        }

        return bitmap
    }

    private fun createBaseBuilder(title: String, description: String): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentTitle(title)
                .setContentText(htmlToPlainText(description))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        runOnVersion(Build.VERSION_CODES.O) {
            builder.setChannelId(ITMoldova.DEFAULT_CHANNEL_ID)
        }

        return builder
    }

    @VisibleForTesting
    fun getNumberOfNewArticles(articles: List<Article>): Int {
        val lastPubDateSaved = appSettings.lastPubDate
        val firstArticlePubDate = Utils.pubDateToMillis(articles[0].pubDate)
        if (lastPubDateSaved == firstArticlePubDate) {
            return 0
        }

        // exit early if the subsequent articles are older than the lastPubDateSaved
        return articles
                .map { Utils.pubDateToMillis(it.pubDate) }
                .takeWhile { it > lastPubDateSaved }
                .count()
    }

    private fun createMainActivityPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, generateId(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createDetailActivityPendingIntent(article: Article): PendingIntent {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(Extra.ARTICLE, article)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, generateId(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun generateId(): Int = Random().nextInt(Integer.MAX_VALUE)

    private fun htmlToPlainText(description: String): String {
        return Html.fromHtml(Html.fromHtml(description).toString()).toString()
    }

    companion object {
        private val MULTILINE_NOTIFICATION_ID = 1
        private val NOTIFICATION_IMAGE_WIDTH = 400
        private val NOTIFICATION_IMAGE_HEIGHT = 250
    }

}
