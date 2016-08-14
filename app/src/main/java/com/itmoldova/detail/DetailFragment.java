package com.itmoldova.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itmoldova.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author vgrec, on 09.07.16.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter presenter;

    @BindView(R.id.content)
    ViewGroup contentGroup;

    @BindView(R.id.title)
    TextView titleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.start();
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

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
