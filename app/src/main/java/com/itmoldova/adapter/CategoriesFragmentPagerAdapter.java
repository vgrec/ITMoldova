package com.itmoldova.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itmoldova.list.ArticlesFragment;

public class CategoriesFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] items;

    public CategoriesFragmentPagerAdapter(FragmentManager fm, String[] items) {
        super(fm);
        this.items = items;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items[position];
    }

    @Override
    public Fragment getItem(int position) {
        return ArticlesFragment.newInstance(items[position]);
    }

    @Override
    public int getCount() {
        return items.length;
    }
}