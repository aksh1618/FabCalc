package com.aksh.fabcalc.history;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Aakarshit on 08-07-2017.
 */

public class HistoryContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_EXPRESSION = "expression";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RESULT = "result";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";
}
