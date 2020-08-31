package com.example.diary;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<notes>> allNotes;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository=new NoteRepository(application);
        allNotes=repository.getAllNotes();
    }
    public void insert(notes note){
        repository.insert(note);
    }
    public void update(notes note){
        repository.update(note);
    }
    public void Delete(notes note){
        repository.delete(note);
    }
    public void deleteAll(){
        repository.deleteAll();
    }
    public LiveData<List<notes>> getAllNotes(){
        return allNotes;
    }
}
