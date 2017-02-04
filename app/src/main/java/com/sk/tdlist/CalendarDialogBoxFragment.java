package com.sk.tdlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

/**
 * Created by karti on 04-02-2017.
 */

public class CalendarDialogBoxFragment extends DialogFragment {
    TaskItem dataModel;
    SQLiteDatabase sqlDB;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dataModel=getArguments().getParcelable("dataModel");
        sqlDB=getContext().openOrCreateDatabase("dbKartik12#4",getContext().MODE_PRIVATE, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle("Set Deadline Date");

        //Get Layout inflater
        LayoutInflater inflater=getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflatedView=inflater.inflate(R.layout.calendar_module,null);
        builder.setView(inflatedView);

        final CalendarView calendarView=(CalendarView) inflatedView.findViewById(R.id.calendarView);

        if(dataModel.getDeadlineDate()!=null){
            calendarView.setDate(dataModel.getDeadlineDateInMillis());
        }

        builder.setPositiveButton("Set Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newDate=dataModel.setDeadlineDateFromMillis(calendarView.getDate());
                sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate='"+newDate+"' WHERE Task='"+dataModel.getTask()+"'");
            }
        });

        builder.setNeutralButton("Clear Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataModel.setDeadlineDate(null);
                sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate=NULL WHERE Task='"+dataModel.getTask()+"'");

                getDialog().dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }

}
