package com.itmoldova.sync

/**
 * Defines the interface for scheduling repeating syncs.
 */
interface SyncScheduler {

    /**
     * Schedule repeating sync at the specified interval.
     */
    fun scheduleRepeatingSync(interval: Long)

    /**
     * Cancel sync
     */
    fun cancel()

}
