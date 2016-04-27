package com.xphonesoftware.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.exercise_type_content)
    EditText excerciseContentText;
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

    private static final int SPEECH_REQUEST_CODE = 0;
    private String spokenWorkout;
    private String weightSubString;
    private String repSubstring;
    private boolean hasWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                excerciseContentText.setText("");
                weightContentText.setText("");
                repContentText.setText("");
                Toast.makeText(getApplicationContext(), "Exercise added", Toast.LENGTH_SHORT).show();
            }
        });
        rejectWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excerciseContentText.setText("");
                weightContentText.setText("");
                repContentText.setText("");
                Toast.makeText(getApplicationContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String parseExercise(String speech) {
        int index = 0;
        for (char i: speech.toCharArray()) {
            index++;
            if (Character.isDigit(i)) {
                String subString = speech.substring(0, index - 1);
                weightSubString = speech.substring(index - 1);
                Log.v("Exercise Type", subString);
                Log.v("Remaining String", weightSubString);
                return subString;
            }
        }
        return "Please repeat workout";
    }

    public String parseWeight() {
        int index = 0;
        if (!weightSubString.contains("pounds")) {
            hasWeight = false;
            return "N/A";
        }
        for (char i: weightSubString.toCharArray()) {
            index++;
            if (Character.isLetter(i)) {
                String substring = weightSubString.substring(0, index - 1);
                repSubstring = weightSubString.substring(index - 1);
                Log.v("Weight", substring);
                Log.v("Remaining String", repSubstring);
                hasWeight = true;
                return  substring;
            }
        }
        return "Please repeat workout";
    }

    public String parseCount() {
        int index = 0;
        int index2 = 0;
        if (hasWeight) {
            for (char i : repSubstring.toCharArray()) {
                index++;
                if (Character.isDigit(i)) {
                    String subString1 = repSubstring.substring(index - 1);
                    Log.v("First Substring", subString1);
                    for (char j : subString1.toCharArray()) {
                        index2++;
                        if (Character.isAlphabetic(j)) {
                            String subString2 = subString1.substring(0, index2 - 1);
                            Log.v("Rep Count", subString2);
                            return subString2;
                        }
                    }
                }
            }
        } else {
            int index3 = 0;
            for (char i: weightSubString.toCharArray()) {
                index3++;
                if (Character.isLetter(i)) {
                    String weightlessSubString = weightSubString.substring(0, index3 - 1);
                    return  weightlessSubString;
                }
            }
        }
        return "Please repeat workout";
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
            excerciseContentText.setText(parseExercise(spokenWorkout));
            weightContentText.setText(parseWeight());
            repContentText.setText(parseCount());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
