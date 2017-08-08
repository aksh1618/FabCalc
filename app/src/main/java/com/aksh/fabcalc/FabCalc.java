package com.aksh.fabcalc;

import android.app.Application;
import android.support.v7.preference.PreferenceManager;

import com.aksh.fabcalc.utils.ColorUtils;

/**
 * Created by Aakarshit on 15-07-2017.
 */

public class FabCalc extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ColorUtils.update(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
//        Colorful.init(this);
    }
}
