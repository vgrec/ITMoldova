package com.itmoldova.sync;

/**
 * Defines the interface for scheduling repeating syncs.
 */
public interface SyncScheduler {

    /**
     * Schedule repeating sync at the specified interval.
     */
    void scheduleRepeatingSync(long interval);

    /**
     * Cancel sync
     */
    void cancel();

}
