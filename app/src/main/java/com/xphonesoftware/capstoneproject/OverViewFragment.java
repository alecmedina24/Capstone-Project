package com.xphonesoftware.capstoneproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.xphonesoftware.capstoneproject.OverViewScreen.OverViewAdapter;
import com.xphonesoftware.capstoneproject.OverViewScreen.OverViewModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 4/30/16.
 */
public class OverViewFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.overview_list)
    RecyclerView overViewList;

    private OverViewModel overViewModel;
    private Spinner exerciseSpinner;
    private StringBuffer exerciseBuffer;
    private boolean exerciseSelected;
    private ArrayList<String> exercisePickerList;
    private Context context;
    private AdapterView<?> adapterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.exercise_overview_layout, container, false);

        context = getContext();

        ButterKnife.bind(this, rootView);

        overViewModel = new OverViewModel(context);

        exerciseSpinner = (Spinner) rootView.findViewById(R.id.exercise_type);
        exerciseSpinner.setOnItemSelectedListener(this);

        setNewPickerAdapter();

//        exercise = getIntent().getStringExtra("exercise");

//        if (exercise != null) {
//            String formattedExercise = exercise.replaceAll("\\s", "");
//            exerciseBuffer = new StringBuffer(formattedExercise);
//            exerciseSelected = true;
//            overViewModel.setExerciseCheck(exerciseBuffer);
//        } else {
            exerciseSelected = false;
            overViewModel.setExerciseCheck(new StringBuffer("-1"));
//        }

        exercisePickerList = overViewModel.createPickerList();

        setNewExerciseAdapter();

        return rootView;
    }

    public void setNewExerciseAdapter() {
        overViewList.setAdapter(new OverViewAdapter(overViewModel.createExerciseList()));
        overViewList.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        overViewList.setItemAnimator(new SlideInUpAnimator());
    }

    public void setNewPickerAdapter() {
        ArrayAdapter<String> pickerAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, overViewModel.createPickerList());

        pickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(pickerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        adapterView = parent;
        if (!exerciseSelected) {
            String exercise = (String) parent.getItemAtPosition(position);
            setExerciseBuffer(exercise);
        }else {
            int index = overViewModel.getPickerItemIndex(exercisePickerList, exerciseBuffer);
            parent.setSelection(index);
            exerciseSelected = false;
        }
        overViewModel.setExerciseCheck(exerciseBuffer);
        setNewExerciseAdapter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setOverViewExercise(String exercise) {
        setExerciseBuffer(exercise);
        exerciseSelected = true;
        overViewModel.setExerciseCheck(exerciseBuffer);
        int index = overViewModel.getPickerItemIndex(exercisePickerList, exerciseBuffer);
        adapterView.setSelection(index);
        setNewExerciseAdapter();
    }

    public void setExerciseBuffer(String exercise) {
        String formattedExercise = exercise.replaceAll("\\s", "");
        exerciseBuffer = new StringBuffer(formattedExercise);
    }
}
