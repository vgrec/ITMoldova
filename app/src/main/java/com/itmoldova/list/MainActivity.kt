package com.itmoldova.list

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.itmoldova.AppSettings
import com.itmoldova.ITMoldova
import com.itmoldova.R
import com.itmoldova.adapter.CategoriesFragmentPagerAdapter
import com.itmoldova.bookmarks.BookmarksActivity
import com.itmoldova.info.InfoActivity
import com.itmoldova.sync.SyncJob
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    @Inject
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        ITMoldova.appComponent.inject(this)
        setTheme(if (appSettings.isDarkModeEnabled) R.style.AppTheme_Dark_NoActionBar else R.style.AppTheme_Light_NoActionBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                val themeSwitch = findViewById<SwitchCompat>(R.id.theme_switch)
                themeSwitch.isChecked = appSettings.isDarkModeEnabled
                themeSwitch.setOnCheckedChangeListener { view, isChecked ->
                    drawer.postDelayed({
                        appSettings.isDarkModeEnabled = !appSettings.isDarkModeEnabled
                        recreate()
                    }, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                }
            }

            private fun setupNotificationsSwitch() {
                val notificationsSwitch = findViewById<SwitchCompat>(R.id.notifications_switch)
                notificationsSwitch.isChecked = appSettings.areNotificationsEnabled
                notificationsSwitch.setOnCheckedChangeListener { view, isChecked ->
                    appSettings.areNotificationsEnabled = !appSettings.areNotificationsEnabled
                    if (appSettings.areNotificationsEnabled) {
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
        val id = item.itemId
        when (id) {
            R.id.action_bookmarks -> startActivity(Intent(this, BookmarksActivity::class.java))
            R.id.action_info -> startActivity(Intent(this, InfoActivity::class.java))
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
