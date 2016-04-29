package com.xphonesoftware.capstoneproject;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.xphonesoftware.capstoneproject.data.ExerciseContract;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.exercise_type_content)
    EditText exerciseContentText;
    @Bind(R.id.weight_content)
    EditText weightContentText;
    @Bind(R.id.rep_count)
    EditText repContentText;
    @Bind(R.id.accept_text_button)
    Button acceptWorkoutButton;
    @Bind(R.id.reject_text_button)
    Button rejectWorkoutButton;
    @Bind(R.id.voice_record)
    ImageButton recordVoiceButton;
    @Bind(R.id.error_message)
    TextView errorMessage;

    private static final int SPEECH_REQUEST_CODE = 0;
    private static final String ERROR_CODE = "1";
    private String spokenWorkout;
    private String weightSubString;
    private String repSubstring;
    private boolean hasWeight;
    private ContentValues exerciseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        recordVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });
        acceptWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exercise = String.valueOf(exerciseContentText.getText());
                String weight = String.valueOf(weightContentText.getText());
                String reps = String.valueOf(repContentText.getText());
                exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_EXERCISE, exercise);
                exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_WEIGHT, weight);
                exerciseData.put(ExerciseContract.ExerciseEntry.COLUMN_REPS, reps);
                getContentResolver().insert(ExerciseContract.ExerciseEntry.CONTENT_URI, exerciseData);
                exerciseContentText.setText("");
                weightContentText.setText("");
                repContentText.setText("");
                Toast.makeText(getApplicationContext(), "Exercise added", Toast.LENGTH_SHORT).show();
            }
        });
        rejectWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseContentText.setText("");
                weightContentText.setText("");
                repContentText.setText("");
                Toast.makeText(getApplicationContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
            }
        });
        exerciseData = new ContentValues();
    }

    public String parseExercise(String speech) {
        int index = 0;
        for (char i : speech.toCharArray()) {
            index++;
            if (Character.isDigit(i)) {
                String exercise = speech.substring(0, index - 1);
                weightSubString = speech.substring(index - 1);
                Log.v("Exercise Type", exercise);
                Log.v("Remaining String", weightSubString);
                return exercise;
            }
        }
        return ERROR_CODE;
    }

    public String parseWeight() {
        int index = 0;
        if (weightSubString != null) {
            if (!weightSubString.contains("pounds")) {
                hasWeight = false;
                return "N/A";
            }
            for (char i : weightSubString.toCharArray()) {
                index++;
                if (Character.isLetter(i)) {
                    String weight = weightSubString.substring(0, index - 1);
                    repSubstring = weightSubString.substring(index - 1);
                    Log.v("Weight", weight);
                    Log.v("Remaining String", repSubstring);
                    hasWeight = true;
                    return weight;
                }
            }
        }
        return ERROR_CODE;
    }

    public String parseCount() {
        int index = 0;
        int index2 = 0;
        if (repSubstring != null && weightSubString != null) {
            if (hasWeight) {
                for (char i : repSubstring.toCharArray()) {
                    index++;
                    if (Character.isDigit(i)) {
                        String subString1 = repSubstring.substring(index - 1);
                        Log.v("First Substring", subString1);
                        for (char j : subString1.toCharArray()) {
                            index2++;
                            if (Character.isAlphabetic(j)) {
                                String reps = subString1.substring(0, index2 - 1);
                                Log.v("Rep Count", reps);
                                return reps;
                            }
                        }
                    }
                }
            } else {
                int index3 = 0;
                for (char i : weightSubString.toCharArray()) {
                    index3++;
                    if (Character.isLetter(i)) {
                        String noWeightRepCount = weightSubString.substring(0, index3 - 1);
                        return noWeightRepCount;
                    }
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenWorkout = results.get(0);
            Log.v("Full Exercise", spokenWorkout);
            String exercise = parseExercise(spokenWorkout);
            String weight = parseWeight();
            String reps = parseCount();
            if (exercise == ERROR_CODE || weight == ERROR_CODE || reps == ERROR_CODE) {
                errorMessage.setVisibility(View.VISIBLE);
                acceptWorkoutButton.setClickable(false);
            } else {
                errorMessage.setVisibility(View.INVISIBLE);
                acceptWorkoutButton.setClickable(true);
                exerciseContentText.setText(exercise);
                weightContentText.setText(weight);
                repContentText.setText(reps);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
