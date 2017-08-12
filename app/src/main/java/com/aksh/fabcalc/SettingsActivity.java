package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.AnimationUtils.revealFromXY;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.aksh.fabcalc.databinding.ActivitySettingsBinding;
import com.aksh.fabcalc.utils.ColorUtils;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class SettingsActivity extends AppCompatActivity implements
        ColorPickerDialogListener{

    private static final String TAG = SettingsActivity.class.getSimpleName();
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
                                if (currentSetting == Setting.GENERAL) {
                                    onBackPressed();
                                } else {
                                    switchToSetting(Setting.GENERAL);
                                }
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
        animateComponents();
    }

    private void applyColors() {
        calcSettings.setColors(ColorUtils.currentColors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.currentColors.getPrimaryColorDark());
            getWindow().setNavigationBarColor(ColorUtils.currentColors.getPrimaryColorDark());
        }
    }

    private void animateComponents() {
        calcSettings.navbarCardView.post(new Runnable() {
            @Override
            public void run() {
                revealFromXY(
                        calcSettings.navbarCardView,
                        calcSettings.navbarCardView.getRight(),
                        calcSettings.navbarCardView.getBottom()
                );
            }
        });
        calcSettings.settingsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                revealFromXY(
                        calcSettings.settingsLinearLayout,
                        calcSettings.settingsLinearLayout.getRight(),
                        calcSettings.settingsLinearLayout.getBottom()
                );
            }
        });
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
        onResume();
    }

    private void switchToSetting(Setting destSetting) {
        getFrameLayoutForSetting(currentSetting).setVisibility(View.GONE);
        getFrameLayoutForSetting(destSetting).setVisibility(View.VISIBLE);
        Rect bottombarRect = new Rect();
        calcSettings.options.bottombar.getGlobalVisibleRect(bottombarRect);
        int x0 = bottombarRect.left;
        int x1 = bottombarRect.left + bottombarRect.right / 3;
        int x2 = bottombarRect.left + 2 * bottombarRect.right / 3;
        int x3 = bottombarRect.right;
        int x = 0;
        switch (destSetting) {
            case GENERAL: x = (x0 + x1) / 2; break;
            case THEME: x = (x1 + x2) / 2; break;
            case COLOR: x = (x2 + x3) / 2; break;
        }
        int y = (bottombarRect.top + bottombarRect.bottom) / 2;
//        int itemNo = 0;
//        switch (destSetting) {
//            case GENERAL: itemNo = 0; break;
//            case THEME: itemNo = 1; break;
//            case COLOR: itemNo = 2; break;
//        }
//        Rect itemRect = getBottombarItemRect(itemNo);
//        int x = (itemRect.left + itemRect.right) / 2;
//        int y = (itemRect.top + itemRect.bottom) / 2;
//        Log.d(TAG, "switchToSetting: x:" + x);
        revealFromXY(calcSettings.settingsLinearLayout, x, y);
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

    private Rect getBottombarItemRect(int itemNo) {
        Rect itemRect = new Rect();
        View itemView = calcSettings.options.bottombar.getMenu().getItem(itemNo).getActionView();
        itemView.getGlobalVisibleRect(itemRect);
        Log.d(TAG, "getBottombarItemRect: " + itemRect.left + " " + itemRect.right);
        return itemRect;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    enum Setting {
        GENERAL,
        THEME,
        COLOR
    }
}
// TODO: 15-07-2017 Implement Degrees/Radians using https://github.com/fasseg/exp4j/issues/72