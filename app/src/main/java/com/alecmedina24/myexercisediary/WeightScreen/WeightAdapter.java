package com.alecmedina24.myexercisediary.WeightScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alecmedina24.myexercisediary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {

    public static final int NUM_COLUMNS = 3;

    private List<WeightModel> weights;
    private Context context;

    public WeightAdapter(List<WeightModel> weights, Context context) {
        this.weights = weights;
        this.context = context;
    }

    @Override
    public WeightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View WeightView = inflater.inflate(R.layout.weight_fragment_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(WeightView);

        return viewHolder;
    }

    public int convertDipToPx(int pixel) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dips = (int) ((pixel * scale) + 0.5f);
        return dips;
    }

    @Override
    public void onBindViewHolder(WeightAdapter.ViewHolder holder, int position) {
        int row = position / NUM_COLUMNS;
        int col = position % NUM_COLUMNS;

        TextView textView = holder.weightListItem;

        int edgeDips = convertDipToPx(16);
        int insideDips = convertDipToPx(2);

        //The first three rows are the heading so check for position 0,1,2
        // (column 1,2,3) and set the title
        if (row == 0) {
            switch (col) {
                case 0:
                    textView.setText(R.string.weight_day_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, 0, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 1:
                    textView.setText(R.string.weight_time_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(insideDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 2:
                    textView.setText(R.string.weight_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
            }
        } else {

            final WeightModel weight = weights.get(row - 1);

            switch (col) {
                case 0:
                    textView.setText(formatDate(weight.getDate()));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 1:
                    textView.setText(formatTime(weight.getDate()));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 2:
                    textView.setText(weight.getWeight());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
            }
        }
    }

    public String formatDate(long date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        String day = format.format(calendar.getTime());
        return day;
    }

    public String formatTime(long date) {
        Date time = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        String timeFormatted = formatter.format(time);
        return timeFormatted;
    }


    @Override
    public int getItemCount() {
        return weights.size() * 3 + 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView weightListItem;

        public ViewHolder(View itemView) {
            super(itemView);
            weightListItem = (TextView) itemView.findViewById(R.id.weight_fragment_item);
        }
    }
}
