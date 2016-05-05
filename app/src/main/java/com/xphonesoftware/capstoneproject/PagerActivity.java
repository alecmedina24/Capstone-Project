package com.xphonesoftware.capstoneproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.xphonesoftware.capstoneproject.DetailsScreen.ExercisesAdapter;

/**
 * Created by alecmedina on 5/1/16.
 */
public class PagerActivity extends AppCompatActivity implements MainFragment.UpdateScreenListener,
        ExercisesAdapter.SetExercise {

    private static final int NUM_PAGES = 2;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private DetailFragment detailFragment;
    private OverViewFragment overViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager);

        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

//        final android.app.ActionBar actionBar = getActionBar();

//        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
//
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//            @Override
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//            }
//
//            @Override
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//            }
//        };
//
//        actionBar.addTab(actionBar.newTab().setText("My Day").setTabListener(tabListener));
//        actionBar.addTab(actionBar.newTab().setText("Exercises").setTabListener(tabListener));
//
//        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                getActionBar().setSelectedNavigationItem(position);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_delete, menu);

        return true;
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
        pager.setCurrentItem(1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
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
