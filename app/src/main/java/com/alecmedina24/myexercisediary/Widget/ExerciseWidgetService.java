package com.alecmedina24.myexercisediary.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alecmedina24.myexercisediary.MyDayScreen.MyDayModel;
import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.Data.ExerciseContract;

import java.util.ArrayList;

/**
 * Created by alecmedina on 5/5/16.
 */
public class ExerciseWidgetService extends RemoteViewsService {

    private final String[] EXERCISE_PROJECTION = new String[]{
            ExerciseContract.ExerciseEntry.COLUMN_DATE,
            ExerciseContract.ExerciseEntry.COLUMN_EXERCISE,
            ExerciseContract.ExerciseEntry.COLUMN_WEIGHT,
            ExerciseContract.ExerciseEntry.COLUMN_REPS
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private Cursor cursor;
        private ArrayList<MyDayModel> exercises;


        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            queryDatabase();
        }

        @Override
        public void onCreate() {
            cursor = queryDatabase();
        }

        @Override
        public void onDataSetChanged() {
            cursor = queryDatabase();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (cursor == null) {
                return 0;
            }
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            final MyDayModel exercise = exercises.get(position);

            remoteViews.setTextViewText(R.id.widget_exercise, "Exercise: " + exercise.getExercise());
            remoteViews.setTextViewText(R.id.widget_weight,"Weight: " + exercise.getWeight());
            remoteViews.setTextViewText(R.id.widget_reps,"Reps: " + exercise.getReps());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private Cursor queryDatabase() {
            Cursor cursor = getContentResolver().
                    query(ExerciseContract.ExerciseEntry.CONTENT_URI, EXERCISE_PROJECTION, null, null, null);
            exercises = new MyDayModel(cursor).createExercisesList();
            return cursor;
        }
    }
}
