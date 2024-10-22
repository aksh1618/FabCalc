package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.CalculationUtils.evaluate;
import static com.aksh.fabcalc.utils.CalculationUtils.getEvaluableString;
import static com.aksh.fabcalc.utils.DisplayUtils.backspace;
import static com.aksh.fabcalc.utils.DisplayUtils.resetDisplay;
import static com.aksh.fabcalc.utils.DisplayUtils.insert;
import static com.aksh.fabcalc.utils.DisplayUtils.updatePreview;
import static com.aksh.fabcalc.utils.LabelsUtils.inputLabels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.databinding.ActivityMainBinding;
import com.aksh.fabcalc.history.HistoryContract;
import com.aksh.fabcalc.history.HistoryProvider;
import com.autofit.et.lib.AutoFitEditText;
import com.shazam.android.widget.text.reflow.ReflowTextAnimatorHelper;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class ClickListeners {

    private static String TAG = ClickListeners.class.getSimpleName();
    // TODO: 19-07-2017 replace with last key pressed ?
    private static boolean equalPressed = false;

    public static void onKeyClicked(Context context, View view, ActivityMainBinding calc){
        String keyText = ((MyFab)view).getText();
        final AutoFitEditText inputEditText = calc.display.inputEditText;
        final TextView resultPrev = calc.display.resultTextView;

        if (inputLabels.contains(keyText)) {
            insert(inputEditText, keyText);
            equalPressed = false;
            updatePreview(resultPrev, getEvaluableString(inputEditText.getText(), context));
        } else if (keyText.equals(context.getString(R.string.key_clear))) {
            if (equalPressed) {
                if (inputEditText.getText().length() > 0)
                    resetDisplay(calc, view);
            } else if (inputEditText.getText().length() < 2) {
                inputEditText.setText("");
                resultPrev.setText("");
                resultPrev.setVisibility(View.GONE);
            } else {
                backspace(inputEditText);
                updatePreview(resultPrev, getEvaluableString(inputEditText.getText(), context));
            }
        } else { // If equal is pressed
            equalPressed = true;
            String finalExpression = inputEditText.getText().toString();
            String result;
            if (finalExpression.length() > 0) {
                try {
                    result = evaluate(getEvaluableString(inputEditText.getText(), context));
                    // TODO: 24-07-2017 If exception is caught, show error in prev, not input.
                } catch (IllegalArgumentException iae) { // Invalid Expression
                    result = "Not A Number !!";
                    Log.w(TAG, "onKeyClicked: " + iae.getStackTrace(), iae);
                } catch (ArithmeticException ae) { // Division By Zero
                    // TODO: 05-07-2017 Show infinity?
                    result = "Division by zero XP";
                    Log.w(TAG, "onKeyClicked: " + ae.getStackTrace(), ae);
                }
                inputEditText.setText("");
                inputEditText.append(result);
                HistoryContract.insertRecord(finalExpression, result, context);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
                    Animator resultAnimator = new ReflowTextAnimatorHelper
                            .Builder(resultPrev,inputEditText)
                            .withDuration(250,300)
                            .withVelocity(1500)
                            .buildAnimator();
                    resultAnimator.addListener(new AnimatorListenerAdapter(){
                        @Override
                        public void onAnimationStart(Animator animation){
//                            resultPrev.setVisibility(View.GONE);
                            inputEditText.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationEnd(Animator animation){
                            inputEditText.setVisibility(View.VISIBLE);
                        }
                    });
                    resultAnimator.start();
                } else {
                    resultPrev.setText("");
                    resultPrev.setVisibility(View.GONE);
                }
                // TODO: 19-07-2017 Do something bout this
//                AnimationUtils.animatePreviewToResult(resultPrev, inputEditText);
            }
//            resultPrev.setText("");
//            resultPrev.setVisibility(View.GONE);
        }
//        updatePreview(resultPrev, getEvaluableString(inputEditText.getText(), context));
    }

    public static void setEqualPressed(boolean equalPressed) {
        ClickListeners.equalPressed = equalPressed;
    }
}
