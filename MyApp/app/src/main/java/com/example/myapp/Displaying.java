package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Displaying extends AppCompatActivity {

    MyDatabaseClass mydata2;
    MyAdapt myAdapt;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_displaying);
        recyclerView=(RecyclerView)findViewById(R.id.rec);
    }

    public void doDis(View view) {
        mydata2.show1();

    }
}
