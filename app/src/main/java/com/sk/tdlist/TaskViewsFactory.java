package com.sk.tdlist;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karti on 03-02-2017.
 */

public class TaskViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    SQLiteDatabase sqlDB;
    private static final List<TaskItem> mainList=new ArrayList<TaskItem>();
    private Context ctxt=null;
    private int appWidgetId;

    public TaskViewsFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        Log.d("taskViewsFactory","Constructor called");
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        sqlDB=DBHandle.createDBTables(ctxt);
        /**
         * This section retrieves data from DB and adds it to ArrayList @existingTasks
         */
        Cursor existingTasks=sqlDB.rawQuery("SELECT * FROM ToDoList where Status='false' order by Task",null);
        if(existingTasks!=null){
            if(existingTasks.moveToFirst()){
                do{
                    mainList.add( new TaskItem( existingTasks.getString( existingTasks.getColumnIndex("Task")) , Boolean.valueOf( existingTasks.getString(existingTasks.getColumnIndex("Status")) ) ) );
                }while (existingTasks.moveToNext());
            }
        }
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(mainList.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_tasklist_row);

        Log.d("taskViewsFactory","getViewAt called, data: "+mainList.get(position).getTask()+" "+row+" "+R.id.textView);

        row.setTextViewText(R.id.textView, mainList.get(position).getTask());

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(WidgetHandler.EXTRA_WORD, mainList.get(position).getTask());
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.textView, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}
