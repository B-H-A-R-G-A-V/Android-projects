package com.example.tres;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMG_REQ=1;
    private EditText fileName;
    private Button choose,upload;
    private TextView showUploads;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;


    private StorageTask task;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileName=findViewById(R.id.fileName);
        choose=findViewById(R.id.chooseFile);
        imageView=findViewById(R.id.fileImage);
        upload=findViewById(R.id.fileUpload);
        showUploads=findViewById(R.id.showUploads);
        progressBar=findViewById(R.id.progressBar);

        storageReference=FirebaseStorage.getInstance().getReference("ours");
        databaseReference= FirebaseDatabase.getInstance().getReference("ours");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task!=null && task.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
            }
        });
        showUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImageActivity.class);
                startActivity(intent);

            }
        });
    }

    private String fileUri(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if(imageUri!=null){
            StorageReference file=storageReference.child(System.currentTimeMillis()+","+fileUri(imageUri));
            task=file.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(getApplicationContext(),"Upload successful",Toast.LENGTH_SHORT).show();
                    file.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        Upload upload = new Upload(fileName.getText().toString().trim(), url);
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(upload);
                    });
                    imageView.setImageResource(0);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMG_REQ && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }
}