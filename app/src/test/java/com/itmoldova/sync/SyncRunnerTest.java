package com.itmoldova.sync;

import com.itmoldova.AppSettings;
import com.itmoldova.controller.NotificationController;
import com.itmoldova.http.ITMoldovaService;
import com.itmoldova.http.NetworkDetector;
import com.itmoldova.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncRunnerTest {

    private ITMoldovaService mockService;
    private NetworkDetector mockNetworkDetector;
    private SyncRunner syncRunner;

    @Before
    public void setUp() {
        mockService = mock(ITMoldovaService.class);
        mockNetworkDetector = mock(NetworkDetector.class);

        syncRunner = new SyncRunner(
                mock(NotificationController.class),
                mock(AppSettings.class),
                mockService,
                mockNetworkDetector);
    }


    @Test
    public void testNoSyncWhenNoConnection() {
        when(mockNetworkDetector.hasInternetConnection()).thenReturn(false);

        syncRunner.start();
        verify(mockService, never()).getDefaultRssFeed(anyInt());
    }

    @Test
    public void testSyncStartsWhenConnection() {
        when(mockNetworkDetector.hasInternetConnection()).thenReturn(true);
        when(mockService.getDefaultRssFeed(anyInt())).thenReturn(
                Observable.just(TestUtils.rssResponse())
        );

        syncRunner.start();
        verify(mockService, times(1)).getDefaultRssFeed(anyInt());
    }
}
