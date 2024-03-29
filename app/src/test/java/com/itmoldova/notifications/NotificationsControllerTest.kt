package com.itmoldova.notifications

import android.app.NotificationManager
import android.content.Context
import com.itmoldova.AppSettings
import com.itmoldova.TestData
import com.itmoldova.notifications.NotificationsController.NotificationType
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@Ignore
class NotificationsControllerTest {

    private lateinit var notificationsController: NotificationsController
    private lateinit var appSettings: AppSettings

    @Before
    fun setUp() {
        appSettings = mock(AppSettings::class.java)
        notificationsController = NotificationsController(
                mock(Context::class.java),
                mock(NotificationManager::class.java),
                appSettings)
    }

    @Test
    fun testShouldShowNotification_NoNewArticles() {
        `when`(appSettings.lastPubDate).thenReturn(1483604987000L) //  5 Jan 8:29
        val shouldShow = notificationsController.shouldShowNotification(TestData.twoArticles())
        assertThat(shouldShow).isFalse()
    }

    @Test
    fun testShouldShowNotification_Success() {
        `when`(appSettings.lastPubDate).thenReturn(1483597787000L) //  5 Jan 6:29
        val shouldShow = notificationsController.shouldShowNotification(TestData.twoArticles())
        assertThat(shouldShow).isTrue()
    }

    @Test
    fun testGetNumberOfArticles_OneNewArticle() {
        `when`(appSettings.lastPubDate).thenReturn(1483597787000L) //  5 Jan 6:29
        val newArticles = notificationsController.getNumberOfNewArticles(TestData.twoArticles())
        assertThat(newArticles).isEqualTo(1)
    }

    @Test
    fun testGetNumberOfArticles_FourNewArticles() {
        `when`(appSettings.lastPubDate).thenReturn(1483344319000L) // 2 Jan 08:05
        val newArticles = notificationsController.getNumberOfNewArticles(TestData.sixArticles())
        assertThat(newArticles).isEqualTo(4)
    }

    @Test
    fun testDetectMultilineNotification() {
        val type = notificationsController
                .detectNotificationTypeToShow(TestData.twoArticles())
        assertThat(type).isEqualTo(NotificationType.MULTILINE)
    }

    @Test
    fun testDetectBigImageNotification() {
        val type = notificationsController
                .detectNotificationTypeToShow(TestData.oneArticleWithImage())
        assertThat(type).isEqualTo(NotificationType.BIG_IMAGE)
    }

    @Test
    fun testDetectBigTextNotification() {
        val type = notificationsController
                .detectNotificationTypeToShow(TestData.oneArticleWithNoImage())
        assertThat(type).isEqualTo(NotificationType.BIG_TEXT)
    }
}
