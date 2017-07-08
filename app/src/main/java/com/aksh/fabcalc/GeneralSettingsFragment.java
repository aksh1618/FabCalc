package com.aksh.fabcalc;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.aksh.fabcalc.R;

/**
 * Created by Aakarshit on 06-07-2017.
 */

public class GeneralSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_calc);
    }
}
