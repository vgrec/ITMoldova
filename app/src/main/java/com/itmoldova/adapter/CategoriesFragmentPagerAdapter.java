package com.itmoldova.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itmoldova.list.ArticlesFragment;
import com.itmoldova.model.Category;

public class CategoriesFragmentPagerAdapter extends FragmentPagerAdapter {

    private Category[] categories = Category.values();
    private Resources resources;

    public CategoriesFragmentPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return resources.getString(categories[position].getStringResId());
    }

    @Override
    public Fragment getItem(int position) {
        return ArticlesFragment.newInstance(categories[position]);
    }

    @Override
    public int getCount() {
        return categories.length;
    }
}