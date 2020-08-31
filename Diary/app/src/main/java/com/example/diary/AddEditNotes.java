package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNotes extends AppCompatActivity {
    EditText editTextTitle,editTextDesc;
    NumberPicker numberPicker;
    public static final String EXTRA_ID="com.example.diary.EXTRA_ID";
    public static final String EXTRA_TITLE="com.example.diary.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION="com.example.diary.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.example.diary.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        editTextTitle=findViewById(R.id.edit_title);
        editTextDesc=findViewById(R.id.edit_Desc);

        numberPicker=findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent=getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDesc.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }
        else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title=editTextTitle.getText().toString();
        String desc=editTextDesc.getText().toString();
        int priority=numberPicker.getValue();

        if (title.trim().isEmpty() || desc.trim().isEmpty()){
            Toast.makeText(this,"Please enter Title & Description",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data=new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,desc);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if(id!=-1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,data);
        finish();


    }
}
