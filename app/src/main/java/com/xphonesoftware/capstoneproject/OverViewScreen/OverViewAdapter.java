package com.xphonesoftware.capstoneproject.OverViewScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xphonesoftware.capstoneproject.R;

import java.util.List;

/**
 * Created by alecmedina on 4/30/16.
 */
public class OverViewAdapter extends RecyclerView.Adapter<OverViewAdapter.ViewHolder> {

    private List<OverViewModel> exercises;

    public OverViewAdapter(List<OverViewModel> exercises) {
        this.exercises = exercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseView = inflater.inflate(R.layout.exercise_overview_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(exerciseView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OverViewModel exercise = exercises.get(position);

        TextView date = holder.dateView;
        date.setText(exercise.formatDate(exercise.getDate()));

        TextView weight = holder.weightView;
        weight.setText(exercise.getWeight());

        TextView reps = holder.repView;
        reps.setText(exercise.getReps());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateView;
        TextView weightView;
        TextView repView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.overview_date_value);
            weightView = (TextView) itemView.findViewById(R.id.overview_weight_value);
            repView = (TextView) itemView.findViewById(R.id.overview_reps_value);
        }
    }
}
