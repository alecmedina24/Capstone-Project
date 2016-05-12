package com.xphonesoftware.capstoneproject.ExerciseScreen;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alecmedina on 4/30/16.
 */
public class ExerciseModel {

    private StringBuffer exerciseCheck;
    private String weight;
    private String reps;
    private long date;
    private Cursor cursor;
    private int exerciseCount;

    private static final long INDEX_ID = 0;
    private static final int INDEX_DATE = 1;
    private static final int INDEX_EXERCISE = 2;
    private static final int INDEX_WEIGHT = 3;
    private static final int INDEX_REPS = 4;

    public ExerciseModel(Cursor cursor) {
        this.cursor = cursor;
        exerciseCount = cursor.getCount() - 1;
        exerciseCheck = new StringBuffer("-1");
    }

    public ExerciseModel(long date, String weight, String reps) {
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

    public ArrayList<ExerciseModel> createExerciseList() {
        ArrayList<ExerciseModel> exercises = new ArrayList<>();
        long date;
        String weight;
        String reps;

        for (int i = exerciseCount; i >= 0; i--) {
            cursor.moveToPosition(i);
            String exercise = cursor.getString(INDEX_EXERCISE);
            if (!exerciseCheck.equals("-1")) {
                String formattedExercise = exercise.replaceAll("\\s", "");
                if (formattedExercise.contentEquals(exerciseCheck)) {
                    date = cursor.getLong(INDEX_DATE);
                    weight = cursor.getString(INDEX_WEIGHT);
                    reps = cursor.getString(INDEX_REPS);
                    exercises.add(new ExerciseModel(date, weight, reps));
                }
            }
        }

        return exercises;
    }

    //Creates list for spinner in ExercisesFragment
    public ArrayList<String> createPickerList() {
        ArrayList<String> pickerList = new ArrayList<>();
        ArrayList<String> checkList = new ArrayList<>();
        boolean onList = true;

        //This is the first position in the spinner, values in spinner are parsed to make sure
        //exercises are not repeated as each one represents a category
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

    //Gets index of spinner item
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
