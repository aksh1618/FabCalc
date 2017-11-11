package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.AnimationUtils.revealFromXY;
import static com.aksh.fabcalc.utils.CalculationUtils.evaluate;
import static com.aksh.fabcalc.utils.LabelsUtils.parenFunctions;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.databinding.ActivityMainBinding;
import com.autofit.et.lib.AutoFitEditText;

/**
 * Created by Aakarshit on 05-07-2017.
 */

public class DisplayUtils {
    public static void updatePreview(TextView resultPrev, String stringToEvaluate) {
        String resultText = resultPrev.getText().toString();
        try {
            resultText = evaluate(stringToEvaluate);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                resultText = "NaN";
            }
        } finally {
            resultPrev.setText(resultText);
            resultPrev.setVisibility(View.VISIBLE);
        }
    }

    static void insert(AutoFitEditText myEditText, String textToInsert) {
        if (parenFunctions.contains(textToInsert)) {
            textToInsert += "(";
            if (textToInsert.equals("logb(")) {
                // TODO: 27-06-2017 Add hint? Try to put cursor before comma?
                textToInsert = "logb(,)";
            }
        }
        int start = Math.max(myEditText.getSelectionStart(), 0);
        int end = Math.max(myEditText.getSelectionEnd(), 0);
        myEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                textToInsert, 0, textToInsert.length());
    }

    static void backspace(AutoFitEditText myEditText) {
        int start = Math.max(myEditText.getSelectionStart(), 0);
        int end = Math.max(myEditText.getSelectionEnd(), 0);
        if (start == end) start = start - 1;
        if (start < 0) return;
        myEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                "", 0, 0);
    }

    public static void resetDisplay(ActivityMainBinding calc, View view) {
        calc.display.inputEditText.setText("");
        calc.display.resultTextView.setText("");
        calc.display.resultTextView.setVisibility(View.GONE);
        // TODO: 27-06-2017 change color for reveal anim?
        int coords[] = new int[2];
        view.getLocationOnScreen(coords);
        revealFromXY(calc.displayCardView, coords[0], coords[1]);
    }
}
