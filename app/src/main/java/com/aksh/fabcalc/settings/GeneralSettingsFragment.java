package com.aksh.fabcalc.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.history.HistoryContract;
import com.aksh.fabcalc.utils.CalculationUtils;

/**
 * Created by Aakarshit on 06-07-2017.
 */

public class GeneralSettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
        PreferenceManager manager = getPreferenceManager();
        Preference clearHistoryPreference =
                manager.findPreference("clear_history");
        clearHistoryPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        HistoryContract.clearAllRecords(getContext());
                        Toast.makeText(getContext(), "History Cleared Successfully", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_degrees_key))) {
            CalculationUtils.setAngleUnit(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
