package com.xphonesoftware.capstoneproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.xphonesoftware.capstoneproject.DetailsScreen.ExercisesAdapter;

/**
 * Created by alecmedina on 5/1/16.
 */
public class PagerActivity extends FragmentActivity implements MainFragment.UpdateScreenListener,
        ExercisesAdapter.SetExercise {

    private static final int NUM_PAGES = 3;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private MainFragment mainFragment;
    private DetailFragment detailFragment;
    private OverViewFragment overViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager);

        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
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
    public void updateScreen() {
        detailFragment.setNewAdapter();
    }

    @Override
    public void setExercise(String exercise) {
        overViewFragment.setOverViewExercise(exercise);
        pager.setCurrentItem(3);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                mainFragment = new MainFragment();
                return mainFragment;
            } else if (position == 1) {
                detailFragment = new DetailFragment();
                return detailFragment;
            } else {
                overViewFragment = new OverViewFragment();
                return overViewFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
