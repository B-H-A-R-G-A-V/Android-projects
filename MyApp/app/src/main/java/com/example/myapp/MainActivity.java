package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText note;
    MyDatabaseClass myData;
    MyAdapter adapter;
    RecyclerView r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        r1=(RecyclerView)findViewById(R.id.rec1);
        note=(EditText)findViewById(R.id.note);
        myData=new MyDatabaseClass(this);
    }

    public void doSave(View view) {
        myData.insert(note.getText().toString());
        note.setText(null);
    }

    public void doShow(View view) {
        ArrayList<StringBuilder>data=myData.show();
        adapter=new MyAdapter(this,data);
        r1.setAdapter(adapter);
        r1.setLayoutManager(new LinearLayoutManager(this));
    }
}
