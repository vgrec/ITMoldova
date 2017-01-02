package com.itmoldova.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.content.ContextCompat;

import com.itmoldova.Extra;
import com.itmoldova.R;
import com.itmoldova.detail.DetailActivity;
import com.itmoldova.list.MainActivity;
import com.itmoldova.model.Item;
import com.itmoldova.parser.ContentParser;

import java.util.List;
import java.util.Random;

/**
 * Helper class that shows a status bar notification in different styles
 * depending on the number of new articles published.
 * <p>
 * <pre>
 * One article:
 *        if the article contains an image then use {@link BigPictureStyle}
 *        otherwise use {@link BigTextStyle}.
 *
 * More than one article:
 *        display multiline notification using {@link InboxStyle}.
 * </pre>
 */
public class NotificationController {

    private static final int MULTILINE_NOTIFICATION_ID = 1;
    private Context context;

    public NotificationController(Context context) {
        this.context = context;
    }

    public void showNotification(List<Item> items) {
        Item firstItem = items.get(0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentTitle(firstItem.getTitle())
                .setContentText(firstItem.getDescription())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(getNotificationStyle(items))
                .setContentIntent(getPendingIntent(items));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotificationId(items.size()), builder.build());
    }

    private NotificationCompat.Style getNotificationStyle(List<Item> items) {
        Item firstItem = items.get(0);
        String title = firstItem.getTitle();
        String description = firstItem.getDescription();

        int size = items.size();
        if (size > 1) {
            InboxStyle inboxStyle = new InboxStyle();
            inboxStyle.setBigContentTitle(size + " " + context.getString(R.string.new_articles));
            for (Item item : items) {
                inboxStyle.addLine(item.getTitle());
            }
            return inboxStyle;
        } else if (ContentParser.extractFirstImage(firstItem.getContent()) != null) {
            BigPictureStyle pictureStyle = new BigPictureStyle();
            pictureStyle.setBigContentTitle(title);
            pictureStyle.setSummaryText(description);
            pictureStyle.bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.test));
            return pictureStyle;
        } else {
            BigTextStyle textStyle = new BigTextStyle();
            textStyle.setBigContentTitle(title);
            textStyle.bigText(description);
            return textStyle;
        }
    }

    private PendingIntent getPendingIntent(List<Item> items) {
        Intent intent;
        if (items.size() == 1) {
            intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Extra.ITEM, items.get(0));
        } else {
            intent = new Intent(context, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestCode = new Random().nextInt(Integer.MAX_VALUE);
        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int getNotificationId(int itemsSize) {
        // We want to have only one multiline notification displayed, in order
        // to achieve this, all multiline notifications will share the same notification id.
        if (itemsSize > 1) {
            return MULTILINE_NOTIFICATION_ID;
        }
        return new Random().nextInt(Integer.MAX_VALUE);
    }

}
