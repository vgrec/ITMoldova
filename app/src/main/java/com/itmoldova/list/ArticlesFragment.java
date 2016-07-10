package com.itmoldova.list;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itmoldova.R;
import com.itmoldova.adapter.ArticlesAdapter;
import com.itmoldova.model.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author vgrec, on 09.07.16.
 */
public class ArticlesFragment extends Fragment implements ArticlesContract.View {

    private ArticlesContract.Presenter presenter;
    private ArticlesAdapter adapter;
    private List<Item> items = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ArticlesAdapter(items);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadArticles();
    }

    @Override
    public void showArticles(List<Item> items) {
        int fromPosition = this.items.size();
        int toPosition = items.size();
        this.items.addAll(items);
        adapter.notifyItemRangeInserted(fromPosition, toPosition);
    }

    @Override
    public void setLoadingIndicator(boolean loading) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNoInternetConnection() {

    }

    @Override
    public void setPresenter(ArticlesContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
