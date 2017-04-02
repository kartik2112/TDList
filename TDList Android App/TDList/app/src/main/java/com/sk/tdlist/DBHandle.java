package com.sk.tdlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by karti on 03-02-2017.
 */

public class DBHandle {
    public static SQLiteDatabase createDBTables(Context context){
        SQLiteDatabase sqlDB=context.openOrCreateDatabase("dbKartik12#4",context.MODE_PRIVATE, null);

        //sqlDB.execSQL("DROP TABLE ToDoList");

        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Task varchar(200) PRIMARY KEY,Status char(5),DeadlineDate varchar(10))");

        return sqlDB;
    }
}
