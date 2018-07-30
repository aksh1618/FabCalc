package com.aksh.fabcalc.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.Editable;

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

//    private static String TAG = CalculationUtils.class.getSimpleName();

    private static final int DECIMAL_PRECISION = 15;
    private static final double SCALE = 1e15;

    private static Function logb;
    private static Operator factorial;
    private static Operator percent;
    private static Function[] trigonometricFunctions;
    private static String angleUnit;

    // TODO: 26-06-2017 Add inverse (instead of cube?)
    static String evaluate(String stringToEvaluate) throws IllegalArgumentException{
        addOperations();
        stringToEvaluate = handlePercent(stringToEvaluate);
        double result = new ExpressionBuilder(stringToEvaluate)
                .operator(factorial)
                .operator(percent)
                .function(logb)
                .functions(trigonometricFunctions)
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
                return arg/100;
            }
        };

        trigonometricFunctions = new Function[]{
                new Function("sin"){
                    public double apply(double... args){
                        return getTrigonometricResult("sin", args[0]);
                    }
                },
                new Function("cos"){
                    public double apply(double... args){
                        return getTrigonometricResult("cos", args[0]);
                    }
                },
                new Function("tan"){
                    public double apply(double... args){
                        // TODO: 03-08-2017 Use Cleaner Method?
                        double result = getTrigonometricResult("tan", args[0]);
//                        Log.d(TAG, "apply: " + result);
                        if (result == 9223.372036854777) {
                            throw new IllegalArgumentException("Infinity!!");
                        }
                        return result;
                    }
                },
                new Function("asin"){
                    public double apply(double... args){
                        return getInverseTrigonometricResult("asin", args[0]);
                    }
                },
                new Function("acos"){
                    public double apply(double... args){
                        return getInverseTrigonometricResult("acos", args[0]);
                    }
                },
                new Function("atan"){
                    public double apply(double... args){
                        return getInverseTrigonometricResult("atan", args[0]);
                    }
                }
        };
    }

    private static double getTrigonometricResult(String function, double angle) {
        if (angleUnit.equals("degrees")) {
            angle = Math.toRadians(angle);
        }
        double result = 0;
        switch (function) {
            case "sin": result = Math.sin(angle); break;
            case "cos": result = Math.cos(angle); break;
            case "tan": result = Math.tan(angle); break;
        }
        return Math.round(result * SCALE) / SCALE;
    }

    private static double getInverseTrigonometricResult(String function, double param) {
        double angle = 0;
        switch (function) {
            case "asin": angle = Math.asin(param); break;
            case "acos": angle = Math.acos(param); break;
            case "atan": angle = Math.atan(param); break;
        }
        if (angleUnit.equals("degrees")) {
            angle = Math.toDegrees(angle);
        }
        return angle;
    }

    public static void setAngleUnit(Context context) {
        boolean useDegrees = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_degrees_key),
                context.getResources().getBoolean(R.bool.pref_degrees_default)
        );
        if (useDegrees) {
            angleUnit = "degrees";
        } else {
            angleUnit = "radians";
        }
    }

    private static String roundString(double value) {
        MathContext mc = new MathContext(DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
        BigDecimal myBigDecimal = new BigDecimal(value, mc);
        if (myBigDecimal.scale() != 0) {
            myBigDecimal =  myBigDecimal.stripTrailingZeros();
        }
        return myBigDecimal.toString();
    }

    public static String getEvaluableString(Editable text, Context context) {
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
        );
    }

    private static String balanceParens(String string) {
        int parensToAdd = 0;
        char charArray[] = string.toCharArray();
        for (char c : charArray) {
            if (c == '(') parensToAdd++;
            else if (c == ')') parensToAdd--;
        }
        if (parensToAdd == 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        while (parensToAdd-- != 0) stringBuilder.append(')');
        string = stringBuilder.toString();
        return string;
    }

    private static String handlePercent(String input) {
        String substituteString = input;
        Pattern pattern = Pattern.compile("(.*)[+-][\\d.]*%");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            substituteString = matcher.group(1);
            substituteString = evaluate(substituteString);
            substituteString = substituteString
                    + input.substring(matcher.group(1).length(), matcher.end())
                    + "*" + substituteString
            ;
            if (input.length() > matcher.end() + 1) {
                substituteString += input.substring(matcher.end() + 1);
            }
        }
        return substituteString;
    }
}