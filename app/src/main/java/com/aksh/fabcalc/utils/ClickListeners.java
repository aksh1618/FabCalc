package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.CalculationUtils.evaluate;
import static com.aksh.fabcalc.utils.CalculationUtils.getEvaluableString;
import static com.aksh.fabcalc.utils.DisplayUtils.backspace;
import static com.aksh.fabcalc.utils.DisplayUtils.insert;
import static com.aksh.fabcalc.utils.DisplayUtils.updatePreview;
import static com.aksh.fabcalc.utils.LabelsLists.inputLabels;
import static com.aksh.fabcalc.utils.LabelsLists.parenFunctions;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.databinding.ActivityMainBinding;
import com.autofit.et.lib.AutoFitEditText;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class ClickListeners {

    private static String TAG = ClickListeners.class.getSimpleName();

    public static void onKeyClicked(Context context, View view, ActivityMainBinding calc){
        String keyText = ((MyFab)view).getText();
        AutoFitEditText inputEditText = calc.display.inputEditText;
        TextView resultPrev = calc.display.resultTextView;

        if (inputLabels.contains(keyText)) {
            insert(inputEditText, keyText);
            resultPrev.setVisibility(View.VISIBLE);
        } else if (keyText.equals(context.getString(R.string.key_clear))) {
            if (resultPrev.getVisibility() == View.GONE || inputEditText.getText().length() < 2) {
                inputEditText.setText("");
                resultPrev.setText(context.getString(R.string.key_num_zero));
                resultPrev.setVisibility(View.GONE);
            } else {
                backspace(inputEditText);
            }
        } else { // If equal is pressed
            // TODO: 26-06-2017 Add some animation (like google calc?)
            if (inputEditText.getText().length() > 0) {
                String result;
                try {
                    result = evaluate(getEvaluableString(inputEditText.getText(), context));
                } catch (IllegalArgumentException iae) { // Invalid Expression
                    result = "Not A Number !!";
                    Log.w(TAG, "onKeyClicked: " + iae.getStackTrace().toString(), iae);
                } catch (ArithmeticException ae) { // Division By Zero
                    // TODO: 05-07-2017 Show infinity?
                    result = "Division by zero XP";
                    Log.w(TAG, "onKeyClicked: " + ae.getStackTrace().toString(), ae);
                }
                inputEditText.setText("");
                inputEditText.append(result);
            }
            resultPrev.setVisibility(View.GONE);
        }
        updatePreview(resultPrev, getEvaluableString(inputEditText.getText(), context));
    }
}
