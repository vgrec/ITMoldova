package com.itmoldova.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Custom implementation of [RecyclerView.OnScrollListener] that
 * provides the ability to get notified when the bottom of the list
 * is reached.
 */
abstract class EndlessScrollListener @JvmOverloads constructor(private val layoutManager: LinearLayoutManager, private val visibleThreshold: Int = DEFAULT_VISIBLE_THRESHOLD) : RecyclerView.OnScrollListener() {

    /**
     * The total number of items in the dataset after the last load
     */
    private var previousTotalItemCount: Int = 0

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var loading = true
    private var currentPage = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = 1
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    fun reset() {
        this.currentPage = 1
        this.loading = false
    }

    companion object {

        private const val DEFAULT_VISIBLE_THRESHOLD = 5
    }
}
