package com.example.android.notepad;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<NoteEntry>> mAllNotes;

    NoteRepository(Application application) {
        NotePadDatabase db = NotePadDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    LiveData<List<NoteEntry>> getAllNotes() {
        return mAllNotes;
    }

    public void insert (NoteEntry note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    public void update(NoteEntry note)  {
        new updateNoteAsyncTask(mNoteDao).execute(note);
    }


    public void deleteNote(NoteEntry note)  {
        new deleteNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<NoteEntry, Void, Void> {

        private NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NoteEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteNoteAsyncTask extends AsyncTask<NoteEntry, Void, Void> {
        private NoteDao mAsyncTaskDao;

        deleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NoteEntry... params) {
            mAsyncTaskDao.deleteNote(params[0]);
            return null;
        }
    }

    private static class updateNoteAsyncTask extends AsyncTask<NoteEntry, Void, Void> {
        private NoteDao mAsyncTaskDao;

        updateNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NoteEntry... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
