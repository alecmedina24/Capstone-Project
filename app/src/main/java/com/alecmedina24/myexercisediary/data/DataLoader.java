package com.alecmedina24.myexercisediary.data;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.alecmedina24.myexercisediary.ExerciseScreen.ExerciseFragment;
import com.alecmedina24.myexercisediary.ExerciseScreen.ExerciseModel;
import com.alecmedina24.myexercisediary.MyDayScreen.MyDayFragment;
import com.alecmedina24.myexercisediary.MyDayScreen.MyDayModel;
import com.alecmedina24.myexercisediary.WeightScreen.WeightFragment;
import com.alecmedina24.myexercisediary.WeightScreen.WeightModel;

/**
 * Created by alecmedina on 5/6/16.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXERCISE_LOADER = 0;
    public static final int WEIGHT_LOADER = 1;
    private static final String[] EXERCISE_PROJECTION = new String[]{
            ExerciseContract.ExerciseEntry._ID,
            ExerciseContract.ExerciseEntry.COLUMN_DATE,
            ExerciseContract.ExerciseEntry.COLUMN_EXERCISE,
            ExerciseContract.ExerciseEntry.COLUMN_WEIGHT,
            ExerciseContract.ExerciseEntry.COLUMN_REPS
    };
    private static final String[] WEIGHT_PROJECTION = new String[] {
            WeightContract.WeightEntry.COLUMN_DATE,
            WeightContract.WeightEntry.COLUMN_WEIGHT
    };

    private Activity activity;
    private ExerciseModel exerciseModel;
    private MyDayModel myDayModel;
    private WeightModel weightModel;
    private int adapterId;
    private MyDayModelCallback myDayModelCallback;
    private ExercisesModelCallback exerciseModelCallback;
    private WeightModelCallback weightModelCallback;

    //Set of callback interfaces and constructors that supplies the information to be used
    //in each fragment
    public interface MyDayModelCallback {
        void setMyDayModel(MyDayModel myDayModel);
    }

    public interface ExercisesModelCallback {
        void setExercisesModel(ExerciseModel exerciseModel);
    }

    public interface WeightModelCallback {
        void setWeightModel(WeightModel weightModel);
    }

    public DataLoader(Activity activity, MyDayFragment fragment) {
        this.activity = activity;
        myDayModelCallback = fragment;
    }

    public DataLoader(Activity activity, ExerciseFragment fragment){
        this.activity = activity;
        exerciseModelCallback = fragment;
    }

    public DataLoader(Activity activity, WeightFragment fragment) {
        this.activity = activity;
        weightModelCallback = fragment;
    }

    //Each time data is called to be loaded the fragment identifies which model to fill
    //out with this id
    public void setAdapterId(int id) {
        adapterId = id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case EXERCISE_LOADER:
                return new CursorLoader(activity, ExerciseContract.ExerciseEntry.CONTENT_URI,
                        EXERCISE_PROJECTION, null, null, null);
            case WEIGHT_LOADER:
                return new CursorLoader(activity, WeightContract.WeightEntry.CONTENT_URI,
                        WEIGHT_PROJECTION, null, null, null);
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
        } else {
            setWeightModel(data);
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

    public void setWeightModel(Cursor cursor) {
        weightModel = new WeightModel(cursor);
        weightModelCallback.setWeightModel(weightModel);
    }
}
