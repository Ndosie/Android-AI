package com.example.android.notepad;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;

    private LiveData<List<NoteEntry>> mAllNotes;

    public NoteViewModel (Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    LiveData<List<NoteEntry>> getAllNotes() { return mAllNotes; }

    public void insert(NoteEntry note) { mRepository.insert(note); }
}
