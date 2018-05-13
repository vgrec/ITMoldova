package com.itmoldova.bookmarks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.itmoldova.BaseActivity
import com.itmoldova.R

class BookmarksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (BaseActivity.IS_DARK) R.style.AppTheme_Dark else R.style.AppTheme_Light)
        super.onCreate(savedInstanceState)
        // Fragment added via XML
        setContentView(R.layout.activity_bookmarks)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
