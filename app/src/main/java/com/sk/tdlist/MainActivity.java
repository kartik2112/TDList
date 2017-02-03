package com.sk.tdlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {


    private List<TaskItem> mainList=new ArrayList<TaskItem>();
    private ListView mainListView=null;
    private ArrayAdapter ad=null;
    private EditText addTaskEditText=null;
    private Button addTaskButton=null;
    private SQLiteDatabase sqlDB=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlDB=DBHandle.createDBTables(this);

        //Toast.makeText(this,"Click on task to delete it!",Toast.LENGTH_SHORT).show();

        /**
         * This section retrieves data from DB and adds it to ArrayList @existingTasks
         */
        Cursor existingTasks=sqlDB.rawQuery("SELECT * FROM ToDoList",null);
        if(existingTasks!=null){
            if(existingTasks.moveToFirst()){
                do{
                    mainList.add( new TaskItem( existingTasks.getString( existingTasks.getColumnIndex("Task")) , Boolean.valueOf( existingTasks.getString(existingTasks.getColumnIndex("Status")) ) ) );
                }while (existingTasks.moveToNext());
            }
        }

        mainListView=(ListView) findViewById(R.id.MainListView);
        ad=new TaskAdapter((ArrayList) mainList,getApplicationContext(),sqlDB);

        if(mainListView!=null){
            mainListView.setAdapter(ad);
        }



        addTaskEditText=(EditText) findViewById(R.id.AddTaskEditText);
        addTaskButton=(Button) findViewById(R.id.AddTaskButton);


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addTaskEditText.getText().toString().equals("")){
                    try{
                        sqlDB.execSQL("INSERT INTO ToDoList VALUES('"+addTaskEditText.getText().toString()+"','false')");
                        mainList.add( new TaskItem( addTaskEditText.getText().toString() , false ) );
                        addTaskEditText.setText("");
                        ad.notifyDataSetChanged();
                    }
                    catch (SQLiteException e){
                        Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
