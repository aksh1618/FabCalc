package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.AnimationUtils.revealFromXY;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.aksh.fabcalc.databinding.ActivitySettingsBinding;
import com.aksh.fabcalc.utils.ColorUtils;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class SettingsActivity extends AppCompatActivity implements
        ColorPickerDialogListener{

    ActivitySettingsBinding calcSettings;
    Setting currentSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calcSettings = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        currentSetting = Setting.GENERAL;
        // TODO: 05-07-2017 Up Navigation and home button?

        calcSettings.options.bottombar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calc:
                                onBackPressed();
                                break;
                            case R.id.action_theme:
                                switchToSetting(Setting.THEME);
                                break;
                            case R.id.action_colors:
                                switchToSetting(Setting.COLOR);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyColors();

        int childCount = calcSettings.settingsMainLayout.getChildCount();
        for(int i=0; i<childCount; i++) {
            final View myView = calcSettings.settingsMainLayout.getChildAt(i);
            myView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO: 19-07-2017 Fix This (Add bottombar to settings which stays stationary?)
                    revealFromXY(myView, myView.getLeft(), myView.getBottom());
                }
            });
        }
    }

    private void applyColors() {
        calcSettings.setColors(ColorUtils.currentColors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.currentColors.getPrimaryColorDark());
            getWindow().setNavigationBarColor(ColorUtils.currentColors.getPrimaryColorDark());
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        SharedPreferences.Editor preferenceEditor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        switch (dialogId) {
            case ColorUtils.PRIMARY_COLOR_DIALOG_ID:
                preferenceEditor.putInt(getString(R.string.pref_color_primary_key), color);
                break;
            case ColorUtils.SECONDARY_COLOR_DIALOG_ID:
                preferenceEditor.putInt(getString(R.string.pref_color_secondary_key), color);
                break;
            case ColorUtils.TEXT_COLOR_DIALOG_ID:
                preferenceEditor.putInt(getString(R.string.pref_color_text_key), color);
                break;
        }
        preferenceEditor.apply();
        ColorUtils.update(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }

    private void switchToSetting(Setting destSetting) {
        getFrameLayoutForSetting(currentSetting).setVisibility(View.GONE);
        getFrameLayoutForSetting(destSetting).setVisibility(View.VISIBLE);
        currentSetting = destSetting;
    }

    private FrameLayout getFrameLayoutForSetting(Setting setting) {
        switch (setting) {
            case GENERAL: return calcSettings.generalSettingsFrameLayout;
            case THEME: return calcSettings.themeSettingsFrameLayout;
            case COLOR: return calcSettings.colorSettingsFrameLayout;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (currentSetting == Setting.GENERAL) {
            super.onBackPressed();
        } else {
            switchToSetting(Setting.GENERAL);
        }
    }

    enum Setting {
        GENERAL,
        THEME,
        COLOR
    }
}
// TODO: 15-07-2017 Implement Degrees/Radians using https://github.com/fasseg/exp4j/issues/72