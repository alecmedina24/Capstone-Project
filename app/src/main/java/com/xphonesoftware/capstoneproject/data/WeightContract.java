package com.xphonesoftware.capstoneproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightContract {
    public static final String CONTENT_AUTHORITY = "com.xphonesoftware.capstoneproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEIGHT = "weight";

    public static final class WeightEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEIGHT).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_WEIGHT;
        public static final String TABLE_NAME = "weights";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEIGHT = "weight";

        public static Uri buildExerciseUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
