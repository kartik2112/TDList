package com.sk.tdlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by karti on 02-02-2017.
 */

// For future implementation.
// Insignificant for current implementations

public class TaskItem implements Parcelable{
    String Task;
    boolean status;
    String deadlineDate;

    TaskItem(String Task,boolean status,String deadlineDate){
        this.Task=Task;
        this.status=status;
        this.deadlineDate=deadlineDate;
    }

    TaskItem(Parcel in) {
        Task = in.readString();
        status = in.readByte() != 0;
        deadlineDate = in.readString();
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    public String getTask() {
        return Task;
    }

    public boolean getStatus() {
        return status;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public long getDeadlineDateInMillis() {
        return getDeadlineDateInMillis(this.deadlineDate);
    }

    public static long getDeadlineDateInMillis(String deadlineDate) {
        String parts[] = deadlineDate.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();

        /**
         * UTC Timezone is required to be set for proper synchronization between obtaining time in millis for a date
         * and setting the reminder on the exact same date in the calendar
         */
        //calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));


        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTimeInMillis();
    }



    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String setDeadlineDateFromMillis(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(date));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.Task,
                String.valueOf(this.status),
                this.deadlineDate});
    }

    public static int findTaskPosition(ArrayList<TaskItem> mainList,String taskName){
        for(int i=0;i<mainList.size();i++){
            if(mainList.get(i).getTask()==taskName){
                return i;
            }
        }
        return -1;
    }
}
