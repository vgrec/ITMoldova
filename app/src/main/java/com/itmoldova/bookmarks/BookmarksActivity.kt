package com.itmoldova.bookmarks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itmoldova.util.ActivityUtils

class BookmarksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            ActivityUtils.addFragmentToActivity(fragmentManager, BookmarksFragment(), android.R.id.content)
        }
    }
}
