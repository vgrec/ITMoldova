package com.itmoldova.list;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itmoldova.R;
import com.itmoldova.model.Article;

import java.util.List;

/**
 * Author vgrec, on 09.07.16.
 */
public class ArticlesFragment extends Fragment implements ArticlesContract.View {

    private ArticlesContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadArticles();
    }

    @Override
    public void showNews(List<Article> articles) {

    }

    @Override
    public void setLoadingIndicator(boolean loading) {

    }

    @Override
    public void setPresenter(ArticlesContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
