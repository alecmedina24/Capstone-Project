package com.alecmedina24.myexercisediary.ExerciseScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.Data.DataLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 4/30/16.
 */
public class ExerciseFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        DataLoader.ExercisesModelCallback {

    @Bind(R.id.overview_list)
    RecyclerView exerciseList;

    private ExerciseModel exerciseModel;
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.exercises_fragment_layout, container, false);

        getData();

        context = getContext();

        ButterKnife.bind(this, rootView);

        exerciseSpinner = (Spinner) rootView.findViewById(R.id.exercise_type);
        exerciseSpinner.setOnItemSelectedListener(this);

        exerciseSelected = false;

        return rootView;
    }

    public void getData() {
        DataLoader dataLoader = new DataLoader(getActivity(), this);
        dataLoader.setAdapterId(2);
        getLoaderManager().initLoader(DataLoader.EXERCISE_LOADER, null, dataLoader);
    }

    public void setNewExerciseAdapter() {
        exerciseList.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), ExerciseAdapter.NUM_COLUMNS));
        exerciseList.setItemAnimator(new SlideInUpAnimator());
        exerciseList.setAdapter(new ExerciseAdapter(exerciseModel.createExerciseList()));
    }

    public void setNewPickerAdapter() {
        exercisePickerList = exerciseModel.createPickerList();
        ArrayAdapter<String> pickerAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, exercisePickerList);

        pickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(pickerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        adapterView = parent;
        if (!exerciseSelected) {
            String exercise = (String) parent.getItemAtPosition(position);
            setExerciseBuffer(exercise);
        } else {
            int index = exerciseModel.getPickerItemIndex(exercisePickerList, exerciseBuffer);
            parent.setSelection(index);
            exerciseSelected = false;
        }
        exerciseModel.setExerciseCheck(exerciseBuffer);
        setNewExerciseAdapter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setExercise(String exercise) {
        setExerciseBuffer(exercise);
        exerciseSelected = true;
        exerciseModel.setExerciseCheck(exerciseBuffer);
        exercisePickerList = exerciseModel.createPickerList();
        setNewPickerAdapter();
        int index = exerciseModel.getPickerItemIndex(exercisePickerList, exerciseBuffer);
        adapterView.setSelection(index);
        setNewExerciseAdapter();
    }

    public void setExerciseBuffer(String exercise) {
        String formattedExercise = exercise.replaceAll("\\s", "");
        exerciseBuffer = new StringBuffer(formattedExercise);
    }

    @Override
    public void setExercisesModel(ExerciseModel exerciseModel) {
        this.exerciseModel = exerciseModel;
        setNewExerciseAdapter();
        setNewPickerAdapter();
    }
}
