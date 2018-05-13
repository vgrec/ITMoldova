package com.itmoldova.settings

import android.os.Bundle
import android.view.MenuItem
import com.itmoldova.BaseActivity
import com.itmoldova.R

class SettingsActivity : BaseActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (IS_DARK) R.style.AppTheme_Dark else R.style.AppTheme_Light)
        super.onCreate(savedInstanceState)
        // Fragment added via XML
        setContentView(R.layout.activity_settings)

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
