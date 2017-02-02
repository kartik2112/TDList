package com.sk.tdlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {


    private List<String> mainList=new Vector<String>();
    private ListView mainListView=null;
    private ArrayAdapter ad=null;
    private EditText addTaskEditText=null;
    private Button addTaskButton=null;
    private SQLiteDatabase sqlDB=null;
    private final int REQUEST_CODE_DELETE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlDB=openOrCreateDatabase("dbKartik12#4",MODE_PRIVATE, null);

        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Task varchar(50) PRIMARY KEY)");

        Cursor existingTasks=sqlDB.rawQuery("SELECT * FROM ToDoList ORDER BY Task",null);

        Toast.makeText(this,"Click on task to delete it!",Toast.LENGTH_SHORT).show();

        if(existingTasks!=null){
            if(existingTasks.moveToFirst()){
                do{
                    mainList.add(existingTasks.getString(existingTasks.getColumnIndex("Task")));
                }while (existingTasks.moveToNext());
            }
        }

        mainListView=(ListView) findViewById(R.id.MainListView);
        ad=new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,mainList);

        if(mainListView!=null){
            mainListView.setAdapter(ad);
        }

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String delElem=mainList.remove(position);
                ad.notifyDataSetChanged();
                final BaseTransientBottomBar.BaseCallback bc=new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        sqlDB.execSQL("DELETE FROM ToDoList WHERE Task='"+delElem+"'");
                    }
                };
                final Snackbar deleteSB=Snackbar.make(view,"Task deleted",Snackbar.LENGTH_LONG);
                deleteSB.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainList.add(position,delElem);
                        ad.notifyDataSetChanged();
                        deleteSB.removeCallback(bc);
                    }
                });
                deleteSB.setActionTextColor(getResources().getColor(R.color.colorAccent));

                deleteSB.addCallback(bc);

                deleteSB.show();

            }
        });


        addTaskEditText=(EditText) findViewById(R.id.AddTaskEditText);
        addTaskButton=(Button) findViewById(R.id.AddTaskButton);


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addTaskEditText.getText().toString().equals("")){
                    sqlDB.execSQL("INSERT INTO ToDoList VALUES('"+addTaskEditText.getText().toString()+"')");
                    mainList.add(addTaskEditText.getText().toString());
                    addTaskEditText.setText("");
                    ad.notifyDataSetChanged();
                }
            }
        });


    }
}
