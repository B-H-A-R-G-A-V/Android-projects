package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapt extends RecyclerView.Adapter<MyAdapt.MyHold> {
    ArrayList<StringBuilder> mydata;
    Context ct;
    public MyAdapt(Context ctx, ArrayList<StringBuilder> d1){
        ct=ctx;
        mydata=d1;
    }
    @NonNull
    @Override
    public MyAdapt.MyHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(ct);
        View view=inflater.inflate(R.layout.my_layout,parent,false);
        return new MyHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapt.MyHold holder, int position) {
        holder.textView.setText(mydata.get(position));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyHold extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHold(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.t1);
        }
    }
}
