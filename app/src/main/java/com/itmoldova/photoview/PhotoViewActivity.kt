package com.itmoldova.photoview

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.itmoldova.Extra
import com.itmoldova.R
import com.itmoldova.adapter.PhotoViewAdapter
import com.itmoldova.anim.DepthPageTransformer

class PhotoViewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var viewPager: ViewPager
    private lateinit var pageNumber: TextView

    private var totalNumberOfUrls: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)

        viewPager = findViewById(R.id.viewpager)
        pageNumber = findViewById(R.id.page_number)

        val urls = intent.getStringArrayListExtra(Extra.PHOTO_URLS) ?: return
        val clickedUrl = intent.getStringExtra(Extra.CLICKED_URL) ?: return
        totalNumberOfUrls = urls.size

        val currentItem = if (urls.indexOf(clickedUrl) != -1) urls.indexOf(clickedUrl) else 0

        viewPager.adapter = PhotoViewAdapter(this, urls)
        viewPager.currentItem = currentItem
        viewPager.addOnPageChangeListener(this)
        viewPager.setPageTransformer(true, DepthPageTransformer())
        updatePageCounter(currentItem)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun updatePageCounter(currentItem: Int) {
        pageNumber.text = getString(R.string.page_counter, currentItem + 1, totalNumberOfUrls)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.removeOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // not used
    }

    override fun onPageSelected(position: Int) {
        updatePageCounter(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        // not used
    }
}
