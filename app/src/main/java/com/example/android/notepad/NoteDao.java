package com.example.android.notepad;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntry note);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * from notes ORDER BY timestamp DESC")
    LiveData<List<NoteEntry>> getAllNotes();

    @Query("SELECT * from notes where id = :id LIMIT 1")
    NoteEntry getNoteById(int id);

    @Delete
    void deleteNote(NoteEntry note);

    @Update
    void update(NoteEntry note);
}
