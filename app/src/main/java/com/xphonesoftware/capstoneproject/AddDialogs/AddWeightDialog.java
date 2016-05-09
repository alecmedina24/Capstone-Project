package com.xphonesoftware.capstoneproject.AddDialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xphonesoftware.capstoneproject.R;
import com.xphonesoftware.capstoneproject.data.WeightContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private ContentValues weightData;
    private Context context;
    private UpdateWeightScreenListener updateWeightScreenListener;
    private AddWeightCallback addWeightCallback;
    private long date;

    //Callbacks to the MainActivity to update the screen and send weight data to google
    public interface UpdateWeightScreenListener {
        void updateWeightScreen();
    }

    public interface AddWeightCallback {
        void addWeightGoogle(String weight);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_weight_dialog, null);

        ButterKnife.bind(this, view);

        weightData = new ContentValues();

        context = getContext();

        updateWeightScreenListener = (UpdateWeightScreenListener) getActivity();
        addWeightCallback = (AddWeightCallback) getActivity();

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
                    addWeightCallback.addWeightGoogle(weight);

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
