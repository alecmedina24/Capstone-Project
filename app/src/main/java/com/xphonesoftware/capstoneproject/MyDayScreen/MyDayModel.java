package com.xphonesoftware.capstoneproject.MyDayScreen;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alecmedina on 4/29/16.
 */
public class MyDayModel {
    private String exercise;
    private String weight;
    private String reps;
    private int id;
    private long date;
    private Cursor cursor;
    private int exerciseCount;

    private static final int INDEX_DATE = 0;
    private static final int INDEX_EXERCISE = 1;
    private static final int INDEX_WEIGHT = 2;
    private static final int INDEX_REPS = 3;

    public MyDayModel(Cursor cursor) {
        date = System.currentTimeMillis();
        this.cursor = cursor;
        exerciseCount = cursor.getCount() - 1;
    }

    public MyDayModel(String exercise, String weight, String reps) {
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
//        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getExercise() {
        return exercise;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }

    public int getId() {
        return id;
    }

    public ArrayList<MyDayModel> createExercisesList() {
        ArrayList<MyDayModel> exercises = new ArrayList<>();
        String exerciseName;
        String weightValue;
        String repValue;
        int id;
        long itemDate;

        for (int i = exerciseCount; i >= 0; i--) {
            cursor.moveToPosition(i);
            itemDate = (cursor.getLong(INDEX_DATE));
            if (formatDate(date).equals(formatDate(itemDate))) {
                exerciseName = (cursor.getString(INDEX_EXERCISE));
                weightValue = (cursor.getString(INDEX_WEIGHT));
                repValue = (cursor.getString(INDEX_REPS));
                exercises.add(new MyDayModel(exerciseName, weightValue, repValue));
            }
        }
        return exercises;
    }

    public String formatDate(long date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        String day = format.format(calendar.getTime());
        return day;
    }
}
