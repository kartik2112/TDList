package com.sk.tdlist;

/**
 * Created by karti on 02-02-2017.
 */

// For future implementation.
// Insignificant for current implementations

public class TaskItem {
    String Task;
    boolean status;
    TaskItem(String Task,boolean status){
        this.Task=Task;
        this.status=status;
    }

    public String getTask() {
        return Task;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
