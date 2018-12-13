package com.example.android.notepad;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {
    private UserDao mUserDao;
    private User mUser;

    private NoteDao mNoteDao;
    private LiveData<List<NoteEntry>> mAllNotes;

    NoteRepository(Application application) {
        NotePadDatabase db = NotePadDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
        mUserDao = db.userDao();
    }

    LiveData<List<NoteEntry>> getAllNotes() {
        return mAllNotes;
    }

    public void insertUser (User user) {
        new NoteRepository.insertUserAsyncTask(mUserDao).execute(user);
    }

    public void deleteUser()  {
        new deleteUserAsyncTask(mUserDao).execute();
    }

    public void insertNote (NoteEntry note) {
        new insertNoteAsyncTask(mNoteDao).execute(note);
    }

    public void updateNote(NoteEntry note)  {
        new updateNoteAsyncTask(mNoteDao).execute(note);
    }

    public void deleteNote(NoteEntry note)  {
        new deleteNoteAsyncTask(mNoteDao).execute(note);
    }
    public void deleteAllNotes()  {
        new deleteAllNotesAsyncTask(mNoteDao).execute();
    }

    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.insert(users[0]);
            return null;
        }
    }

    private static class deleteUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertNoteAsyncTask extends AsyncTask<NoteEntry, Void, Void> {

        private NoteDao mAsyncTaskDao;

        insertNoteAsyncTask(NoteDao dao) {
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

    private static class deleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao mAsyncTaskDao;

        deleteAllNotesAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
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
