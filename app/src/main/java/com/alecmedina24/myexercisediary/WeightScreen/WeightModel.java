package com.alecmedina24.myexercisediary.WeightScreen;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightModel {

    private String weight;
    private long date;
    private Cursor cursor;
    private int weightsCount;

    private static final int WEIGHT_INDEX = 1;
    private static final int DATE_INDEX = 0;

    public WeightModel(Cursor cursor) {
        this.cursor = cursor;
        date = System.currentTimeMillis();
        weightsCount = cursor.getCount() - 1;
    }

    public WeightModel(String weight, long date) {
        this.weight = weight;
        this.date = date;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public long getDate() {
        return date;
    }

    public ArrayList<WeightModel> createWeightsList() {
        ArrayList<WeightModel> weightsList = new ArrayList<>();
        String weight;
        long date;

        for (int i = weightsCount; i >= 0; i--) {
            cursor.moveToPosition(i);
            weight = cursor.getString(WEIGHT_INDEX);
            date = cursor.getLong(DATE_INDEX);
            weightsList.add(new WeightModel(weight, date));
        }

        return weightsList;
    }
}
