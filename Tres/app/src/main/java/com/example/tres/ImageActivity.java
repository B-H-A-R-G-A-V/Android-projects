package com.example.tres;

import android.app.DownloadManager;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    List<Upload> uploads;
    private DatabaseReference databaseReference;
    ProgressBar progressBar;
    ImageAdapter adapter;
    private ValueEventListener valueEventListener;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        recyclerView=findViewById(R.id.recList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storage=FirebaseStorage.getInstance();

        progressBar=findViewById(R.id.progressCircle);
        uploads=new ArrayList<>();
        adapter=new ImageAdapter(ImageActivity.this,uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ImageActivity.this);
        databaseReference= FirebaseDatabase.getInstance().getReference("ours");

        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    Upload upload=postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploads.add(upload);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Database error : "+error.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private class MyTask extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Download will start soon",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(strings[0]));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("FirebaseEx");
            request.setDescription("Downloading Image...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());
            DownloadManager manager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getApplicationContext(), "" + uploads.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadClick(int position) {
        String Url = uploads.get(position).getImageUrl();
        if (Url != null){
            MyTask task=new MyTask();
            task.execute(Url);
        }
        else {
            Toast.makeText(getApplicationContext(),"No file to download",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem=uploads.get(position);
        String selectedKey=selectedItem.getKey();

        StorageReference reference=storage.getReferenceFromUrl(selectedItem.getImageUrl());
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}