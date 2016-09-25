package com.itmoldova.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itmoldova.Extra;
import com.itmoldova.R;
import com.itmoldova.model.Item;
import com.itmoldova.parser.DetailViewCreator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Shows details of an article.
 *
 * Author vgrec, on 09.07.16.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter presenter;
    private Item item;

    @BindView(R.id.content)
    ViewGroup contentGroup;

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static DetailFragment newInstance(Item item) {
        Bundle args = new Bundle();
        args.putParcelable(Extra.ITEM, item);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getParcelable(Extra.ITEM);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();

        presenter = new DetailPresenter(this, new DetailViewCreator(getActivity()));
        presenter.loadArticle(item);
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.articles_list, menu);
    }

    @Override
    public void showArticleDetail(List<View> views) {
        for (View view : views) {
            contentGroup.addView(view);
        }
    }

    @Override
    public void showTitle(String title) {
        titleView.setText(title);
    }
}
