package com.sk.tdlist;

import android.app.Activity;
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
import android.widget.Toast;

/**
 * Created by karti on 04-02-2017.
 */

public class CalendarDialogBoxFragment extends DialogFragment {
    TaskItem dataModel;
    SQLiteDatabase sqlDB;
    String oldDate;



    /**
     * For this part, the reference used is
     * https://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog
     */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog,TaskItem dataModel);
        public void onDialogNeutralClick(DialogFragment dialog,TaskItem dataModel);
        public void onDialogNegativeClick(DialogFragment dialog,TaskItem dataModel);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }








    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dataModel=getArguments().getParcelable("dataModel");

        sqlDB=getContext().openOrCreateDatabase("dbKartik12#4",getContext().MODE_PRIVATE, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());


        //Get Layout inflater
        LayoutInflater inflater=getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflatedView=inflater.inflate(R.layout.calendar_module,null);
        builder.setView(inflatedView);

        final CalendarView calendarView=(CalendarView) inflatedView.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {

                /**
                 * Here month ranges from 0 to 11. Thus we need to add 1 to month value
                 */
                sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate='"+day+"/"+(month+1)+"/"+year+"' WHERE Task='"+dataModel.getTask()+"'");
                dataModel.setDeadlineDate(day+"/"+(month+1)+"/"+year);


                /**
                 * Calendar handling part
                 */
                int noDeleted=RealCalendarModule.deleteEvent(getContext().getContentResolver(),dataModel.getTask());
                Toast.makeText(getContext(),noDeleted+" events deleted from Calendar",Toast.LENGTH_SHORT).show();
                RealCalendarModule.addEvent(getContext(),dataModel.getTask(),dataModel.getDeadlineDateInMillis());


                mListener.onDialogPositiveClick(CalendarDialogBoxFragment.this,dataModel);
            }
        });

        String fetchedDeadlineDate=getArguments().getString("deadlineDate");
        if(fetchedDeadlineDate==null){
            oldDate=null;
        }
        else{
            oldDate=new String(fetchedDeadlineDate);
        }

        if(fetchedDeadlineDate!=null){
            calendarView.setDate(TaskItem.getDeadlineDateInMillis(fetchedDeadlineDate));
        }

        builder.setPositiveButton("Set Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /**
                 * This part is intentionally commented because the new date setting is handled by the onDateChangeListener in a better way
                 */
                /*String newDate=dataModel.setDeadlineDateFromMillis(calendarView.getDate());
                sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate='"+newDate+"' WHERE Task='"+dataModel.getTask()+"'");
                mListener.onDialogPositiveClick(CalendarDialogBoxFragment.this,dataModel);*/
            }
        });

        builder.setNeutralButton("Clear Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataModel.setDeadlineDate(null);
                sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate=NULL WHERE Task='"+dataModel.getTask()+"'");


                /**
                 * Deleting existing events from calendar
                 */
                int noDeleted=RealCalendarModule.deleteEvent(getContext().getContentResolver(),dataModel.getTask());
                Toast.makeText(getContext(),noDeleted+" events deleted from Calendar",Toast.LENGTH_SHORT).show();



                getDialog().dismiss();
                mListener.onDialogNeutralClick(CalendarDialogBoxFragment.this,dataModel);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(oldDate==null){
                    if(dataModel.getDeadlineDate()!=null){
                        dataModel.setDeadlineDate(oldDate);
                        sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate=NULL WHERE Task='"+dataModel.getTask()+"'");


                        /**
                         * Deleting existing events from calendar
                         */
                        int noDeleted=RealCalendarModule.deleteEvent(getContext().getContentResolver(),dataModel.getTask());
                        Toast.makeText(getContext(),noDeleted+" events deleted from Calendar",Toast.LENGTH_SHORT).show();


                        getDialog().dismiss();
                        mListener.onDialogNegativeClick(CalendarDialogBoxFragment.this,dataModel);
                    }
                }
                else{
                    if(!oldDate.equals(dataModel.getDeadlineDate())){
                        dataModel.setDeadlineDate(oldDate);
                        sqlDB.execSQL("UPDATE ToDoList SET DeadlineDate='"+oldDate+"' WHERE Task='"+dataModel.getTask()+"'");



                        /**
                         * Calendar handling part
                         */
                        int noDeleted=RealCalendarModule.deleteEvent(getContext().getContentResolver(),dataModel.getTask());
                        Toast.makeText(getContext(),noDeleted+" events deleted from Calendar",Toast.LENGTH_SHORT).show();
                        RealCalendarModule.addEvent(getContext(),dataModel.getTask(),TaskItem.getDeadlineDateInMillis(oldDate));


                        getDialog().dismiss();
                        mListener.onDialogNegativeClick(CalendarDialogBoxFragment.this,dataModel);
                    }
                }

                getDialog().cancel();
            }
        });

        return builder.create();
    }

}
