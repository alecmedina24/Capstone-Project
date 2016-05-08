package com.xphonesoftware.capstoneproject.AddDialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.xphonesoftware.capstoneproject.R;
import com.xphonesoftware.capstoneproject.data.WeightContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alecmedina on 5/7/16.
 */
public class AddWeightDialog extends DialogFragment {

    @Bind(R.id.date_picker)
    EditText datePicker;
    @Bind(R.id.time_picker)
    EditText timePicker;
    @Bind(R.id.weight_picker)
    EditText weightPicker;

    private static final String TAG = "weight";

    private ContentValues weightData;
    private Context context;
    private UpdateWeightScreenListener updateWeightScreenListener;
    private long date;
    private GoogleApiClient googleApiClient;
//    private com.google.android.gms.common.api.Status insertStatus;


    public interface UpdateWeightScreenListener {
        void updateWeightScreen();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_weight_dialog, null);

        ButterKnife.bind(this, view);

        weightData = new ContentValues();

        context = getContext();

        updateWeightScreenListener = (UpdateWeightScreenListener) getActivity();

        date = System.currentTimeMillis();

        datePicker.setText(formatDate(date));
        timePicker.setText(formatTime(date));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Log your weight");
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String weight = String.valueOf(weightPicker);
                if (!weight.equals("")) {
                    weight = String.valueOf(weightPicker.getText());
                    weightData.put(WeightContract.WeightEntry.COLUMN_DATE, date);
                    weightData.put(WeightContract.WeightEntry.COLUMN_WEIGHT, weight);
                    context.getContentResolver().insert(WeightContract.WeightEntry.CONTENT_URI, weightData);

                    updateWeightScreenListener.updateWeightScreen();

                    pushWeightData(Integer.valueOf(weight));

                    dialog.dismiss();

                    Toast.makeText(context.getApplicationContext(),
                            "Weight Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(),
                            "Weight not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }

        );
        return builder.create();
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
        dataPoint.getValue(Field.FIELD_WEIGHT).setFloat(Float.valueOf(weight));
        dataSet.add(dataPoint);

        // Then, invoke the History API to insert the data and await the result, which is
        // possible here because of the {@link AsyncTask}. Always include a timeout when calling
        // await() to prevent hanging that can occur from the service being shutdown because
        // of low memory or other conditions.
        Log.i(TAG, "Inserting the dataset in the History API.");
        Fitness.HistoryApi.insertData(googleApiClient, dataSet).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // Before querying the data, check to see if the insertion succeeded.
                if (!status.isSuccess()) {
                    Log.i(TAG, "There was a problem inserting the dataset.");
                }

                // At this point, the data has been inserted and can be read.
                Log.i(TAG, "Data insert was successful!");
            }
        });
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
}
