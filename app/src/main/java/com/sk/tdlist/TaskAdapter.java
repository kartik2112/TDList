package com.sk.tdlist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class TaskAdapter extends ArrayAdapter<TaskItem> {
    private ArrayList<TaskItem> dataSet;
    private SQLiteDatabase sqlDB;
    private FragmentManager fm;

    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView taskName;
        ImageView delIcon;
        ImageView calendarIcon;
        CheckBox cbox;
        TextView dateView;
    }


    public TaskAdapter(ArrayList<TaskItem> data, Context context,SQLiteDatabase sqlDB,FragmentManager fm) {
        super(context, R.layout.task_list_row_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.sqlDB=sqlDB;
        this.fm=fm;
    }

    private int lastPosition = -1;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        super.getView(position, convertView, parent);
        // Get the data item for this position
        final TaskItem dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_list_row_item, parent, false);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.txtView);
            viewHolder.delIcon = (ImageView) convertView.findViewById(R.id.delIcon);
            viewHolder.cbox = (CheckBox) convertView.findViewById(R.id.chckBox);
            viewHolder.calendarIcon=(ImageView) convertView.findViewById(R.id.setDate);
            viewHolder.dateView=(TextView) convertView.findViewById(R.id.dateView);

            result=convertView;

            convertView.setTag(viewHolder); //This is currently not used. This will be useful while reusing listeners
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.taskName.setText(dataModel.getTask());
        if(dataModel.getDeadlineDate()==null){
            viewHolder.dateView.setVisibility(View.GONE);
        }
        else{
            viewHolder.dateView.setText(dataModel.getDeadlineDate());
        }
        viewHolder.cbox.setChecked(dataModel.getStatus());

        viewHolder.cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.cbox.isChecked()){
                    dataModel.setStatus(true);
                    sqlDB.execSQL("UPDATE ToDoList SET Status='true' WHERE Task='"+dataModel.getTask()+"'");
                }
                else{
                    dataModel.setStatus(false);
                    sqlDB.execSQL("UPDATE ToDoList SET Status='false' WHERE Task='"+dataModel.getTask()+"'");
                }
            }
        });

        viewHolder.delIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * On clicking delete ImageView, this function is invoked
                 * Code for adding Task is in MainActivity.java
                 */
                final int positionOfTaskInList=dataSet.indexOf(dataModel);
                final TaskItem delElem=dataSet.remove(positionOfTaskInList);
                notifyDataSetChanged();
                sqlDB.execSQL("DELETE FROM ToDoList WHERE Task='"+delElem.getTask()+"'");


                /**
                 * Code for Snackbar display and UNDO functionality
                 */
                final Snackbar deleteSB=Snackbar.make(view,"Task deleted",Snackbar.LENGTH_LONG);
                deleteSB.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataSet.add(positionOfTaskInList,delElem);
                        notifyDataSetChanged();
                        sqlDB.execSQL("INSERT INTO ToDoList VALUES('"+delElem.getTask()+"','"+delElem.getStatus()+"','"+delElem.getDeadlineDate()+"')");
                        //deleteSB.removeCallback(bc);
                    }
                });
                deleteSB.setActionTextColor(getContext().getResources().getColor(R.color.colorAccent));
                deleteSB.show();
            }
        });

        viewHolder.calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * This part will open the dialog box containing calendar_module.xml layout inflated and
                 * Set Date, Clear Date and Cancel buttons
                 */
                Bundle b=new Bundle();
                b.putParcelable("dataModel",dataModel);
                b.putString("deadlineDate",dataModel.getDeadlineDate());

                CalendarDialogBoxFragment dF=new CalendarDialogBoxFragment();
                dF.setArguments(b);
                dF.show(fm,"CalendarFragment");
                Log.d("TaskAdapter","called DialogFragment");
            }
        });
        //viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }

}

