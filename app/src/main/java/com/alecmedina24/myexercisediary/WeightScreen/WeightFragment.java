package com.alecmedina24.myexercisediary.WeightScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alecmedina24.myexercisediary.Dialogs.AddWeightDialog;
import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.Data.DataLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alecmedina on 5/6/16.
 */
public class WeightFragment extends Fragment implements DataLoader.WeightModelCallback {

    @Bind(R.id.weight_list)
    RecyclerView weightList;
    @Bind(R.id.add_weight_button)
    FloatingActionButton fab;

    private Context context;
    private WeightModel weightModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.weight_fragment_layout, container, false);

        getData();

        ButterKnife.bind(this, rootView);

        context = getContext();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AddWeightDialog addWeightDialog = new AddWeightDialog();
                addWeightDialog.show(fragmentManager, "weight");
            }
        });

        return rootView;
    }

    public void getData() {
        DataLoader dataLoader = new DataLoader(getActivity(), this);
        dataLoader.setAdapterId(3);
        getLoaderManager().initLoader(DataLoader.WEIGHT_LOADER, null, dataLoader);
    }

    public void setWeightListAdapter() {
        weightList.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), WeightAdapter.NUM_COLUMNS));
        weightList.setItemAnimator(new SlideInUpAnimator());
        weightList.setAdapter(new WeightAdapter(weightModel.createWeightsList(), context));
    }

    @Override
    public void setWeightModel(WeightModel weightModel) {
        this.weightModel = weightModel;
        setWeightListAdapter();
    }
}
