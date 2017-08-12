package com.aksh.fabcalc.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.aksh.fabcalc.R;
import com.amulyakhare.textdrawable.TextDrawable;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class DrawableUtils {

    private TextDrawable.IShapeBuilder mBuilder;
    private Context mContext;

    public DrawableUtils(Context context) {
        mContext = context;
//        Resources res = mContext.getResources();
        mBuilder = TextDrawable.builder()
                .beginConfig()
//                .textColor(ColorUtils.currentColors.getTextColor())
//                .fontSize(res.getDimensionPixelSize(R.dimen.short_label_text_size))
                .useFont(Typeface.SANS_SERIF)
                .bold()
                .endConfig();
    }

    public DrawableUtils(Context context, TextDrawable.IShapeBuilder builder) {
        mContext = context;
        mBuilder = builder;
    }

    TextDrawable getTextDrawable(String text) {
        int fontSize = mContext.getResources().getDimensionPixelSize(R.dimen.short_label_text_size);
        if (text.length() > 1) {
            fontSize = mContext.getResources().getDimensionPixelSize(R.dimen.medium_label_text_size);
            if(text.length() > 3) fontSize = mContext.getResources().getDimensionPixelSize(R.dimen.long_label_text_size);
        }
        mBuilder.beginConfig()
                .fontSize(fontSize)
                .textColor(ColorUtils.currentColors.getTextColor())
                .endConfig();
        return mBuilder.buildRound(text, Color.TRANSPARENT);
    }
}
