package com.xphonesoftware.capstoneproject.WeightScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.xphonesoftware.capstoneproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder>
        implements GoogleApiClient.OnConnectionFailedListener {

    public static final int NUM_COLUMNS = 3;
    private static final String TAG = "weight";

    private List<WeightModel> weights;
    private Context context;
    private GoogleApiClient googleApiClient;

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

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.HISTORY_API).build();

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

        if (row == 0) {
            switch (col) {
                case 0:
                    textView.setText("day");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, 0, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 1:
                    textView.setText("time");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(insideDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 2:
                    textView.setText("weight");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
            }
        } else {

            final WeightModel weight = weights.get(row - 1);
//            if (row == 1 && col == 0) {
//                pushWeightData(Integer.valueOf(weight.getWeight()));
//            }

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

    public void pushWeightData(int dayWeight) {
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(context.getPackageName())
                .setDataType(DataType.TYPE_WEIGHT)
                .setType(DataSource.TYPE_RAW)
                .build();

        // Create a data set
        int weight = dayWeight;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_WEIGHT).setInt(weight);
        dataSet.add(dataPoint);

        // Then, invoke the History API to insert the data and await the result, which is
        // possible here because of the {@link AsyncTask}. Always include a timeout when calling
        // await() to prevent hanging that can occur from the service being shutdown because
        // of low memory or other conditions.
        Log.i(TAG, "Inserting the dataset in the History API.");
        com.google.android.gms.common.api.Status insertStatus =
                Fitness.HistoryApi.insertData(googleApiClient, dataSet)
                        .await(1, TimeUnit.MINUTES);

        // Before querying the data, check to see if the insertion succeeded.
        if (!insertStatus.isSuccess()) {
            Log.i(TAG, "There was a problem inserting the dataset.");
        }

        // At this point, the data has been inserted and can be read.
        Log.i(TAG, "Data insert was successful!");
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
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String timeFormatted = formatter.format(time);
        return timeFormatted;
    }


    @Override
    public int getItemCount() {
        return weights.size() * 3 + 3;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView weightListItem;

        public ViewHolder(View itemView) {
            super(itemView);
            weightListItem = (TextView) itemView.findViewById(R.id.weight_fragment_item);
        }
    }
}
