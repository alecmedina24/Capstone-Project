package com.xphonesoftware.capstoneproject.DetailsScreen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xphonesoftware.capstoneproject.OverViewActivity;
import com.xphonesoftware.capstoneproject.R;

import java.util.List;

/**
 * Created by alecmedina on 4/29/16.
 */
public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    private List<ExerciseModel> exercises;
    private Context context;

    public ExercisesAdapter(List<ExerciseModel> exercises, Context context) {
        this.exercises = exercises;
        this.context = context;
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
    public void onBindViewHolder(final ExercisesAdapter.ViewHolder holder, int position) {
        final ExerciseModel exercise = exercises.get(position);

        TextView exerciseText = holder.exerciseValue;
        exerciseText.setText(exercise.getExercise());
        exerciseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OverViewActivity.class);
                intent.putExtra("exercise", exercise.getExercise());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_SEND);
                context.startActivity(intent);
            }
        });

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

        TextView exerciseValue;
        TextView weightValue;
        TextView repValue;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseValue = (TextView) itemView.findViewById(R.id.exercise_value_item);
            weightValue = (TextView) itemView.findViewById(R.id.weight_value_item);
            repValue = (TextView) itemView.findViewById(R.id.rep_value_item);
        }
    }
}
