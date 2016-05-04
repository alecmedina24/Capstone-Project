package com.xphonesoftware.capstoneproject.DetailsScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xphonesoftware.capstoneproject.R;

import java.util.List;

/**
 * Created by alecmedina on 4/29/16.
 */
public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    public static final int NUM_COLUMNS = 3;

    private List<ExerciseModel> exercises;
    private SetExercise setExercise;


    public ExercisesAdapter(List<ExerciseModel> exercises, Context context) {
        this.exercises = exercises;
        setExercise = (SetExercise) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

//        View exerciseView = inflater.inflate(R.layout.list_item_layout, parent, false);
        View exerciseView = inflater.inflate(R.layout.my_day_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(exerciseView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExercisesAdapter.ViewHolder holder, int position) {
        int row = position / NUM_COLUMNS;
        int col = position % NUM_COLUMNS;

        final ExerciseModel exercise = exercises.get(row);

        TextView textView = holder.testView;

        switch (col) {
            case 0:
                textView.setText(exercise.getExercise());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setExercise.setExercise(exercise.getExercise());
                    }
                });
                break;
            case 1:
                textView.setText(exercise.getWeight());
                break;
            case 2:
                textView.setText(exercise.getReps());
                break;
        }
//        TextView exerciseText = holder.exerciseValue;
//        exerciseText.setText(exercise.getExercise());
//        exerciseText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setExercise.setExercise(exercise.getExercise());
//            }
//        });
//
//        TextView weightText = holder.weightValue;
//        weightText.setText(exercise.getWeight());
//
//        TextView repText = holder.repValue;
//        repText.setText(exercise.getReps());


    }

    @Override
    public int getItemCount() {
        return exercises.size() * NUM_COLUMNS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        TextView exerciseValue;
//        TextView weightValue;
//        TextView repValue;
        TextView testView;

        public ViewHolder(View itemView) {
            super(itemView);
//            exerciseValue = (TextView) itemView.findViewById(R.id.exercise_value_item);
//            weightValue = (TextView) itemView.findViewById(R.id.weight_value_item);
//            repValue = (TextView) itemView.findViewById(R.id.rep_value_item);
            testView = (TextView) itemView.findViewById(R.id.test_item);
        }
    }

    public interface SetExercise {
        void setExercise(String exercise);
    }
}
