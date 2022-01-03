package com.chkan.iqtimer.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.chkan.iqtimer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}