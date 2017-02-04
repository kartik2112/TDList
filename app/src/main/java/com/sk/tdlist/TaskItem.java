package com.sk.tdlist;

import java.util.Calendar;

/**
 * Created by karti on 02-02-2017.
 */

// For future implementation.
// Insignificant for current implementations

public class TaskItem {
    String Task;
    boolean status;
    String deadlineDate;

    TaskItem(String Task,boolean status,String deadlineDate){
        this.Task=Task;
        this.status=status;
        this.deadlineDate=deadlineDate;
    }

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
        String parts[] = deadlineDate.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }



    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }


}
