package com.aksh.fabcalc.utils;

/**
 * Created by Aakarshit on 15-07-2017.
 */

public class ThemeUtils {
    // TODO: 16-07-2017 Separate theme from colors?
    enum Theme {
        BLACK_AND_WHITE,
        FLAT,
        IMAGE,
        DEFAULT
    }

    public static void applyTheme(Theme finalTheme) {
        switch (finalTheme) {
            case BLACK_AND_WHITE: applyBlackAndWhiteTheme(); break;
            case FLAT: applyFlatTheme(); break;
            case IMAGE: applyImageTheme(); break;
            case DEFAULT: applyDefaultTheme(); break;
        }
    }

    private static void applyDefaultTheme() {
        // TODO: 15-07-2017 4 options (Listpreference?):
        // key background (primary?),
        // display (secondary?),
        // keys (3 options: white, secondary, custom),
        // text (2 options: primary, custom);
        // separate display text color and keys text color?
    }

    private static void applyImageTheme() {

    }

    private static void applyFlatTheme() {
        // TODO: 15-07-2017 3 options : pure black, dark, custom.
    }

    private static void applyBlackAndWhiteTheme() {

    }
}
