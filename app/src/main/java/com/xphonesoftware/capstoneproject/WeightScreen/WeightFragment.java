package com.xphonesoftware.capstoneproject.WeightScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.stetho.Stetho;
import com.xphonesoftware.capstoneproject.R;
import com.xphonesoftware.capstoneproject.data.WeightContract;
import com.xphonesoftware.capstoneproject.data.WeightDbHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightFragment extends Fragment {

    @Bind(R.id.weight_list)
    RecyclerView weightList;

    private static final String[] WEIGHT_PROJECTION = {
            WeightContract.WeightEntry.COLUMN_DATE,
            WeightContract.WeightEntry.COLUMN_WEIGHT
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.weight_fragment_layout, container, false);

        Stetho.initializeWithDefaults(getContext());

        ButterKnife.bind(this, rootView);

        WeightDbHelper dbHelper = new WeightDbHelper(getContext());
        dbHelper.getWritableDatabase();

        return rootView;
    }
}
