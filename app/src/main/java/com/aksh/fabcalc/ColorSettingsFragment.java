package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.ColorUtils.PRIMARY_COLOR_DIALOG_ID;
import static com.aksh.fabcalc.utils.ColorUtils.SECONDARY_COLOR_DIALOG_ID;
import static com.aksh.fabcalc.utils.ColorUtils.TEXT_COLOR_DIALOG_ID;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;

import com.aksh.fabcalc.utils.MyColorPreference;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;

/**
 * Created by Aakarshit on 23-07-2017.
 */

public class ColorSettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
//        Preference.OnPreferenceChangeListener{

    String TAG = "ColorSettingFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_color);
        PreferenceManager manager = getPreferenceManager();
        Preference primaryColorPreference =
                manager.findPreference(getString(R.string.pref_color_primary_key));
        primaryColorPreference.setDefaultValue(
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        primaryColorPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        ColorPickerDialog.newBuilder()
                                .setColor(getPreferenceScreen().getSharedPreferences().getInt(
                                        getString(R.string.pref_color_primary_key),
                                        ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)
                                ))
                                .setDialogId(PRIMARY_COLOR_DIALOG_ID)
                                .show(getActivity());
                        return true;
                    }
                }
        );
        Preference secondaryColorPreference =
                manager.findPreference(getString(R.string.pref_color_secondary_key));
        secondaryColorPreference.setDefaultValue(
                ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        secondaryColorPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        ColorPickerDialog.newBuilder()
                                .setColor(getPreferenceScreen().getSharedPreferences().getInt(
                                        getString(R.string.pref_color_secondary_key),
                                        ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)
                                ))
                                .setDialogId(SECONDARY_COLOR_DIALOG_ID)
                                .show(getActivity());
                        return true;
                    }
                }
        );
        Preference textColorPreference =
                manager.findPreference(getString(R.string.pref_color_text_key));
        textColorPreference.setDefaultValue(
                ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
        textColorPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        ColorPickerDialog.newBuilder()
                                .setColor(getPreferenceScreen().getSharedPreferences().getInt(
                                        getString(R.string.pref_color_text_key),
                                        ResourcesCompat.getColor(getResources(), R.color.colorBlack, null)
                                ))
                                .setDialogId(TEXT_COLOR_DIALOG_ID)
                                .show(getActivity());
                        return true;
                    }
                }
        );
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
//        Log.d("YO", "onSharedPreferenceChanged: " + key);
        if (preference instanceof MyColorPreference) {
            ImageView colorIndicator = ((MyColorPreference)preference).colorIndicator;
            int color = sharedPreferences.getInt(key,
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            ((GradientDrawable)colorIndicator.getBackground()).setColor(color);
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
