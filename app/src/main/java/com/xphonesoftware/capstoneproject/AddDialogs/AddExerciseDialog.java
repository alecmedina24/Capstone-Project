package com.xphonesoftware.capstoneproject.AddDialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xphonesoftware.capstoneproject.R;
import com.xphonesoftware.capstoneproject.data.ExerciseContract;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddExerciseDialog extends DialogFragment {
    @Bind(R.id.exercise_type_content)
    EditText exerciseContentText;
    @Bind(R.id.weight_content)
    EditText weightContentText;
    @Bind(R.id.rep_count)
    EditText repContentText;
    @Bind(R.id.voice_record)
    ImageButton recordVoiceButton;

    private static final int SPEECH_REQUEST_CODE = 0;
    private static final String ERROR_CODE = "-1";
    private String spokenWorkout;
    private String weightSubString;
    private String repSubstring;
    private long date;
    private boolean hasWeight;
    private ContentValues exerciseData;
    private Context context;
    private UpdateExerciseScreenListener updateExerciseScreenListener;

    //Callback to MainActivity to update the screen
    public interface UpdateExerciseScreenListener {
        void updateScreen();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_exercise_dialog, null);

        ButterKnife.bind(this, view);

        exerciseData = new ContentValues();

        context = getContext();

        updateExerciseScreenListener = (UpdateExerciseScreenListener) getActivity();

        recordVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(context.getString(R.string.add_exercise_title));
        builder.setPositiveButton(context.getString(R.string.positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String exercise = String.valueOf(exerciseContentText.getText());
                //Ensures an exercise is always listed before adding
                if (!exercise.equals("")) {
                    //The values put into the database are pulled from the EditText fields
                    //in case the user manually added/edited items
                    String weight = String.valueOf(weightContentText.getText());
                    String reps = String.valueOf(repContentText.getText());
                    date = System.currentTimeMillis();
                    exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_DATE, date);
                    exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_EXERCISE, exercise);
                    exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_WEIGHT, weight);
                    exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_REPS, reps);
                    context.getContentResolver().insert(ExerciseContract.ExerciseEntry.CONTENT_URI, exerciseData);
                    exerciseContentText.setText("");
                    weightContentText.setText("");
                    repContentText.setText("");

                    updateExerciseScreenListener.updateScreen();

                    dialog.dismiss();

                    Toast.makeText(context.getApplicationContext(),
                            R.string.notify_exercise_added, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(),
                            R.string.notify_exercise_not_added, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }

        );
        return builder.create();
    }

    //For voice recognizer only: parses the returned string for the exercise and substrings the rest
    //for further parsing
    public String parseExercise(String speech) {
        int index = 0;
        for (char i : speech.toCharArray()) {
            index++;
            if (Character.isDigit(i)) {
                String exercise = speech.substring(0, index - 1);
                weightSubString = speech.substring(index - 1);
//                Log.v("Exercise Type", exercise);
//                Log.v("Remaining String", weightSubString);
                return exercise;
            }
        }
        return ERROR_CODE;
    }

    //For voice recognizer only: uses the substring assigned in parseExercise to get weight if present
    //and substrings the rest
    public String parseWeight() {
        int index = 0;
        if (weightSubString != null) {
            if (!weightSubString.contains(context.getString(R.string.pounds_check))) {
                hasWeight = false;
                return "0";
            }
            for (char i : weightSubString.toCharArray()) {
                index++;
                if (Character.isLetter(i)) {
                    String weight = weightSubString.substring(0, index - 1);
                    repSubstring = weightSubString.substring(index - 1);
//                    Log.v("Weight", weight);
//                    Log.v("Remaining String", repSubstring);
                    hasWeight = true;
                    return weight;
                }
            }
        }
        return ERROR_CODE;
    }

    //For voice recognizer only:  uses the substrings assigned in parseWeight to get the count
    public String parseCount() {
        int index = 0;
        int index2 = 0;
        if (repSubstring != null && weightSubString != null) {
            if (hasWeight) {
                for (char i : repSubstring.toCharArray()) {
                    index++;
                    if (Character.isDigit(i)) {
                        String subString1 = repSubstring.substring(index - 1);
//                        Log.v("First Substring", subString1);
                        for (char j : subString1.toCharArray()) {
                            index2++;
                            if (Character.isAlphabetic(j)) {
                                String reps = subString1.substring(0, index2 - 1);
//                                Log.v("Rep Count", reps);
                                return reps;
                            }
                        }
                    }
                }
            }
        } else if (repSubstring == null && weightSubString != null) {
            int index3 = 0;
            for (char i : weightSubString.toCharArray()) {
                index3++;
                if (Character.isLetter(i)) {
                    String noWeightRepCount = weightSubString.substring(0, index3 - 1);
                    return noWeightRepCount;
                }
            }
        }
        return ERROR_CODE;
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == -1) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenWorkout = results.get(0);
//            Log.v("Full Exercise", spokenWorkout);
            String exercise = parseExercise(spokenWorkout);
            String weight = parseWeight();
            String reps = parseCount();
            if (exercise == ERROR_CODE || weight == ERROR_CODE || reps == ERROR_CODE) {
                Toast.makeText(context.getApplicationContext(), R.string.repeat_exercise, Toast.LENGTH_SHORT).show();
            } else {
                exerciseContentText.setText(exercise);
                weightContentText.setText(weight);
                repContentText.setText(reps);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
