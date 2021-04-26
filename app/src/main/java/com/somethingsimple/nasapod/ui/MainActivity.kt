package com.somethingsimple.nasapod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.databinding.ActivityMainBinding
import com.somethingsimple.nasapod.ui.favourite.FavouriteFragment
import com.somethingsimple.nasapod.ui.pod.PodFragment
import com.somethingsimple.nasapod.ui.settings.SettingsFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        replaceFragment(PodFragment.newInstance())
        setupBottomNavView()
    }

    private fun setupBottomNavView() {

        binding.bottomNavigationView.apply {
            selectedItemId = R.id.app_bar_explore
            setOnNavigationItemReselectedListener { item ->

            }
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.app_bar_fav -> {
                        replaceFragment(FavouriteFragment.newInstance())
                        true
                    }
                    R.id.app_bar_explore -> {
                        replaceFragment(PodFragment.newInstance())
                        true
                    }
                    R.id.app_bar_settings -> {
                        replaceFragment(SettingsFragment.newInstance())
                        true
                    }
                    else -> false
                }
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }
}