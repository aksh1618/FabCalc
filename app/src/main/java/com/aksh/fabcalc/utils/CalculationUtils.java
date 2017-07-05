package com.aksh.fabcalc.utils;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import com.aksh.fabcalc.R;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aakarshit on 27-06-2017.
 */

public class CalculationUtils {

    private static String TAG = CalculationUtils.class.getSimpleName();

    private static final int DECIMAL_PRECISION = 15;

    private static Function logb;
    private static Operator factorial;
    private static Operator percent;

    // TODO: 26-06-2017 Add factorial, logb, inverse (instead of cube?), percent (replace by '/100')
    static String evaluate(String stringToEvaluate) throws IllegalArgumentException{
        addOperations();
        stringToEvaluate = handlePercent(stringToEvaluate);
        double result = new ExpressionBuilder(stringToEvaluate)
                .operator(factorial)
                .operator(percent)
                .function(logb)
                .build().evaluate();
        return roundString(result);
    }

    private static void addOperations() {
        logb = new Function("logb", 2) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(args[1]);
            }
        };

        factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                final int arg = (int) args[0];
                if ((double) arg != args[0]) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (arg < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                double result = 1;
                for (int i = 1; i <= arg; i++) {
                    result *= i;
                }
                return result;
            }
        };

        percent = new Operator("%", 1, true, Operator.PRECEDENCE_DIVISION + 1) {
            @Override
            public double apply(double... args) {
                double arg = args[0];
                double result = arg/100;
                return result;
            }
        };
    }

    private static String roundString(double value) {
        MathContext mc = new MathContext(DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
        BigDecimal myBigDecimal = new BigDecimal(value, mc);
        if (myBigDecimal.scale() != 0) {
            myBigDecimal =  myBigDecimal.stripTrailingZeros();
        }
        return myBigDecimal.toString();
    }

    static String getEvaluableString(Editable text, Context context) {
        return balanceParens(text.toString()
                .replace(context.getString(R.string.key_plus), "+")
                .replace(context.getString(R.string.key_minus), "-")
                .replace(context.getString(R.string.key_multiply), "*")
                .replace(context.getString(R.string.key_divide), "/")
                .replace(context.getString(R.string.key_degree), "π/180")
                .replace(context.getString(R.string.key_sqrt), "sqrt")
                .replace(context.getString(R.string.key_log_natural), "log")
                .replace(context.getString(R.string.key_log_base_10), "log10")
                .replace("log10b", "logb")
//                .replace(context.getString(R.string.key_percentage), "@")
        );
    }

    private static String balanceParens(String string) {
        int parensToAdd = 0;
        char charArray[] = string.toCharArray();
        for (char c : charArray) {
            if (c == '(') parensToAdd++;
            else if (c == ')') parensToAdd--;
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        while (parensToAdd-- != 0) stringBuilder.append(')');
        string = stringBuilder.toString();
        return string;
    }

    private static String handlePercent(String input) {
        String substituteString = input;
//        if (input.matches("(.)*[+-]([\\d.])+%"))
        Pattern pattern = Pattern.compile("(.*)[+-][\\d.]*%");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            substituteString = matcher.group(1);
            Log.d(TAG, "handlePercent: regex match = " + substituteString);
//            Log.d(TAG, "handlePercent: checking match.end -> " + input.charAt(matcher.end() - 1));
            substituteString = evaluate(substituteString);
            Log.d(TAG, "handlePercent: multiplicand = " + substituteString);
//            substituteString = input.substring(0, matcher.end()) + "*" + substituteString;
            substituteString = substituteString
                    + input.substring(matcher.group(1).length(), matcher.end())
                    + "*" + substituteString
            ;
            Log.d(TAG, "handlePercent: intermediate expression = " + substituteString);
            if (input.length() > matcher.end() + 1) {
                substituteString += input.substring(matcher.end() + 1);
            }
        }
        return substituteString;
    }
}

//    static String getTextWithFunctionParens(String text) {
//        // FIXME: 27-06-2017 Make this reliant on resource?
//        if (Function.isValidFunctionName(text)) {
//            text += "(";
//            if (text.equals("logb(")) {
//                text = "logb( , )";
//            }
//        }
//        return text;
//    }