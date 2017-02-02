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

        sqlDB=openOrCreateDatabase("dbKartik12#4",MODE_PRIVATE, null);

        //sqlDB.execSQL("DROP TABLE ToDoList");

        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Task varchar(50) PRIMARY KEY,Status char(5))");



        Toast.makeText(this,"Click on task to delete it!",Toast.LENGTH_SHORT).show();

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

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final TaskItem delElem=mainList.remove(position);
                ad.notifyDataSetChanged();
                sqlDB.execSQL("DELETE FROM ToDoList WHERE Task='"+delElem.getTask()+"'");

                final Snackbar deleteSB=Snackbar.make(view,"Task deleted",Snackbar.LENGTH_LONG);
                deleteSB.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainList.add(position,delElem);
                        ad.notifyDataSetChanged();
                        sqlDB.execSQL("INSERT INTO ToDoList(Task,Status) VALUES('"+delElem.getTask()+"','"+delElem.getStatus()+"')");
                    }
                });
                deleteSB.setActionTextColor(getResources().getColor(R.color.colorAccent));
                deleteSB.show();

            }
        });


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
