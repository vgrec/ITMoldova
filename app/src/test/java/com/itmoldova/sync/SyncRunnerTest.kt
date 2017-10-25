package com.itmoldova.sync

import com.itmoldova.AppSettings
import com.itmoldova.TestUtils
import com.itmoldova.controller.NotificationController
import com.itmoldova.http.ITMoldovaService
import com.itmoldova.http.NetworkDetector
import com.itmoldova.model.Rss
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import rx.Observable
import rx.Scheduler

class SyncRunnerTest {

    private lateinit var mockScheduler: Scheduler
    private lateinit var mockService: ITMoldovaService
    private lateinit var mockNetworkDetector: NetworkDetector
    private lateinit var syncRunner: SyncRunner

    @Before
    fun setUp() {
        mockScheduler = mock(Scheduler::class.java)
        mockService = mock(ITMoldovaService::class.java)
        mockNetworkDetector = mock(NetworkDetector::class.java)

        syncRunner = SyncRunner(
                mock(NotificationController::class.java),
                mock(AppSettings::class.java),
                mockService,
                mockNetworkDetector)
    }


    @Test
    fun testNoSyncWhenNoConnection() {
        `when`(mockNetworkDetector.hasInternetConnection()).thenReturn(false)

        syncRunner.start(mockScheduler, mockScheduler)
        verify<ITMoldovaService>(mockService, never()).getDefaultRssFeed(anyInt())
    }

    @Test
    fun testSyncStartsWhenConnection() {
        `when`(mockNetworkDetector.hasInternetConnection()).thenReturn(true)
        `when`<Observable<Rss>>(mockService.getDefaultRssFeed(anyInt())).thenReturn(
                Observable.just<Rss>(TestUtils.rssResponse())
        )

        syncRunner.start(mockScheduler, mockScheduler)
        verify<ITMoldovaService>(mockService, times(1)).getDefaultRssFeed(anyInt())
    }
}
