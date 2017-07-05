package com.aksh.fabcalc.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.aksh.fabcalc.R;

/**
 * Created by Aakarshit on 17-06-2017.
 */

public class MyFab extends FloatingActionButton {

    private String text;
    private String drawableText;
    private Context mContext;

    public MyFab(Context context) {
        super(context);
        mContext = context;
    }

    public MyFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initCustoms(attrs);
    }

    public MyFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initCustoms(attrs);
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Nullable
    public String getText() {
        if (text == null) {
            return drawableText;
        }
        return text;
    }

    public void setDrawableText(@Nullable String drawableText) {
        this.drawableText = drawableText;
        setImageDrawable(getTextDrawableFromText(drawableText));
    }

    @Nullable
    public String getDrawableText() {
        return drawableText;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    private void initCustoms(AttributeSet attributeSet){
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.MyFab, 0, 0);
        try {
            // get the text and colors specified using the names in attrs.xml
            setText(a.getString(R.styleable.MyFab_text));
            setDrawableText(a.getString(R.styleable.MyFab_drawableText));
        } finally {
            a.recycle();
        }
    }

    private Drawable getTextDrawableFromText(String text) {
        return new DrawableUtils(mContext).getTextDrawable(text);
    }
}
