package com.itmoldova.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Custom implementation of {@link RecyclerView.OnScrollListener} that
 * provides the ability to get notified when the bottom of the list
 * is reached.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int DEFAULT_VISIBLE_THRESHOLD = 5;

    /**
     * The total number of items in the dataset after the last load
     */
    private int previousTotalItemCount;

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean loading = true;

    private int visibleThreshold;
    private int currentPage = 1;

    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this(layoutManager, DEFAULT_VISIBLE_THRESHOLD);
    }

    public EndlessScrollListener(LinearLayoutManager layoutManager, int visibleThreshold) {
        this.layoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = 1;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

    public void reset() {
        this.currentPage = 1;
        this.loading = false;
    }
}
