package com.itmoldova.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itmoldova.Extra;
import com.itmoldova.R;
import com.itmoldova.adapter.ArticlesAdapter;
import com.itmoldova.detail.DetailActivity;
import com.itmoldova.http.ITMoldovaServiceCreator;
import com.itmoldova.http.NetworkConnectionManager;
import com.itmoldova.model.Category;
import com.itmoldova.model.Item;
import com.itmoldova.util.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Shows a list of articles.
 * <p>
 * Author vgrec, on 09.07.16.
 */
public class ArticlesFragment extends Fragment implements ArticlesContract.View {

    private ArticlesContract.Presenter presenter;
    private ArticlesAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private Category category;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewEndlessScrollListener endlessScrollListener;

    public static Fragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putSerializable(Extra.CATEGORY, category);
        ArticlesFragment fragment = new ArticlesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        category = (Category) getArguments().getSerializable(Extra.CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticlesAdapter(getActivity(), items, this::openArticleDetail);
        recyclerView.setAdapter(adapter);
        endlessScrollListener = new RecyclerViewEndlessScrollListener(layoutManager);
        recyclerView.addOnScrollListener(endlessScrollListener);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshArticles(category));

        return view;
    }

    private class RecyclerViewEndlessScrollListener extends EndlessScrollListener {

        RecyclerViewEndlessScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            presenter.loadArticles(category, page);
        }
    }

    private void openArticleDetail(Item item) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Extra.ITEM, item);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ArticlesPresenter(
                ITMoldovaServiceCreator.createItMoldovaService(),
                this,
                new NetworkConnectionManager(getActivity().getApplicationContext()));
        presenter.loadArticles(category, 0);
    }

    @Override
    public void showArticles(List<Item> items, boolean clearDataSet) {
        if (clearDataSet) {
            this.items.clear();
            adapter.notifyDataSetChanged();
            endlessScrollListener.reset();
        }

        int fromPosition = this.items.size();
        int toPosition = items.size();
        this.items.addAll(items);
        adapter.notifyItemRangeInserted(fromPosition, toPosition);
    }

    @Override
    public void setLoadingIndicator(boolean loading) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(loading));
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoInternetConnection() {
        Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_LONG).show();
        // TODO: consider showing a SnackBar here
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.articles_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                presenter.refreshArticles(category);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.cancel();
        }
    }
}
