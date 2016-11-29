package com.itmoldova.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itmoldova.util.ActivityUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ActivityUtils.addFragmentToActivity(getFragmentManager(), new SettingsFragment(), android.R.id.content);
        }
    }
}
