package com.alecmedina24.myexercisediary.MyDayScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alecmedina24.myexercisediary.Dialogs.AddExerciseDialog;
import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.Data.DataLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 4/29/16.
 */
public class MyDayFragment extends Fragment implements DataLoader.MyDayModelCallback {

    @Bind(R.id.list_layout)
    RecyclerView exerciseList;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.day_listed)
    TextView dayListedView;
    @Bind(R.id.next_day)
    TextView nextDayButton;
    @Bind(R.id.previous_day)
    TextView previousDayButton;
    @Bind(R.id.floating_action_button)
    FloatingActionButton fab;
    @Bind(R.id.detail_screen_layout)
    RelativeLayout rootLayout;
    @Bind(R.id.day_listed)
    TextView dayListed;

    private static final long ONE_DAY = 86400000;
    private static final int SPEECH_REQUEST_CODE = 0;

    private MyDayModel myDayModel;
    private long day;
    private long today;
    private long yesterday;
    private Context context;
    private MyDayAdapter myDayAdapter;
    private AddExerciseDialog addExerciseDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_day_fragment_layout, container, false);

        getData();

        context = getContext();

//        Stetho.initializeWithDefaults(context);

        ButterKnife.bind(this, rootView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        day = System.currentTimeMillis();

        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = day - ONE_DAY;
                myDayModel.setDate(day);
                setDayHeader();
                setNewAdapter();
            }
        });

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = day + ONE_DAY;
                myDayModel.setDate(day);
                setDayHeader();
                setNewAdapter();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
                Snackbar.make(rootLayout, "Please speak exercise, weight, and reps", Snackbar.LENGTH_LONG).show();
            }
        });

        dayListed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        today = System.currentTimeMillis();
        yesterday = System.currentTimeMillis() - ONE_DAY;

        return rootView;
    }

    public void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == -1) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenWorkout = results.get(0);
            FragmentManager fm = getFragmentManager();
            addExerciseDialog = new AddExerciseDialog();
            addExerciseDialog.show(fm, context.getString(R.string.add_exercise_tag));
            addExerciseDialog.setSpokenWorkout(spokenWorkout);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getData() {
        DataLoader dataLoader = new DataLoader(getActivity(), this);
        dataLoader.setAdapterId(1);
        getLoaderManager().initLoader(DataLoader.EXERCISE_LOADER, null, dataLoader);
    }

    public void setNewAdapter() {
        exerciseList.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), MyDayAdapter.NUM_COLUMNS));
        exerciseList.setItemAnimator(new SlideInUpAnimator());
        myDayAdapter = new MyDayAdapter(myDayModel.createExercisesList(),
                context, (AppCompatActivity) getActivity());
        exerciseList.setAdapter(myDayAdapter);
    }

    public void setDayHeader() {
        if (myDayModel.formatDate(day).equals(myDayModel.formatDate(today))) {
            dayListedView.setText(context.getString(R.string.today));
        } else if (myDayModel.formatDate(day).equals(myDayModel.formatDate(yesterday))) {
            dayListedView.setText(context.getString(R.string.yesterday));
        } else {
            dayListedView.setText(myDayModel.formatDate(day));
        }
    }

    @Override
    public void setMyDayModel(MyDayModel myDayModel) {
        this.myDayModel = myDayModel;
//        updateMyWidgets(myDayModel);
        setNewAdapter();
    }

//    public void updateMyWidgets(MyDayModel myDayModel) {
//        AppWidgetManager man = AppWidgetManager.getInstance(context);
//        int[] ids = man.getAppWidgetIds(
//                new ComponentName(context, ExerciseWidgetProvider.class));
//        Intent updateIntent = new Intent();
//        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        updateIntent.putExtra(ExerciseWidgetProvider.WIDGET_IDS_KEY, ids);
//        context.sendBroadcast(updateIntent);
//    }
}
