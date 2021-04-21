package com.somethingsimple.nasapod.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.databinding.SettingsActivityBinding

class SettingsActivity : BaseActivity() {
    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.fab.setOnClickListener {
            onBackPressed()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            preferenceManager.findPreference<SwitchPreference>(getString(R.string.pref_force_dark))
                ?.apply {
                    onPreferenceChangeListener =
                        Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
                            (newValue as Boolean).let {
                                setGlobalNightMode(it)
                                sharedPreferences.edit()
                                    .putBoolean(getString(R.string.pref_force_dark), it)
                                    .apply()
                            }
                            true
                        }
                }
            preferenceManager.findPreference<ListPreference>(getString(R.string.pref_theme))
                ?.apply {
                    onPreferenceChangeListener =
                        Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
                            sharedPreferences.edit()
                                .putString(getString(R.string.pref_theme), newValue as String)
                                .apply()
                            true
                        }
                }
            return super.onCreateView(inflater, container, savedInstanceState)
        }

    }
}