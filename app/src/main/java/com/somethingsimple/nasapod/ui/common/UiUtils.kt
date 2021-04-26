package com.somethingsimple.nasapod.ui.common

import androidx.appcompat.app.AppCompatDelegate

fun setGlobalNightMode(nightMode: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (nightMode) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
    )
}