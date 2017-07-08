package com.aksh.fabcalc.history;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Aakarshit on 08-07-2017.
 */

@Database(version = HistoryDatabase.VERSION)
public class HistoryDatabase {

    public static final int VERSION = 1;

    @Table(HistoryContract.class)
    public static final String HISTORY = "calculation_history";
}
