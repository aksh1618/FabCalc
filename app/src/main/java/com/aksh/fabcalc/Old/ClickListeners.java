package com.aksh.fabcalc.Old;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.databinding.CalcDisplayBinding;
import com.aksh.fabcalc.utils.TextDrawable;
import com.autofit.et.lib.AutoFitEditText;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class ClickListeners {

    // TODO: 20-06-2017 Separate calculation related logic
    private static final double MAX_ERROR = 1e100;
    private static boolean operationDone = false;
    private static final int DECIMAL_PRECISION = 15;
    private static String TAG = ClickListeners.class.getSimpleName();
    private static String[] inputs;

    public static void onKeyClicked(Context context, View view, CalcDisplayBinding display){
        String keyText = ((TextDrawable)((FloatingActionButton)view).getDrawable()).getText();
        Toast toast = Toast.makeText(context, keyText, Toast.LENGTH_SHORT);
        AutoFitEditText input = display.inputEditText;
        // TODO: 20-06-2017 Name this live-something ?
        TextView resultPrev = display.resultTextView;
//        populateInputs(context);

        resultPrev.setVisibility(View.VISIBLE);

        if (isInput(keyText.charAt(0))) {
//            Log.d("ClickListener", "onKeyClicked: " + input.getText());
            insert(input, keyText);
        } else if (keyText.equals(context.getString(R.string.key_clear))) {
            input.setText("");
            resultPrev.setText(context.getString(R.string.key_num_zero));
//        } else if (keyText.equals(context.getString(R.string.key_backspace))) {
//            backspace(input);
        } else {
            // TODO: 19-06-2017 Use arity jar as lib, could be used later for factorials PnC etc
            if (operationDone) {
                input.setText("");
                input.append(resultPrev.getText());
            }
            resultPrev.setVisibility(View.GONE);
            toast.show();
        }

        evaluate(resultPrev, getEvaluableString(input.getText(), context));

        // TODO: 16-06-2017 Call a method to adjust text size
        // TODO: 16-06-2017 Update Result Text View
    }

//    private static void populateInputs(Context context) {
//        inputs = new String[]{
//                context.getString(R.string.key_num_zero),
//                context.getString(R.string.key_num_one),
//                context.getString(R.string.key_num_two),
//                context.getString(R.string.key_num_three),
//                context.getString(R.string.key_num_four),
//                context.getString(R.string.key_num_five),
//                context.getString(R.string.key_num_six),
//                context.getString(R.string.key_num_seven),
//                context.getString(R.string.key_num_eight),
//                context.getString(R.string.key_num_nine),
//                context.getString(R.string.key_plus),
//                context.getString(R.string.key_minus),
//                context.getString(R.string.key_divide),
//                context.getString(R.string.key_multiply),
//                context.getString(R.string.key_equal),
//                context.getString(R.string.key_paren_open),
//                context.getString(R.string.key_paren_close)
//        };
//    }

    private static boolean isInput(char c) {
        if(Character.isDigit(c)) return true;
        switch (c) {
            case '+':
            case '-':
            case 'x':
            case '/':
            case '(':
            case ')':
            case '.': return true;
        }
        return false;
    }

    private static void insert(AutoFitEditText myEditText, String textToInsert) {
        int start = Math.max(myEditText.getSelectionStart(), 0);
        int end = Math.max(myEditText.getSelectionEnd(), 0);
        myEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                textToInsert, 0, textToInsert.length());
    }

    private static void backspace(AutoFitEditText myEditText) {
        int start = Math.max(myEditText.getSelectionStart(), 0);
        int end = Math.max(myEditText.getSelectionEnd(), 0);
        if (start == end) start = start - 1;
        if (start < 0) return;
        myEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                "", 0, 0);
    }

    private static String getEvaluableString(Editable text, Context context) {
        return text.toString()
                .replace(context.getString(R.string.key_plus).charAt(0),'+')
                .replace(context.getString(R.string.key_minus).charAt(0),'-')
                .replace(context.getString(R.string.key_multiply).charAt(0),'*')
                .replace(context.getString(R.string.key_divide).charAt(0),'/');
    }

    private static void evaluate(TextView resultPrev, String stringToEvaluate) {
        String resultText = resultPrev.getText().toString();
        try {
            double result = new ExpressionBuilder(stringToEvaluate).build().evaluate();
            // TODO: 19-06-2017 Use arity's double to String here, in order to get 1.2*3 = 3.6
//            resultText = Double.toString(shortApprox(result, MAX_ERROR));
//            resultText = Double.toString(round(result, 10));
            operationDone = true;
            resultText = roundString(result);
        } catch (Exception iTE) {
          // Ignore
        } finally {
            resultPrev.setText(resultText);
        }
    }

//    private static double shortApprox(double value, double maxError) {
//        final double v = Math.abs(value);
//        final double tail = Double.parseDouble("1E" + (int)Math.floor(Math.log10(Math.abs(maxError))));
//        final double ret = Math.floor(v/tail +.5)*tail;
//        return (value < 0) ? -ret : ret;
//    }

    private static String roundString(double value) {
//        String valueString = "" + value;
//        Log.d(TAG, "double: " + valueString);
//        Log.d(TAG, "bigDecimal: " + new BigDecimal(value, MathContext.DECIMAL128).toPlainString());
//        Log.d(TAG, "bigDecimal: " + new BigDecimal(
//                value, new MathContext(decimalDigits, RoundingMode.HALF_EVEN)).toPlainString());
//        String valueString = new BigDecimal(value, MathContext.DECIMAL128).toPlainString();
//        if (! valueString.contains(".")) return String.valueOf(Double.parseDouble(valueString));
//        String mantissaString = valueString.split("\\.")[0];
//        Log.d(TAG, "mantissa: " + mantissaString);
//        String fractionString = "." + valueString.split("\\.")[1];
//        Log.d(TAG, "fraction: " + fractionString);
//        double fraction = Double.parseDouble(fractionString);
//        double factor = Double.parseDouble("1E" + decimalDigits);
//        double roundedFraction =  Math.round(fraction * factor) / factor;
//        Log.d(TAG, "roundedFraction: " + roundedFraction);
//        fractionString = "" + roundedFraction;
////        return Math.round(value * factor) / factor;
//        return mantissaString + fractionString.substring(1);
//        int mantissaLength = ("" + value).split("\\.")[0].length();
//        MathContext mc = new MathContext(mantissaLength + DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
        MathContext mc = new MathContext(DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
//        MathContext mc = MathContext.UNLIMITED;
        BigDecimal myBigDecimal = new BigDecimal(value, mc);
        Log.d(TAG, "roundString: scale = " + myBigDecimal.scale());
        if (myBigDecimal.scale() != 0) {
            myBigDecimal =  myBigDecimal.stripTrailingZeros();
        }
        return myBigDecimal.toString();
    }
}
