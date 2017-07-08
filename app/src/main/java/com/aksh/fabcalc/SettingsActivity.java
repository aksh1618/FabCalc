package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.Animations.revealFromCenter;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.aksh.fabcalc.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding calcSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calcSettings = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        // TODO: 05-07-2017 Up Navigation and home button?
    }

    @Override
    protected void onResume() {
        super.onResume();

        int childCount = calcSettings.settingsMainLayout.getChildCount();
        for(int i=0; i<childCount; i++) {
            final View myView = calcSettings.settingsMainLayout.getChildAt(i);
            myView.post(new Runnable() {
                @Override
                public void run() {
                    revealFromCenter(myView);
                }
            });
        }
    }
}
