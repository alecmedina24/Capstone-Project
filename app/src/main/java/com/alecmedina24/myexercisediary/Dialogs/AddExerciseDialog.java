package com.alecmedina24.myexercisediary.Dialogs;

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

import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.Data.ExerciseContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddExerciseDialog extends DialogFragment {
    @Bind(R.id.exercise_type_content)
    EditText exerciseContentText;
    @Bind(R.id.weight_content)
    EditText weightContentText;
    @Bind(R.id.rep_count)
    EditText repContentText;

    private static final String ERROR_CODE = "-1";
    private static final String[] TENS =
            {"ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private static final String[] ONES =
            {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] TEENS =
            {"eleven","twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
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
        void displayVoiceRecognizer();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_exercise_dialog, null);

        ButterKnife.bind(this, view);

        exerciseData = new ContentValues();

        context = getContext();

        updateExerciseScreenListener = (UpdateExerciseScreenListener) getActivity();

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
        builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateExerciseScreenListener.displayVoiceRecognizer();
                dialog.dismiss();
            }
        });
        setDialogFields();
        return builder.create();
    }

    //For voice recognizer only: parses the returned string for the exercise and substrings the rest
    //for further parsing
    public String parseExercise(String speech) {
        int index = 0;
        if (speech.contains("hundred")) {
            String exercise = speech.substring(0, speech.indexOf("hundred") - 1);
            weightSubString = speech.substring(speech.indexOf("hundred"));
            return exercise;
        } else {
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
            if (weightSubString.contains("hundred")) {
                String weightInWordsConverted = "1";
                String weightInWords = weightSubString.substring(0, weightSubString.indexOf("pounds") - 1);
                repSubstring = weightSubString.substring(weightSubString.indexOf("pounds"));
                hasWeight = true;
                if (weightInWords.contains("and")) {
                    weightInWords = weightInWords.replace("and", "");
                }
                if (weightInWords.contains("-")) {
                    weightInWords = weightInWords.replace("-", " ");
                }
                String[] weightInWordsFormatted = weightInWords.split("\\s");
                for (int i = 1; i < weightInWordsFormatted.length; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (weightInWordsFormatted[i].equals(TENS[j])) {
                            weightInWordsConverted += String.valueOf((j + 1) * 10);
                        }
                        if (weightInWordsFormatted[i].equals(ONES[j])) {
                            weightInWordsConverted = weightInWordsConverted.replace("0", String.valueOf(j + 1));
                        }
                        if (weightInWordsFormatted[i].equals(TEENS[j])) {
                            weightInWordsConverted += "1" + String.valueOf(j + 1);
                        }
                    }
                }
                return weightInWordsConverted;
            } else {
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

    public void setSpokenWorkout(String spokenWorkout) {
        this.spokenWorkout = spokenWorkout;
    }

    public void setDialogFields() {
        String exercise = parseExercise(spokenWorkout.toLowerCase()).toLowerCase();
        String weight = parseWeight();
        String reps = parseCount();

        if (exercise == ERROR_CODE || weight == ERROR_CODE || reps == ERROR_CODE) {
            Toast.makeText(context.getApplicationContext(),
                    R.string.repeat_exercise, Toast.LENGTH_SHORT).show();
        } else {
            exerciseContentText.setText(exercise);
            weightContentText.setText(weight);
            repContentText.setText(reps);
        }
    }
}
