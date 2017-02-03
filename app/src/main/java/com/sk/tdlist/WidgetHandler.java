package com.sk.tdlist;

import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karti on 03-02-2017.
 */

public class WidgetHandler extends AppWidgetProvider {
    public static String EXTRA_WORD=
            "com.commonsware.android.appwidget.lorem.WORD";

    private List<TaskItem> mainList=new ArrayList<TaskItem>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            Log.d("WidgetHandler","loop executing");
            Intent svcIntent=new Intent(context, WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget=new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.MainListView,
                    svcIntent);

            Intent clickIntent=new Intent(context, TaskViewActivity.class);
            PendingIntent clickPI=PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.MainListView, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);

            /*int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            SQLiteDatabase sqlDB=context.openOrCreateDatabase("dbKartik12#4",context.MODE_PRIVATE, null);

            sqlDB.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Task varchar(50) PRIMARY KEY,Status char(5))");

            /**
             * This section retrieves data from DB and adds it to ArrayList @existingTasks

            Cursor existingTasks=sqlDB.rawQuery("SELECT * FROM ToDoList",null);
            mainList=new ArrayList<TaskItem>();
            if(existingTasks!=null){
                if(existingTasks.moveToFirst()){
                    do{
                        mainList.add( new TaskItem( existingTasks.getString( existingTasks.getColumnIndex("Task")) , Boolean.valueOf( existingTasks.getString(existingTasks.getColumnIndex("Status")) ) ) );
                    }while (existingTasks.moveToNext());
                }
            }


            ListView mainListView=(ListView) views.findViewById(R.id.MainListView);

            ArrayAdapter ad=new TaskAdapter((ArrayList) mainList,context,sqlDB);

            if(mainListView!=null){
                mainListView.setAdapter(ad);
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);*/
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
