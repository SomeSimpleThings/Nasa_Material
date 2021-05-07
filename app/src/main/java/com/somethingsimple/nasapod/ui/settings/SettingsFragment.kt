package com.somethingsimple.nasapod.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.ui.MainActivity
import com.somethingsimple.nasapod.ui.common.setGlobalNightMode

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
                        (newValue as String).let {
                            sharedPreferences.edit()
                                .putString(getString(R.string.pref_theme), it)
                                .apply()
                            (activity as MainActivity).apply {
                                setAppTheme(it)
                                recreate()
                            }
                        }
                        true
                    }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}