package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText id,pass;
    MyDatabaseClass myData;
    MyAdapter adapter;
    RecyclerView r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1=(RecyclerView)findViewById(R.id.rec1);
        id=(EditText) findViewById(R.id.yourId);
        pass=(EditText)findViewById(R.id.pass);
        myData=new MyDatabaseClass(this);
    }

    public void doSave(View view) {
        myData.insert(id.getText().toString(),pass.getText().toString());
    }

    public void doShow(View view) {
        ArrayList<StringBuilder>data=myData.show();
        adapter=new MyAdapter(this,data);
        r1.setAdapter(adapter);
        r1.setLayoutManager(new LinearLayoutManager(this));
    }
}
