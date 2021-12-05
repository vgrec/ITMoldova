package com.itmoldova.info

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.R
import javax.inject.Inject

class InfoActivity : AppCompatActivity() {

    @Inject
    lateinit var appSetting: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        ITMoldova.appComponent.inject(this)
        setTheme(if (appSetting.isDarkModeEnabled) R.style.AppTheme_Dark else R.style.AppTheme_Light)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val infoTextView = findViewById<TextView>(R.id.info)
        infoTextView.text = Html.fromHtml(getString(R.string.info_text))
        infoTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
