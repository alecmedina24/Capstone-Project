package com.xphonesoftware.capstoneproject.OverViewScreen;

import android.content.Context;
import android.database.Cursor;

import com.xphonesoftware.capstoneproject.data.ExerciseContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alecmedina on 4/30/16.
 */
public class OverViewModel {

    private StringBuffer exerciseCheck;
    private String weight;
    private String reps;
    private long date;
    private Cursor cursor;
    private int exerciseCount;
    private Context context;

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

    public OverViewModel(Context context) {
        this.context = context;
        cursor = this.context.getContentResolver().
                query(ExerciseContract.ExerciseEntry.CONTENT_URI, EXERCISE_PROJECTION, null, null, null);
        exerciseCount = cursor.getCount() - 1;
    }

    public OverViewModel(long date, String weight, String reps) {
        this.date = date;
        this.weight = weight;
        this.reps = reps;
    }

    public long getDate() {
        return date;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }

    public void setExerciseCheck(StringBuffer check) {
        exerciseCheck = check;
    }

    public ArrayList<OverViewModel> createExerciseList() {
        ArrayList<OverViewModel> exercises = new ArrayList<>();
        long date;
        String weight;
        String reps;

        cursor = context.getContentResolver().
                query(ExerciseContract.ExerciseEntry.CONTENT_URI, EXERCISE_PROJECTION, null, null, null);
        exerciseCount = cursor.getCount() - 1;

        for (int i = exerciseCount; i >= 0; i--) {
            cursor.moveToPosition(i);
            String exercise = cursor.getString(INDEX_EXERCISE);
            if (!exerciseCheck.equals("-1")) {
                String formattedExercise = exercise.replaceAll("\\s", "");
                if (formattedExercise.contentEquals(exerciseCheck)) {
                    date = cursor.getLong(INDEX_DATE);
                    weight = cursor.getString(INDEX_WEIGHT);
                    reps = cursor.getString(INDEX_REPS);
                    exercises.add(new OverViewModel(date, weight, reps));
                }
            }
        }

        return exercises;
    }

    public ArrayList<String> createPickerList() {
        ArrayList<String> pickerList = new ArrayList<>();
        ArrayList<String> checkList = new ArrayList<>();
        boolean onList = true;

        pickerList.add("Select Exercise");
        checkList.add("selectexercise");

        for (int i = exerciseCount; i >= 0; i--) {
            cursor.moveToPosition(i);
            String exercise = cursor.getString(INDEX_EXERCISE);
            String formattedExercise = exercise.replaceAll("\\s", "");

            if (checkList.size() == 1) {
                checkList.add(formattedExercise);
                pickerList.add(exercise);
            } else {
                for (int j = 0; j < checkList.size(); j++) {
                    if (formattedExercise.equals(checkList.get(j))) {
                        onList = true;
                        break;
                    } else {
                        onList = false;
                    }
                }
            }

            if (!onList) {
                pickerList.add(exercise);
                checkList.add(formattedExercise);
            }
        }

        return pickerList;
    }

    public int getPickerItemIndex(ArrayList<String> list, StringBuffer buffer) {

        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            String formattedListItem = list.get(i).replaceAll("\\s", "");
            if (formattedListItem.equals(buffer.toString())) {
                index = i;
            }
        }

        return index;
    }

    public String formatDate(long date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        String day = format.format(calendar.getTime());
        return day;
    }
}
