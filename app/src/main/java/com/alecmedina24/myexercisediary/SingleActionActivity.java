package com.alecmedina24.myexercisediary;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.alecmedina24.myexercisediary.Dialogs.WidgetDialogs.WidgetAddExerciseDialog;
import com.alecmedina24.myexercisediary.Dialogs.WidgetDialogs.WidgetAddWeightDialog;

import java.util.List;

/**
 * Created by alecmedina on 6/17/16.
 */
public class SingleActionActivity extends AppCompatActivity implements WidgetAddExerciseDialog.WidgetExerciseCallback {

    public static final String WEIGHT_ACTION = "ADD_WEIGHT";
    public static final String EXERCISE_ACTION = "ADD_EXERCISE";
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().containsKey(WEIGHT_ACTION)) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                WidgetAddWeightDialog widgetAddWeightDialog = new WidgetAddWeightDialog();
                widgetAddWeightDialog.show(fragmentManager, "weight");
            } else {
                displaySpeechRecognizer();
            }
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            WidgetAddWeightDialog widgetAddWeightDialog = new WidgetAddWeightDialog();
            widgetAddWeightDialog.show(fragmentManager, "weight");
        }
    }

    public void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getString(R.string.structure_text) +
                this.getString(R.string.example_text) + this.getString(R.string.hint_text));
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == -1) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenWorkout = results.get(0);
            FragmentManager fm = getSupportFragmentManager();
            WidgetAddExerciseDialog widgetAddExerciseDialog = new WidgetAddExerciseDialog();
            widgetAddExerciseDialog.show(fm, "widget_add_exercise");
            widgetAddExerciseDialog.setSpokenWorkout(spokenWorkout);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void retryVoiceRecognizer() {
        displaySpeechRecognizer();
    }
}
