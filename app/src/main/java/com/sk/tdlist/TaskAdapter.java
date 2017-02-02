package com.sk.tdlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by karti on 02-02-2017.
 * Code is almost AS IS written on
 * http://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 */

public class TaskAdapter extends ArrayAdapter<String> {
    private ArrayList<String> dataSet;
    private SQLiteDatabase sqlDB;

    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView taskName;
        ImageView img;
        CheckBox cbox;
    }


    public TaskAdapter(ArrayList<String> data, Context context,SQLiteDatabase sqlDB) {
        super(context, R.layout.task_list_row_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.sqlDB=sqlDB;
    }

    private int lastPosition = -1;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        super.getView(position, convertView, parent);
        // Get the data item for this position
        final String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_list_row_item, parent, false);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.txtView);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.delIcon);
            viewHolder.cbox = (CheckBox) convertView.findViewById(R.id.chckBox);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.taskName.setText(dataModel);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * On clicking delete ImageView, this function is invoked
                 * Code for adding Task is in MainActivity.java
                 */
                final int positionOfTaskInList=dataSet.indexOf(dataModel);
                final String delElem=dataSet.remove(positionOfTaskInList);
                notifyDataSetChanged();
                sqlDB.execSQL("DELETE FROM ToDoList WHERE Task='"+delElem+"'");


                /**
                 * Code for Snackbar display and UNDO functionality
                 */
                final Snackbar deleteSB=Snackbar.make(view,"Task deleted",Snackbar.LENGTH_LONG);
                deleteSB.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataSet.add(positionOfTaskInList,delElem);
                        notifyDataSetChanged();
                        sqlDB.execSQL("INSERT INTO ToDoList VALUES('"+delElem+"')");
                        //deleteSB.removeCallback(bc);
                    }
                });
                deleteSB.setActionTextColor(getContext().getResources().getColor(R.color.colorAccent));
                deleteSB.show();
            }
        });
        //viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }


    /*@Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }*/



}

