package com.aksh.fabcalc.history;

import android.content.ContentValues;
import android.content.Context;

import com.aksh.fabcalc.utils.CalcDateUtils;

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

    public static void insertRecord(String expression, String result, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryContract.COLUMN_EXPRESSION, expression);
        contentValues.put(HistoryContract.COLUMN_RESULT, result);
        contentValues.put(HistoryContract.COLUMN_DATE, CalcDateUtils.getDate());
        context.getContentResolver().insert(
                HistoryProvider.History.CONTENT_URI,
                contentValues
        );
    }

    public static void clearAllRecords(Context context) {
        context.getContentResolver().delete(
                HistoryProvider.History.CONTENT_URI,
                null,
                null);
    }
}
