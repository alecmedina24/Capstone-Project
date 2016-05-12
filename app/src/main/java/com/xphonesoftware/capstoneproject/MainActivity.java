package com.xphonesoftware.capstoneproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.xphonesoftware.capstoneproject.AddDialogs.AddExerciseDialog;
import com.xphonesoftware.capstoneproject.AddDialogs.AddWeightDialog;
import com.xphonesoftware.capstoneproject.ExerciseScreen.ExerciseFragment;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayAdapter;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayFragment;
import com.xphonesoftware.capstoneproject.WeightScreen.WeightFragment;
import com.xphonesoftware.capstoneproject.data.ExerciseContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by alecmedina on 5/1/16.
 */
public class MainActivity extends AppCompatActivity implements AddExerciseDialog.UpdateExerciseScreenListener,
        MyDayAdapter.MyDayAdapterCallback, AddWeightDialog.UpdateWeightScreenListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        AddWeightDialog.AddWeightCallback {

    private static final int NUM_PAGES = 3;
    private static final String TAG = "weight";
    private static final String KEY_NAME = "_id";

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private MyDayFragment myDayFragment;
    private ExerciseFragment exerciseFragment;
    private WeightFragment weightFragment;
    private GoogleApiClient googleApiClient;
    private ArrayList<Long> deleteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.my_day_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.exercises_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.weight_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        //Connect to GoogleAPIs
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .build();

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("GoogleApiClient", "Failed to connect");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("GoogleApiClient", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (deleteList != null) {
                    for (int i = 0; i < deleteList.size(); i++) {
                        getContentResolver().delete(ExerciseContract.ExerciseEntry
                                        .CONTENT_URI, KEY_NAME + "=" + String.valueOf(deleteList.get(i)), null);
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Add weight to GoogleFit
    @Override
    public void addWeightGoogle(String weight) {
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(getPackageName())
                .setDataType(DataType.TYPE_WEIGHT)
                .setType(DataSource.TYPE_RAW)
                .build();

        // Create a data set
        float weightInKilo = Float.valueOf(weight) / 2.2046226218f;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the weight.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_WEIGHT).setFloat(weightInKilo);
        dataSet.add(dataPoint);

        Log.v(TAG, "Inserting the dataset in the History API.");
        Fitness.HistoryApi.insertData(googleApiClient, dataSet).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // Before querying the data, check to see if the insertion succeeded.
                if (!status.isSuccess()) {
                    Log.v(TAG, "There was a problem inserting the dataset.");
                } else {
                    // At this point, the data has been inserted and can be read.
                    Log.v(TAG, "Data insert was successful!");
                }
            }
        });
    }

    @Override
    public void updateWeightScreen() {
        weightFragment.getData();
    }

    @Override
    public void updateScreen() {
        myDayFragment.getData();
        exerciseFragment.getData();
    }

    @Override
    public void setExercise(String exercise) {
        exerciseFragment.setExercise(exercise);
        pager.setCurrentItem(1);
    }

    @Override
    public void setPickedList(ArrayList<Long> pickedList) {
        deleteList = pickedList;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                myDayFragment = new MyDayFragment();
                return myDayFragment;
            } else if (position == 1) {
                exerciseFragment = new ExerciseFragment();
                return exerciseFragment;
            } else {
                weightFragment = new WeightFragment();
                return weightFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}


