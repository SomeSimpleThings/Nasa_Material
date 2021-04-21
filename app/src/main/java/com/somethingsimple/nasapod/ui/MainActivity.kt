package com.somethingsimple.nasapod.ui

import android.os.Bundle
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.ui.pod.PodFragment

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PodFragment.newInstance())
                .commitNow()
        }
    }
}