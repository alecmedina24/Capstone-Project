package com.xphonesoftware.capstoneproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alecmedina on 4/27/16.
 */
public class ExerciseContract {
    public static final String CONTENT_AUTHORITY = "com.xphonesoftware.capstoneproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_EXERCISES = "exercises";

    /* Inner class that defines the table contents */
    public static final class ExerciseEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_EXERCISES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_EXERCISES;
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_EXERCISE = "exercise";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_REPS = "reps";

        public static Uri buildExerciseUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
