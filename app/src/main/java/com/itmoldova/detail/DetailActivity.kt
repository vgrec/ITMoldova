package com.itmoldova.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.model.Item
import com.itmoldova.util.ActivityUtils

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val item = intent.getParcelableExtra<Item>(Extra.ITEM)
            val items = intent.getParcelableArrayListExtra<Item>(Extra.ITEMS)
            val detailFragment = DetailFragment.newInstance(items, item)
            ActivityUtils.addFragmentToActivity(fragmentManager, detailFragment, android.R.id.content)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val detailFragment = fragmentManager.findFragmentById(android.R.id.content) as DetailFragment
        if (detailFragment != null) {
            val item = intent.getParcelableExtra<Item>(Extra.ITEM)
            val items = intent.getParcelableArrayListExtra<Item>(Extra.ITEMS)
            detailFragment.loadArticle(items, item)
        }
    }
}
