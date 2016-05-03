package com.xphonesoftware.capstoneproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class DetailFragment extends Fragment {

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

    private static final long ONE_DAY = 86400000;

    private ExerciseModel exerciseModel;
    private long day;
    private long today;
    private long yesterday;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.detail_layout, container, false);

        context = getContext();

        ButterKnife.bind(this, rootView);

        exerciseModel = new ExerciseModel(context);

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

        today = System.currentTimeMillis();
        yesterday = System.currentTimeMillis() - ONE_DAY;

        return rootView;
    }

    public void setNewAdapter() {
        exerciseList.setAdapter(new ExercisesAdapter(exerciseModel.createExercisesList(), context));
        exerciseList.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
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
