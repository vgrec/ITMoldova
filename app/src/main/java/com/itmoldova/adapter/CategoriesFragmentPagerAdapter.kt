package com.itmoldova.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.itmoldova.list.ArticlesFragment
import com.itmoldova.model.Category

class CategoriesFragmentPagerAdapter(fm: FragmentManager, private val resources: Resources) : FragmentPagerAdapter(fm) {

    private val categories = Category.values()

    override fun getPageTitle(position: Int): CharSequence =
            resources.getString(categories[position].stringResId)

    override fun getItem(position: Int): Fragment = ArticlesFragment.newInstance(categories[position])

    override fun getCount(): Int = categories.size
}