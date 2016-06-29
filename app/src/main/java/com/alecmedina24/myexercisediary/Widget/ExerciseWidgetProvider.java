package com.alecmedina24.myexercisediary.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.alecmedina24.myexercisediary.SingleActionActivity;
import com.alecmedina24.myexercisediary.R;

/**
 * Created by alecmedina on 5/5/16.
 */
public class ExerciseWidgetProvider extends AppWidgetProvider {

    private PendingIntent pendingIntent;

//    public static final String WIDGET_IDS_KEY = "exercisewidgetproviderids";

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        if (intent.hasExtra(WIDGET_IDS_KEY)) {
//            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
//            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
//        } else {
//            super.onReceive(context, intent);
//        }
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];

//            Intent intent = new Intent(context, ExerciseWidgetService.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent addWeightIntent = new Intent(context, SingleActionActivity.class);
            addWeightIntent.putExtra(SingleActionActivity.WEIGHT_ACTION, SingleActionActivity.WEIGHT_ACTION);

            Intent addExerciseIntent = new Intent(context, SingleActionActivity.class);
            addExerciseIntent.putExtra(SingleActionActivity.EXERCISE_ACTION, SingleActionActivity.EXERCISE_ACTION);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setOnClickPendingIntent(R.id.widget_add_weight_button, pendingIntent.getActivity(context, 0, addWeightIntent, 0));
            remoteViews.setOnClickPendingIntent(R.id.widget_add_exercise_button, pendingIntent.getActivity(context, 1, addExerciseIntent, 0));

//            remoteViews.setRemoteAdapter(R.id.widget_list, intent);
//            remoteViews.setEmptyView(R.id.widget_list, R.id.no_exercises);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
        }
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
