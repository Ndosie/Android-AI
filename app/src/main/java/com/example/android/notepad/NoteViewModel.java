package com.example.android.notepad;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;

    private LiveData<List<NoteEntry>> mAllNotes;
    private User mUser;

    public NoteViewModel (Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mAllNotes = mNoteRepository.getAllNotes();
    }

    LiveData<List<NoteEntry>> getAllNotes() { return mAllNotes; }

    public void insertNote(NoteEntry note) { mNoteRepository.insertNote(note); }

    public void insertUser(User user) { mNoteRepository.insertUser(user); }

    public void deleteNote(NoteEntry note) {mNoteRepository.deleteNote(note);}
    public void deleteAllNotes() {mNoteRepository.deleteAllNotes();}

    public void update(NoteEntry note) {
        mNoteRepository.updateNote(note);
    }

    public void deleteUser(){mNoteRepository.deleteUser();}
}
