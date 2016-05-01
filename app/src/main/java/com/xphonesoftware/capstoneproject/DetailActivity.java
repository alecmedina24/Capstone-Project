package com.xphonesoftware.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.xphonesoftware.capstoneproject.DetailsScreen.ExerciseModel;
import com.xphonesoftware.capstoneproject.DetailsScreen.ExercisesAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 4/29/16.
 */
public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.list_layout)
    RecyclerView exerciseList;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.day_listed)
    TextView dayListedView;
    @Bind(R.id.next_day)
    Button nextDayButton;
    @Bind(R.id.previous_day)
    Button previousDayButton;

    //TODO: REMOVE IN FUTURE
    @Bind(R.id.overview_activity)
    Button overViewActivity;

    private static final long ONE_DAY = 86400000;

    private ExerciseModel exerciseModel;
    private long day;
    private long today;
    private long yesterday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        ButterKnife.bind(this);

        exerciseModel = new ExerciseModel(this);
        setNewAdapter();

        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        day = System.currentTimeMillis();

        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = day - ONE_DAY;
                exerciseModel.setDate(day);
                setDayHeader();
                setNewAdapter();
            }
        });

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = day + ONE_DAY;
                exerciseModel.setDate(day);
                setDayHeader();
                setNewAdapter();
            }
        });

        //TODO: REMOVE THIS IN FUTURE
        overViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OverViewActivity.class);
                startActivity(intent);
            }
        });

        today = System.currentTimeMillis();
        yesterday = System.currentTimeMillis() - ONE_DAY;

    }

    public void setNewAdapter() {
        exerciseList.setAdapter(new ExercisesAdapter(exerciseModel.createExercisesList(), getApplicationContext()));
        exerciseList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        exerciseList.setItemAnimator(new SlideInUpAnimator());
    }

    public void setDayHeader() {
        if (exerciseModel.formatDate(day).equals(exerciseModel.formatDate(today))) {
            dayListedView.setText("TODAY");
        } else if (exerciseModel.formatDate(day).equals(exerciseModel.formatDate(yesterday))) {
            dayListedView.setText("YESTERDAY");
        } else {
            dayListedView.setText(exerciseModel.formatDate(day));
        }
    }
}
