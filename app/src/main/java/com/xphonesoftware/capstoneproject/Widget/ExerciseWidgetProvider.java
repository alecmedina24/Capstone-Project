package com.xphonesoftware.capstoneproject.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xphonesoftware.capstoneproject.R;

/**
 * Created by alecmedina on 5/5/16.
 */
public class ExerciseWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY = "exercisewidgetproviderids";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, ExerciseWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            remoteViews.setRemoteAdapter(R.id.widget_list, intent);
            remoteViews.setEmptyView(R.id.widget_list, R.id.no_exercises);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
