package com.aksh.fabcalc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.aksh.fabcalc.R;

/**
 * Created by Aakarshit on 25-07-2017.
 */

public class MyColorPreference extends Preference {

    public ImageView colorIndicator;

    public MyColorPreference(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyColorPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        colorIndicator = (ImageView) holder.findViewById(R.id.color_indicator);
        int color = (sharedPreferences.getInt(getKey(),
                ResourcesCompat.getColor(getContext().getResources(), R.color.colorPrimary, null)));
        ((GradientDrawable)colorIndicator.getBackground()).setColor(color);
        super.onBindViewHolder(holder);
    }
}
