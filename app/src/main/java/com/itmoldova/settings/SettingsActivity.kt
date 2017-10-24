package com.itmoldova.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.itmoldova.util.ActivityUtils

class SettingsActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            ActivityUtils.addFragmentToActivity(fragmentManager, SettingsFragment(), android.R.id.content)
        }
    }
}
