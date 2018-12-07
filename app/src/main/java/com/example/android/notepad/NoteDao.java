package com.example.android.notepad;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntry note);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * from notes ORDER BY timestamp DESC")
    List<NoteEntry> getAllNotes();
}
