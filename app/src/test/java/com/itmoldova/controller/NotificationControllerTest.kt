package com.itmoldova.controller

import android.app.NotificationManager
import android.content.Context
import com.itmoldova.AppSettings
import com.itmoldova.TestUtils
import com.itmoldova.controller.NotificationController.NotificationType
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class NotificationControllerTest {

    private lateinit var notificationController: NotificationController
    private lateinit var appSettings: AppSettings

    @Before
    fun setUp() {
        appSettings = mock(AppSettings::class.java)
        notificationController = NotificationController(
                mock(Context::class.java),
                mock(NotificationManager::class.java),
                appSettings)
    }

    @Test
    fun testShouldShowNotification_NoNewArticles() {
        `when`(appSettings.lastPubDate).thenReturn(1483604987000L) //  5 Jan 8:29
        val shouldShow = notificationController.shouldShowNotification(TestUtils.twoArticles())
        assertThat(shouldShow).isFalse()
    }

    @Test
    fun testShouldShowNotification_Success() {
        `when`(appSettings.lastPubDate).thenReturn(1483597787000L) //  5 Jan 6:29
        val shouldShow = notificationController.shouldShowNotification(TestUtils.twoArticles())
        assertThat(shouldShow).isTrue()
    }

    @Test
    fun testGetNumberOfArticles_OneNewArticle() {
        `when`(appSettings.lastPubDate).thenReturn(1483597787000L) //  5 Jan 6:29
        val newArticles = notificationController.getNumberOfNewArticles(TestUtils.twoArticles())
        assertThat(newArticles).isEqualTo(1)
    }

    @Test
    fun testGetNumberOfArticles_FourNewArticles() {
        `when`(appSettings.lastPubDate).thenReturn(1483344319000L) // 2 Jan 08:05
        val newArticles = notificationController.getNumberOfNewArticles(TestUtils.sixArticles())
        assertThat(newArticles).isEqualTo(4)
    }

    @Test
    fun testDetectMultilineNotification() {
        val type = notificationController
                .detectNotificationTypeToShow(TestUtils.twoArticles())
        assertThat(type).isEqualTo(NotificationType.MULTILINE)
    }

    @Test
    fun testDetectBigImageNotification() {
        val type = notificationController
                .detectNotificationTypeToShow(TestUtils.oneArticleWithImage())
        assertThat(type).isEqualTo(NotificationType.BIG_IMAGE)
    }

    @Test
    fun testDetectBigTextNotification() {
        val type = notificationController
                .detectNotificationTypeToShow(TestUtils.oneArticleWithNoImage())
        assertThat(type).isEqualTo(NotificationType.BIG_TEXT)
    }
}
