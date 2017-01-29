package com.sk.tdlist;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class MainActivity extends Activity {

    private String tempArray[]={"Do Android", "Do Hierarchy of Android","Do PCS"};
    private List<String> mainList=new Vector<String>(Arrays.asList(tempArray));
    private ListView mainListView=null;
    private ArrayAdapter ad=null;
    private EditText addTaskEditText=null;
    private Button addTaskButton=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListView=(ListView) findViewById(R.id.MainListView);
        ad=new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,mainList);

        if(mainListView!=null){
            mainListView.setAdapter(ad);
        }

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainList.remove(position);
                ad.notifyDataSetChanged();
            }
        });

        addTaskEditText=(EditText) findViewById(R.id.AddTaskEditText);
        addTaskButton=(Button) findViewById(R.id.AddTaskButton);

        addTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("")){
                    addTaskButton.setEnabled(false);
                }
                else{
                    addTaskButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addTaskEditText.getText().toString().equals("")){
                    mainList.add(addTaskEditText.getText().toString());
                    addTaskEditText.setText("");
                    addTaskEditText.clearFocus();
                }
            }
        });


    }
}
