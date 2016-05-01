package com.xphonesoftware.capstoneproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xphonesoftware.capstoneproject.OverViewScreen.OverViewAdapter;
import com.xphonesoftware.capstoneproject.OverViewScreen.OverViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 4/30/16.
 */
public class OverViewActivity extends AppCompatActivity {

    @Bind(R.id.overview_list)
    RecyclerView overViewList;

    private OverViewModel overViewModel;
    private String exercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_overview_layout);
        ButterKnife.bind(this);

        overViewModel = new OverViewModel(this);

        exercise = getIntent().getStringExtra("exercise");

        if (exercise != null) {
            String formattedExercise = exercise.replaceAll("\\s", "");
            StringBuffer exerciseBuffer = new StringBuffer(formattedExercise);
            overViewModel.setExerciseCheck(exerciseBuffer);
        } else {
            overViewModel.setExerciseCheck(new StringBuffer("-1"));
        }

        setNewAdapter();
    }

    public void setNewAdapter() {
        overViewList.setAdapter(new OverViewAdapter(overViewModel.createExerciseList()));
        overViewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        overViewList.setItemAnimator(new SlideInUpAnimator());
    }
}
