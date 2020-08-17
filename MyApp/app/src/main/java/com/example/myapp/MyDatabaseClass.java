package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;


public class MyDatabaseClass extends SQLiteOpenHelper {

    static final private String DB_NAME="id_pass1";
    static final private String DB_TABLE="persn";
    static final private int DB_VER=1;
    Context ctx;
    SQLiteDatabase sqLiteDatabase;

    public MyDatabaseClass(Context ct){
        super(ct, DB_NAME, null, DB_VER);
        ctx=ct;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DB_TABLE+" (_Sno integer primary key autoincrement,Id varchar(100),Pass varchar(100));");
        Log.i("data","Success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insert(String s1,String s2){
        sqLiteDatabase=getWritableDatabase();

        sqLiteDatabase.execSQL("insert into "+DB_TABLE+" (Id,Pass) values('"+s1+"','"+s2+"');");
        Toast.makeText(ctx,"Data Saved",Toast.LENGTH_SHORT).show();
    }
    public ArrayList<StringBuilder> show()
    {
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor =sqLiteDatabase.rawQuery("Select * from "+DB_TABLE,null);
        StringBuilder sb=new StringBuilder();
        ArrayList<StringBuilder> data=new ArrayList<>();
        while (cursor.moveToNext())
        {
            int s=cursor.getInt(0);
            String s1=cursor.getString(1);
            String s2=cursor.getString(2);
            sb.append(s+". "+s1+" = "+s2+"\n");
            data.add(sb);
        }
        return data;
    }
}
