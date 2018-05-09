package com.itmoldova

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    companion object {
        var IS_DARK = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}