package com.aksh.fabcalc.Old;

import com.aksh.fabcalc.databinding.BasicCalcKeysBinding;
import com.aksh.fabcalc.utils.MyFab;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class ButtonArrays {

    public static MyFab[] digitButtonsArray;
    public static List<MyFab> digitButtonsList;
    public static MyFab[] inputButtonsArray;
    public static List<MyFab> inputButtonsList;

    public static void initArrays(BasicCalcKeysBinding keys){

//        digitButtonsArray = new MyFab[]{
//                keys.zeroFab,
//                keys.oneFab,
//                keys.twoFab,
//                keys.threeFab,
//                keys.fourFab,
//                keys.fiveFab,
//                keys.sixFab,
//                keys.sevenFab,
//                keys.eightFab,
//                keys.nineFab
//        };
        digitButtonsList = Arrays.asList(digitButtonsArray);
//
//        inputButtonsArray = new MyFab[]{
//                keys.zeroFab,
//                keys.oneFab,
//                keys.twoFab,
//                keys.threeFab,
//                keys.fourFab,
//                keys.fiveFab,
//                keys.sixFab,
//                keys.sevenFab,
//                keys.eightFab,
//                keys.nineFab,
//                keys.pointFab,
//                keys.plusFab,
//                keys.minusFab,
//                keys.multiplyFab,
//                keys.divideFab,
//                keys.parenOpenFab,
//                keys.parenCloseFab,
//        };
        inputButtonsList = Arrays.asList(inputButtonsArray);
    }

}

