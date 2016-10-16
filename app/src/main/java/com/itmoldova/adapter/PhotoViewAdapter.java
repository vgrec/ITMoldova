package com.itmoldova.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itmoldova.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> urls;

    public PhotoViewAdapter(Context context, List<String> urls) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_photo, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        new PhotoViewAttacher(imageView);
        Picasso.with(context)
                .load(urls.get(position))
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
