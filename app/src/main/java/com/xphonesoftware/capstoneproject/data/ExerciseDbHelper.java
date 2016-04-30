package com.xphonesoftware.capstoneproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alecmedina on 4/27/16.
 */
public class ExerciseDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "exercises.db";

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + ExerciseContract.ExerciseEntry.TABLE_NAME + " (" +
                ExerciseContract.ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                ExerciseContract.ExerciseEntry.COLUMN_DATE + " TEXT, " +
                ExerciseContract.ExerciseEntry.COLUMN_EXERCISE + " TEXT, " +
                ExerciseContract.ExerciseEntry.COLUMN_WEIGHT + " TEXT, " +
                ExerciseContract.ExerciseEntry.COLUMN_REPS + " TEXT " +
                ");";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseContract.ExerciseEntry.TABLE_NAME);
        onCreate(db);
    }
}
