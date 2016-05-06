package com.xphonesoftware.capstoneproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by alecmedina on 4/28/16.
 */
public class ExerciseProvider extends ContentProvider {

    private ExerciseDbHelper dbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final int EXERCISES = 100;
    private static final String UNKNOWN_URI = "Unknown uri: ";
    private static final String INSERT_FAILED = "Failed to insert row into ";

    @Override
    public boolean onCreate() {
        dbHelper = new ExerciseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case EXERCISES:
                cursor = dbHelper.getReadableDatabase().query(ExerciseContract.ExerciseEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case EXERCISES:
                return ExerciseContract.ExerciseEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case EXERCISES:
                long _id = db.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ExerciseContract.ExerciseEntry.buildExerciseUri(_id);
                } else {
                    throw new android.database.SQLException(INSERT_FAILED + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case EXERCISES:
                rowsUpdated = db.update(ExerciseContract.ExerciseEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ExerciseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ExerciseContract.PATH_EXERCISES, EXERCISES);
        return matcher;
    }
}
