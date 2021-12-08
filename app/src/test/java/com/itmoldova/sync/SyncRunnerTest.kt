package com.itmoldova.sync

import com.itmoldova.AppSettings
import com.itmoldova.TestData
import com.itmoldova.notifications.NotificationsController
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.NetworkDetector
import com.itmoldova.model.Rss
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.*

@Ignore
class SyncRunnerTest {

    private lateinit var mockService: ITMoldovaService
    private lateinit var mockNetworkDetector: NetworkDetector
    private lateinit var syncRunner: RssChecker
    private lateinit var scheduler: Scheduler

    @Before
    fun setUp() {
        mockService = mock(ITMoldovaService::class.java)
        mockNetworkDetector = mock(NetworkDetector::class.java)
        scheduler = Schedulers.trampoline()

        syncRunner = RssChecker(
                mock(NotificationsController::class.java),
                mock(AppSettings::class.java),
                mockService,
                mockNetworkDetector)
    }


    @Test
    fun testNoSyncWhenNoConnection() {
        `when`(mockNetworkDetector.hasInternetConnection()).thenReturn(false)

        syncRunner.start(scheduler, scheduler)
        verify<ITMoldovaService>(mockService, never()).getDefaultRssFeed(anyInt())
    }

    @Test
    fun testSyncStartsWhenConnection() {
        `when`(mockNetworkDetector.hasInternetConnection()).thenReturn(true)
        `when`<Observable<Rss>>(mockService.getDefaultRssFeed(anyInt())).thenReturn(
                Observable.just<Rss>(TestData.rssResponse())
        )

        syncRunner.start(scheduler, scheduler)
        verify<ITMoldovaService>(mockService, times(1)).getDefaultRssFeed(anyInt())
    }
}
