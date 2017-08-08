package com.aksh.fabcalc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.aksh.fabcalc.R;

/**
 * Created by Aakarshit on 23-07-2017.
 */

public class ColorUtils {
    private int primaryColor;
    private int secondaryColor;
    private int textColor;

    private int primaryColorDark;
    private ColorStateList primaryColorListDark;
    private ColorStateList keyColorList;

    public static ColorUtils currentColors;
    public static final int PRIMARY_COLOR_DIALOG_ID = 121;
    public static final int SECONDARY_COLOR_DIALOG_ID = 122;
    public static final int TEXT_COLOR_DIALOG_ID = 123;

    private int tintColor;

    public ColorUtils(int primaryColor, int secondaryColor) {
        this(primaryColor, secondaryColor, getAutoTextColor());
    }

    private static int getAutoTextColor() {
        // TODO: 23-07-2017 Handle this
        return Color.WHITE;
    }

    public ColorUtils(int primaryColor, int secondaryColor, int textColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.textColor = textColor;
        float hsvHolder[] = new float[3];
        Color.colorToHSV(primaryColor, hsvHolder);
        hsvHolder[2] = hsvHolder[2] > 0.1f ? hsvHolder[2] - 0.1f :  hsvHolder[2] / 2f;
        tintColor = textColor;
        primaryColorDark = Color.HSVToColor(hsvHolder);
        primaryColorListDark = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_enabled}, // enabled
                        new int[] {-android.R.attr.state_enabled}  // disabled
                        },
                new int[] {
                        tintColor,
                        tintColor
                }
        );
        keyColorList = ColorStateList.valueOf(Color.WHITE);
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getPrimaryColorDark() {
        return primaryColorDark;
    }

    public ColorStateList getPrimaryColorListDark() {
        return primaryColorListDark;
    }

    public ColorStateList getKeyColorList() {
        return keyColorList;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setPrimaryColorDark(int primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
    }

    public void setPrimaryColorListDark(ColorStateList primaryColorListDark) {
        this.primaryColorListDark = primaryColorListDark;
    }

    public void setKeyColorList(ColorStateList keyColorList) {
        this.keyColorList = keyColorList;
    }

    public static void update(Context context, SharedPreferences preferences) {
        ColorUtils newColors = new ColorUtils(
                preferences.getInt(context.getString(R.string.pref_color_primary_key),
                        ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null)),
                preferences.getInt(context.getString(R.string.pref_color_secondary_key),
                        ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null)),
                preferences.getInt(context.getString(R.string.pref_color_text_key),
                        ResourcesCompat.getColor(context.getResources(), R.color.colorWhite, null))
        );
        if (preferences.getBoolean(context.getString(R.string.pref_color_keys_key),
                context.getResources().getBoolean(R.bool.pref_color_keys_default))) {
            newColors.setKeyColorList(ColorStateList.valueOf(newColors.getSecondaryColor()));
        }
        currentColors = newColors;
    }
}
