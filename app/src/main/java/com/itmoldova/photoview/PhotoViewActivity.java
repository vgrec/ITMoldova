package com.itmoldova.photoview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.itmoldova.Extra;
import com.itmoldova.R;
import com.itmoldova.adapter.PhotoViewAdapter;
import com.itmoldova.anim.DepthPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.page_number)
    TextView pageNumber;

    private int totalNumberOfUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);

        ArrayList<String> urls = getIntent().getStringArrayListExtra(Extra.INSTANCE.getPHOTO_URLS());
        String clickedUrl = getIntent().getStringExtra(Extra.INSTANCE.getCLICKED_URL());
        totalNumberOfUrls = urls.size();

        PhotoViewAdapter adapter = new PhotoViewAdapter(this, urls);
        viewPager.setAdapter(adapter);
        int currentItem = urls.indexOf(clickedUrl) != -1 ? urls.indexOf(clickedUrl) : 0;
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(this);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        updatePageCounter(currentItem);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void updatePageCounter(int currentItem) {
        pageNumber.setText(getString(R.string.page_counter, currentItem + 1, totalNumberOfUrls));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // not used
    }

    @Override
    public void onPageSelected(int position) {
        updatePageCounter(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // not used
    }
}
