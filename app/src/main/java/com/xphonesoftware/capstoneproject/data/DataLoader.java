package com.xphonesoftware.capstoneproject.data;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.xphonesoftware.capstoneproject.ExerciseScreen.ExerciseFragment;
import com.xphonesoftware.capstoneproject.ExerciseScreen.ExerciseModel;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayFragment;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayModel;

/**
 * Created by alecmedina on 5/6/16.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXERCISE_LOADER = 0;
    private static final String[] EXERCISE_PROJECTION = new String[]{
            ExerciseContract.ExerciseEntry.COLUMN_DATE,
            ExerciseContract.ExerciseEntry.COLUMN_EXERCISE,
            ExerciseContract.ExerciseEntry.COLUMN_WEIGHT,
            ExerciseContract.ExerciseEntry.COLUMN_REPS
    };

    private static final int INDEX_DATE = 0;
    private static final int INDEX_EXERCISE = 1;
    private static final int INDEX_WEIGHT = 2;
    private static final int INDEX_REPS = 3;

    private Activity activity;
    private ExerciseModel exerciseModel;
    private MyDayModel myDayModel;
    private int adapterId;
    private MyDayModelCallback myDayModelCallback;
    private ExercisesModelCallback exerciseModelCallback;

    public interface MyDayModelCallback {
        void setMyDayModel(MyDayModel myDayModel);
    }

    public interface ExercisesModelCallback {
        void setExercisesModel(ExerciseModel exerciseModel);
    }

    public DataLoader(Activity activity, MyDayFragment fragment) {
        this.activity = activity;
        myDayModelCallback = fragment;
    }

    public DataLoader(Activity activity, ExerciseFragment fragment){
        this.activity = activity;
        exerciseModelCallback = fragment;
    }

    public void setAdapterId(int id) {
        adapterId = id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case EXERCISE_LOADER:
                return new CursorLoader(activity, ExerciseContract.ExerciseEntry.CONTENT_URI,
                        EXERCISE_PROJECTION, null, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapterId == 1 ) {
            setMyDayModel(data);
        } else if (adapterId == 2) {
            setExerciseModel(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setMyDayModel(Cursor cursor) {
        myDayModel = new MyDayModel(cursor);
        myDayModelCallback.setMyDayModel(myDayModel);
    }

    public void setExerciseModel(Cursor cursor) {
        exerciseModel = new ExerciseModel(cursor);
        exerciseModelCallback.setExercisesModel(exerciseModel);
    }
}