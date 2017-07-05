package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.CalculationUtils.evaluate;
import static com.aksh.fabcalc.utils.LabelsLists.parenFunctions;

import android.widget.TextView;

import com.autofit.et.lib.AutoFitEditText;

/**
 * Created by Aakarshit on 05-07-2017.
 */

public class DisplayUtils {
    static void updatePreview(TextView resultPrev, String stringToEvaluate) {
        String resultText = resultPrev.getText().toString();
        try {
            resultText = evaluate(stringToEvaluate);
        } catch (Exception iTE) {
            // Ignore
        } finally {
            resultPrev.setText(resultText);
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
//        textToInsert = getTextWithFunctionParens(textToInsert);
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
}
