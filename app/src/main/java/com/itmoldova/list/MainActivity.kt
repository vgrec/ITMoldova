package com.itmoldova.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Switch
import com.itmoldova.AppSettings
import com.itmoldova.BaseActivity
import com.itmoldova.R
import com.itmoldova.adapter.CategoriesFragmentPagerAdapter
import com.itmoldova.bookmarks.BookmarksActivity
import com.itmoldova.sync.SyncJob

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (IS_DARK) R.style.AppTheme_Dark_NoActionBar else R.style.AppTheme_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appSettings = AppSettings(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = CategoriesFragmentPagerAdapter(
                supportFragmentManager,
                resources)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        val root = findViewById<View>(android.R.id.content)
        root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setupThemeSwitch()
                setupNotificationsSwitch()
            }

            private fun setupThemeSwitch() {
                val themeSwitch = findViewById<Switch>(R.id.theme_switch)
                themeSwitch.isChecked = IS_DARK
                themeSwitch.setOnCheckedChangeListener { view, isChecked ->
                    drawer.postDelayed({
                        IS_DARK = !IS_DARK
                        recreate()
                    }, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                }
            }

            private fun setupNotificationsSwitch() {
                val notificationsSwitch = findViewById<Switch>(R.id.notifications_switch)
                notificationsSwitch.isChecked = appSettings.notificationsEnabled
                notificationsSwitch.setOnCheckedChangeListener { view, isChecked ->
                    appSettings.notificationsEnabled = !appSettings.notificationsEnabled
                    if (appSettings.notificationsEnabled) {
                        SyncJob.scheduleSync()
                    } else {
                        SyncJob.cancelSync()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.action_bookmarks) {
            startActivity(Intent(this, BookmarksActivity::class.java))
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
