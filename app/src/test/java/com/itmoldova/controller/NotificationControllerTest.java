package com.itmoldova.controller;

import android.app.NotificationManager;
import android.content.Context;

import com.itmoldova.AppSettings;
import com.itmoldova.controller.NotificationController.NotificationType;
import com.itmoldova.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotificationControllerTest {

    private NotificationController notificationController;
    private AppSettings appSettings;

    @Before
    public void setUp() {
        appSettings = mock(AppSettings.class);
        notificationController = new NotificationController(
                mock(Context.class),
                mock(NotificationManager.class),
                appSettings);
    }

    @Test
    public void testShouldShowNotification_NoNewArticles() {
        when(appSettings.getLastPubDate()).thenReturn(1483604987000L); //  5 Jan 8:29
        boolean shouldShow = notificationController.shouldShowNotification(TestUtils.twoArticles());
        assertThat(shouldShow).isFalse();
    }

    @Test
    public void testShouldShowNotification_Success() {
        when(appSettings.getLastPubDate()).thenReturn(1483597787000L); //  5 Jan 6:29
        boolean shouldShow = notificationController.shouldShowNotification(TestUtils.twoArticles());
        assertThat(shouldShow).isTrue();
    }

    @Test
    public void testGetNumberOfArticles_OneNewArticle() {
        when(appSettings.getLastPubDate()).thenReturn(1483597787000L); //  5 Jan 6:29
        int newArticles = notificationController.getNumberOfNewArticles(TestUtils.twoArticles());
        assertThat(newArticles).isEqualTo(1);
    }

    @Test
    public void testGetNumberOfArticles_FourNewArticles() {
        when(appSettings.getLastPubDate()).thenReturn(1483344319000L); // 2 Jan 08:05
        int newArticles = notificationController.getNumberOfNewArticles(TestUtils.sixArticles());
        assertThat(newArticles).isEqualTo(4);
    }

    @Test
    public void testDetectMultilineNotification() {
        NotificationType type = notificationController
                .detectNotificationTypeToShow(TestUtils.twoArticles());
        assertThat(type).isEqualTo(NotificationType.MULTILINE);
    }

    @Test
    public void testDetectBigImageNotification() {
        NotificationType type = notificationController
                .detectNotificationTypeToShow(TestUtils.oneArticleWithImage());
        assertThat(type).isEqualTo(NotificationType.BIG_IMAGE);
    }

    @Test
    public void testDetectBigTextNotification() {
        NotificationType type = notificationController
                .detectNotificationTypeToShow(TestUtils.oneArticleWithNoImage());
        assertThat(type).isEqualTo(NotificationType.BIG_TEXT);
    }
}
