package com.itmoldova.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.support.annotation.VisibleForTesting
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.itmoldova.AppSettings
import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.detail.DetailActivity
import com.itmoldova.list.MainActivity
import com.itmoldova.model.Item
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

    fun shouldShowNotification(items: List<Item>): Boolean = getNumberOfNewArticles(items) > 0

    fun showNotification(items: List<Item>) {
        val type = detectNotificationTypeToShow(items)
        when (type) {
            NotificationsController.NotificationType.MULTILINE -> showMultilineNotification(items)
            NotificationsController.NotificationType.BIG_IMAGE -> showBigImageNotification(items)
            NotificationsController.NotificationType.BIG_TEXT -> showBigTextNotification(items)
        }
    }

    @VisibleForTesting
    fun detectNotificationTypeToShow(items: List<Item>): NotificationType {
        if (getNumberOfNewArticles(items) > 1) {
            return NotificationType.MULTILINE
        }

        val hasImage = UiUtils.extractFirstImage(items[0].content) != null
        return if (hasImage) NotificationType.BIG_IMAGE else NotificationType.BIG_TEXT
    }

    private fun showMultilineNotification(items: List<Item>) {
        val firstItem = items[0]

        val inboxStyle = InboxStyle()
        inboxStyle.setBigContentTitle(getNumberOfNewArticles(items).toString() + " " + context.getString(R.string.new_articles))
        (0 until getNumberOfNewArticles(items))
                .map { items[it] }
                .forEach { inboxStyle.addLine(it.title) }

        val builder = createBaseBuilder(firstItem.title, firstItem.description)
        builder.setStyle(inboxStyle)
        builder.setContentIntent(createMainActivityPendingIntent())
        notificationManager.notify(MULTILINE_NOTIFICATION_ID, builder.build())
    }

    private fun showBigTextNotification(items: List<Item>) {
        val firstItem = items[0]

        val textStyle = BigTextStyle()
        textStyle.setBigContentTitle(firstItem.title)
        textStyle.bigText(firstItem.description)

        val builder = createBaseBuilder(firstItem.title, firstItem.description)
        builder.setStyle(textStyle)
        builder.setContentIntent(createDetailActivityPendingIntent(firstItem))
        notificationManager.notify(generateId(), builder.build())
    }

    private fun showBigImageNotification(items: List<Item>) {
        val firstItem = items[0]
        val bitmap = loadBitmap(firstItem)
        if (bitmap != null) {
            val pictureStyle = BigPictureStyle()
            pictureStyle.setBigContentTitle(firstItem.title)
            pictureStyle.bigPicture(bitmap)

            val builder = createBaseBuilder(firstItem.title, firstItem.description)
            builder.setStyle(pictureStyle)
            builder.setContentIntent(createDetailActivityPendingIntent(firstItem))
            notificationManager.notify(generateId(), builder.build())
        }
    }

    private fun loadBitmap(firstItem: Item): Bitmap? {
        val futureTarget = Glide.with(context.applicationContext)
                .load(UiUtils.extractFirstImage(firstItem.content))
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
        return NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    }

    @VisibleForTesting
    fun getNumberOfNewArticles(items: List<Item>): Int {
        val lastPubDateSaved = appSettings.lastPubDate
        val firstArticlePubDate = Utils.pubDateToMillis(items[0].pubDate)
        if (lastPubDateSaved == firstArticlePubDate) {
            return 0
        }

        // exit early if the subsequent items are older than the lastPubDateSaved
        return items
                .map { Utils.pubDateToMillis(it.pubDate) }
                .takeWhile { it > lastPubDateSaved }
                .count()
    }

    private fun createMainActivityPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, generateId(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createDetailActivityPendingIntent(item: Item): PendingIntent {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(Extra.ITEM, item)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, generateId(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun generateId(): Int = Random().nextInt(Integer.MAX_VALUE)

    companion object {
        private val MULTILINE_NOTIFICATION_ID = 1
        private val NOTIFICATION_IMAGE_WIDTH = 400
        private val NOTIFICATION_IMAGE_HEIGHT = 250
    }

}
