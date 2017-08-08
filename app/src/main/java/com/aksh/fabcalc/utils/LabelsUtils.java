package com.aksh.fabcalc.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.aksh.fabcalc.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class LabelsUtils {

    static List<String> inputLabels;
    private static List<String> basicLabels;
    private static List<String> advancedLabels;
    static List<String> parenFunctions;
    static List<String> trigonometricFunctions;

    public static void initArrays(Context context){
        inputLabels = Arrays.asList(context.getResources().getStringArray(R.array.input_labels));
        basicLabels = Arrays.asList(context.getResources().getStringArray(R.array.basic_labels));
        advancedLabels = Arrays.asList(context.getResources().getStringArray(R.array.advanced_labels));
        parenFunctions = Arrays.asList(context.getResources().getStringArray(R.array.paren_functions));
//        trigonometricFunctions = Arrays.asList(context.getResources().getStringArray(R.array.trigonometric_functions));
    }

    public static void applyLabels(ViewGroup viewGroup, State state) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            MyFab myFab = (MyFab) viewGroup.getChildAt(i);
            switch (state) {
                case BASIC:
                    myFab.setDrawableText(basicLabels.get(i));
                    break;
                case ADVANCED:
                    myFab.setDrawableText(advancedLabels.get(i));
                    break;
            }
        }
    }

    @Nullable
    static List<String> getLabelsForState(State state) {
        switch (state) {
            case BASIC:
                return basicLabels;
            case ADVANCED:
                return advancedLabels;
        }
        return null;
    }
}

