package com.alecmedina24.myexercisediary.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "weights.db";

    public WeightDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + WeightContract.WeightEntry.TABLE_NAME + " (" +
                WeightContract.WeightEntry._ID + " INTEGER PRIMARY KEY," +
                WeightContract.WeightEntry.COLUMN_DATE + " TEXT, " +
                WeightContract.WeightEntry.COLUMN_WEIGHT + " TEXT " +
                ");";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeightContract.WeightEntry.TABLE_NAME);
        onCreate(db);
    }
}
