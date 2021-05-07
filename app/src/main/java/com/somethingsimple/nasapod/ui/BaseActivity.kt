package com.somethingsimple.nasapod.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.ui.common.setGlobalNightMode

open class BaseActivity : AppCompatActivity() {
    private lateinit var currentTheme: String
    private var darkMode: Boolean = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = PreferenceManager
            .getDefaultSharedPreferences(this)
        currentTheme = sharedPref.getString(
            getString(R.string.pref_theme),
            getString(R.string.default_theme)
        )!!
        darkMode = sharedPref.getBoolean(
            getString(R.string.pref_force_dark),
            false
        )
        setAppTheme(currentTheme)
        setGlobalNightMode(darkMode)
    }

    override fun onResume() {
        super.onResume()
        val theme =
            sharedPref.getString(getString(R.string.pref_theme), getString(R.string.default_theme))
        if (currentTheme != theme)
            recreate()
    }

    fun setAppTheme(currentTheme: String) {
        when (currentTheme) {
            getString(R.string.default_theme) -> setTheme(R.style.Theme_NASAPOD)
            getString(R.string.grey_theme) -> setTheme(R.style.Theme_NASAPOD_Grey)
            getString(R.string.solarized_theme) -> setTheme(R.style.Theme_NASAPOD_Solarized)
        }
    }
}