package com.xphonesoftware.capstoneproject;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xphonesoftware.capstoneproject.AddDialogs.AddExerciseDialog;
import com.xphonesoftware.capstoneproject.AddDialogs.AddWeightDialog;
import com.xphonesoftware.capstoneproject.ExerciseScreen.ExerciseFragment;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayAdapter;
import com.xphonesoftware.capstoneproject.MyDayScreen.MyDayFragment;
import com.xphonesoftware.capstoneproject.WeightScreen.WeightFragment;
import com.xphonesoftware.capstoneproject.Widget.ExerciseWidgetProvider;

/**
 * Created by alecmedina on 5/1/16.
 */
public class MainActivity extends AppCompatActivity implements AddExerciseDialog.UpdateExerciseScreenListener,
        MyDayAdapter.MyDayAdapterCallback, AddWeightDialog.UpdateWeightScreenListener {

    private static final int NUM_PAGES = 3;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private MyDayFragment myDayFragment;
    private ExerciseFragment exerciseFragment;
    private WeightFragment weightFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.my_day_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.exercises_tab));
        tabLayout.addTab(tabLayout.newTab().setText("MY WEIGHT"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

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
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                myDayFragment = new MyDayFragment();
                return myDayFragment;
            } else if (position == 1){
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

    public static void updateMyWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context, ExerciseWidgetProvider.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(ExerciseWidgetProvider.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }
}


