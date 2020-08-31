package com.example.diary;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<notes>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database=NoteDatabase.getInstance(application);
        noteDao=database.noteDao();
        allNotes=noteDao.getAllNotes();
    }

    public void insert(notes note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void update(notes note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(notes note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAll(){
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }
    public LiveData<List<notes>> getAllNotes(){
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<notes,Void,Void>{
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(notes... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<notes,Void,Void>{
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(notes... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<notes,Void,Void>{
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(notes... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
