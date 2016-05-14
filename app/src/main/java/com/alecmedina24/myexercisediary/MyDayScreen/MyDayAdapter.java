package com.alecmedina24.myexercisediary.MyDayScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SelectableHolder;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.alecmedina24.myexercisediary.R;
import com.alecmedina24.myexercisediary.data.ExerciseContract;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alecmedina on 4/29/16.
 */
public class MyDayAdapter extends RecyclerView.Adapter<MyDayAdapter.ViewHolder> {

    public static final int NUM_COLUMNS = 3;

    private List<MyDayModel> exercises;
    private MyDayAdapterCallback myDayAdapterCallback;
    private Context context;
    private MultiSelector multiSelector;
    private AppCompatActivity activity;
    private ModalMultiSelectorCallback mDeleteMode;
    private static final String KEY_NAME = "_id";
    private ArrayList<Integer> selectedPositions;
    private boolean isSelected;

    public interface MyDayAdapterCallback {
        void setExercise(String exercise);

        void redrawScreen();
    }

    public MyDayAdapter(final List<MyDayModel> exercises, Context context,
                        final AppCompatActivity appCompatActivity) {
        this.exercises = exercises;
        this.context = context;
        myDayAdapterCallback = (MyDayAdapterCallback) context;
        multiSelector = new MultiSelector();
        this.activity = appCompatActivity;

        selectedPositions = new ArrayList<>();

        multiSelector = new MultiSelector();

        mDeleteMode = new ModalMultiSelectorCallback(multiSelector) {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                super.onCreateActionMode(actionMode, menu);
                activity.getMenuInflater().inflate(R.menu.menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                // don't call super here - leads to crash!
                // clear selections
                multiSelector.clearSelections();
                // here we need to change the mIsSelectable property without refreshing all the holders,
                // so we cant use mMultiSelector.setSelectable(false)
                try {
                    Field field = multiSelector.getClass().getDeclaredField("mIsSelectable");
                    if (field != null) {
                        if (!field.isAccessible())
                            field.setAccessible(true);
                        field.set(multiSelector, false);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    for (int i = exercises.size() * 3 + 3; i >= 0; i--) {
                        if (multiSelector.isSelected(i, 0)) {
                            appCompatActivity.getContentResolver()
                                    .delete(ExerciseContract.ExerciseEntry.CONTENT_URI, KEY_NAME +
                                            "=" + String.valueOf(exercises.get(i / 3 - 1).getId()), null);
                        }
                    }
                    mode.finish();
                    myDayAdapterCallback.redrawScreen();
                }
                return false;
            }
        };
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseView = inflater.inflate(R.layout.my_day_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(exerciseView, multiSelector);

        return viewHolder;
    }

    public int convertDipToPx(int pixel) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dips = (int) ((pixel * scale) + 0.5f);
        return dips;
    }

    @Override
    public void onBindViewHolder(final MyDayAdapter.ViewHolder holder, final int position) {
        int row = position / NUM_COLUMNS;
        int col = position % NUM_COLUMNS;

        final TextView textView = holder.myDayView;

        int edgeDips = convertDipToPx(16);
        int insideDips = convertDipToPx(2);

        //The first three rows are the heading so check for position 0,1,2
        // (column 1,2,3) and set the title
        if (row == 0) {
            switch (col) {
                case 0:
                    textView.setText(R.string.my_day_exercise_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, 0, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 1:
                    textView.setText(R.string.my_day_weight_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(insideDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
                    break;
                case 2:
                    textView.setText(R.string.my_day_reps_header);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setTypeface(null, Typeface.BOLD);
            }
        } else {

            final MyDayModel exercise = exercises.get(row - 1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!multiSelector.tapSelection(holder)) {
                        myDayAdapterCallback.setExercise(exercise.getExercise());
                    } else {
                        int selectedPosition = position % NUM_COLUMNS;

                        if (selectedPositions.size() > 0) {
                            for (int i = 0; i < selectedPositions.size(); i++) {
                                if (position == selectedPositions.get(i)) {
                                    multiSelector.setSelected(position, 0, false);
                                    selectedPositions.remove(i);
                                    isSelected = true;
                                    if (selectedPosition == 0) {
                                        multiSelector.setSelected(position + 1, 0, false);
                                        multiSelector.setSelected(position + 2, 0, false);
                                        int index = selectedPositions.indexOf(position + 1);
                                        selectedPositions.remove(index);
                                        int index1 = selectedPositions.indexOf(position + 2);
                                        selectedPositions.remove(index1);
                                    } else if (selectedPosition == 1) {
                                        multiSelector.setSelected(position - 1, 0, false);
                                        multiSelector.setSelected(position + 1, 0, false);
                                        int index = selectedPositions.indexOf(position - 1);
                                        selectedPositions.remove(index);
                                        int index1 = selectedPositions.indexOf(position + 1);
                                        selectedPositions.remove(index1);
                                    } else {
                                        multiSelector.setSelected(position - 1, 0, false);
                                        multiSelector.setSelected(position - 2, 0, false);
                                        int index = selectedPositions.indexOf(position - 1);
                                        selectedPositions.remove(index);
                                        int index1 = selectedPositions.indexOf(position - 2);
                                        selectedPositions.remove(index1);
                                    }
                                }
                            }
                        }
                        if (isSelected == false) {
                            multiSelector.setSelected(position, 0, true);
                            selectedPositions.add(position);
                            if (selectedPosition == 0) {
                                multiSelector.setSelected(position + 1, 0, true);
                                multiSelector.setSelected(position + 2, 0, true);
                                selectedPositions.add(position + 1);
                                selectedPositions.add(position + 2);
                            } else if (selectedPosition == 1) {
                                multiSelector.setSelected(position - 1, 0, true);
                                multiSelector.setSelected(position + 1, 0, true);
                                selectedPositions.add(position - 1);
                                selectedPositions.add(position + 1);
                            } else {
                                multiSelector.setSelected(position - 1, 0, true);
                                multiSelector.setSelected(position - 2, 0, true);
                                selectedPositions.add(position - 1);
                                selectedPositions.add(position - 2);
                            }
                        }
                    }
                    isSelected = false;
                }
            });

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    activity.startSupportActionMode(mDeleteMode);

                    int selectedPosition = position % NUM_COLUMNS;

                    multiSelector.setSelected(position, 0, true);
                    selectedPositions.add(position);
                    if (selectedPosition == 0) {
                        multiSelector.setSelected(position + 1, 0, true);
                        multiSelector.setSelected(position + 2, 0, true);
                        selectedPositions.add(position + 1);
                        selectedPositions.add(position + 2);
                    } else if (selectedPosition == 1) {
                        multiSelector.setSelected(position - 1, 0, true);
                        multiSelector.setSelected(position + 1, 0, true);
                        selectedPositions.add(position - 1);
                        selectedPositions.add(position + 1);
                    } else {
                        multiSelector.setSelected(position - 1, 0, true);
                        multiSelector.setSelected(position - 2, 0, true);
                        selectedPositions.add(position - 1);
                        selectedPositions.add(position - 2);
                    }
                    return true;
                }
            });

            switch (col) {
                case 0:
                    textView.setText(exercise.getExercise());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    textView.setPadding(edgeDips, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 1:
                    textView.setText(exercise.getWeight());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, insideDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
                case 2:
                    textView.setText(exercise.getReps());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(0, 0, edgeDips, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTypeface(null, Typeface.NORMAL);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size() * NUM_COLUMNS + 3;
    }

    public static class ViewHolder extends SwappingHolder implements SelectableHolder {

        TextView myDayView;

        public ViewHolder(View itemView, MultiSelector multiSelector) {
            super(itemView, multiSelector);
            myDayView = (TextView) itemView.findViewById(R.id.my_day_item);
        }
    }
}
