package com.xphonesoftware.capstoneproject.ExerciseInformation;

import android.provider.BaseColumns;

/**
 * Created by alecmedina on 4/27/16.
 */
public class ExerciseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ExerciseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
