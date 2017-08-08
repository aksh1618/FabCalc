package com.aksh.fabcalc.history;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Aakarshit on 08-07-2017.
 */

@ContentProvider(authority = HistoryProvider.AUTHORITY, database = HistoryDatabase.class)
public final class HistoryProvider {

    static final String AUTHORITY = "com.aksh.fabcalc.history.HistoryProvider";

    @TableEndpoint(table = HistoryDatabase.HISTORY)
    public static class History {
        @ContentUri(
                path = "history",
                type = "vnd.android.cursor.dir/history",
                defaultSort = HistoryContract.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/history");
    }
}
