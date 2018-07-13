package com.itmoldova.bookmarks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.R
import javax.inject.Inject

class BookmarksActivity : AppCompatActivity() {

    @Inject
    lateinit var appSetting: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        ITMoldova.appComponent.inject(this)
        setTheme(if (appSetting.isDarkModeEnabled) R.style.AppTheme_Dark else R.style.AppTheme_Light)
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
