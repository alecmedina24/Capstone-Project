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

    private List<ExerciseModel> exercises;

    public ExercisesAdapter(List<ExerciseModel> exercises) {
        this.exercises = exercises;
    }


    @Override
    public ExercisesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseView = inflater.inflate(R.layout.list_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(exerciseView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExercisesAdapter.ViewHolder holder, int position) {
        ExerciseModel exercise = exercises.get(position);

        TextView exerciseText = holder.exerciseValue;
        exerciseText.setText(exercise.getExercise());

        TextView weightText = holder.weightValue;
        weightText.setText(exercise.getWeight());

        TextView repText = holder.repValue;
        repText.setText(exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.exercise_value_item)
        TextView exerciseValue;
//        @Bind(R.id.weight_value_item)
        TextView weightValue;
//        @Bind(R.id.rep_value_item)
        TextView repValue;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(itemView);
            exerciseValue = (TextView) itemView.findViewById(R.id.exercise_value_item);
            weightValue = (TextView) itemView.findViewById(R.id.weight_value_item);
            repValue = (TextView) itemView.findViewById(R.id.rep_value_item);
        }
    }
}
