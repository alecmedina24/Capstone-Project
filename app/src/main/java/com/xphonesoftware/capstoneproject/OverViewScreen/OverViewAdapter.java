package com.xphonesoftware.capstoneproject.OverViewScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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

    public static final int NUM_COLUMNS = 3;

    private List<OverViewModel> exercises;
    private Context context;

    public OverViewAdapter(List<OverViewModel> exercises) {
        this.exercises = exercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseView = inflater.inflate(R.layout.my_day_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(exerciseView);

        return viewHolder;
    }

    public int convertDipToPx(int pixel){
        float scale = context.getResources().getDisplayMetrics().density;
        int dips=(int) ((pixel * scale) + 0.5f);
        return dips;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int row = position / NUM_COLUMNS;
        int col = position % NUM_COLUMNS;

        TextView textView = holder.dayView;

        int edgeDips = convertDipToPx(16);
        int insideDips = convertDipToPx(2);

        if (row == 0) {
            switch (col) {
                case 0:
                    textView.setText("Date");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, 0, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 1:
                    textView.setText("Weight");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(insideDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 2:
                    textView.setText("Reps");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
            }
        } else {

            final OverViewModel exercise = exercises.get(row - 1);

            switch (col) {
                case 0:
                    textView.setText(exercise.formatDate(exercise.getDate()));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 1:
                    textView.setText(exercise.getWeight());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 2:
                    textView.setText(exercise.getReps());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size() * 3 + 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayView;

        public ViewHolder(View itemView) {
            super(itemView);
            dayView = (TextView) itemView.findViewById(R.id.test_item);
        }
    }
}
