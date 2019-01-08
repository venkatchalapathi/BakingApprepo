package com.example.venky.bakingapp.Others;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.venky.bakingapp.Activity.MainActivity;
import com.example.venky.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class Baking_App_widgetInfo extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(ProjectConstants.BAKING_APP_SHARED_PREFERENCES_KEY,0);
        String text = sharedPreferences.getString(ProjectConstants.SP_TEXT_INGRED_KEY,"DATA NOT FOUND");
        String recipe_name = sharedPreferences.getString(ProjectConstants.RECIPE_NAME_KEY,"RECIPE_NAME NOT AVAILABLE");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking__app_widget_info);
        views.setTextViewText(R.id.appwidget_title, recipe_name+"\n"+text);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.widget_host,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


}

