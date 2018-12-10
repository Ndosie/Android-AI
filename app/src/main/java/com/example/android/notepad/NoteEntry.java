package com.example.android.notepad;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "notes")
public class NoteEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "content")
    private String mContent;

    @NonNull
    @ColumnInfo(name = "timestamp")
    private long mTimestamp;

    public NoteEntry(@NonNull String content, @NonNull long timestamp) {
        this.mContent = content;
        this.mTimestamp = timestamp;
    }

    @Ignore
    public NoteEntry(@NonNull int id, @NonNull String content, @NonNull long timestamp) {
        this.mId = id;
        this.mContent = content;
        this.mTimestamp = timestamp;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public int getId() {return this.mId;}
    public String getContent(){return this.mContent;}
    public long getTimestamp(){return this.mTimestamp;}

}
