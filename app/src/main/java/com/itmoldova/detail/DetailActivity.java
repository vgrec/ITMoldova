package com.itmoldova.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itmoldova.R;
import com.itmoldova.model.Item;
import com.itmoldova.util.ActivityUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.frameContent);
        if (detailFragment == null) {
            detailFragment = new DetailFragment();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), detailFragment, R.id.frameContent);
        }

        Item item = getIntent().getParcelableExtra(ITEM);
        new DetailPresenter(detailFragment, item);

    }
}
