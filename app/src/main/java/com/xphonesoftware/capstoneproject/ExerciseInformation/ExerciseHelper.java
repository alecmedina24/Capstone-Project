package com.xphonesoftware.capstoneproject.ExerciseInformation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alecmedina on 4/27/16.
 */
public class ExerciseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseContract.FeedEntry.TABLE_NAME + " (" +
                    ExerciseContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    ExerciseContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    ExerciseContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseContract.FeedEntry.TABLE_NAME;

    public static final String DATABASE_NAME = "FeedReader.db";
    public static final int DATABASE_VERSION = 1;

    public ExerciseHelper(Context context) {
        super (context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
