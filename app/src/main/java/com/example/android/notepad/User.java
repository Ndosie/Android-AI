package com.example.android.notepad;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    @NonNull
    @ColumnInfo(name = "displayName")
    private String mDisplayName;

    @NonNull
    @ColumnInfo(name = "photoUrl")
    private String mPhotoUrl;

    public User(@NonNull String id, @NonNull String displayName, @NonNull String photoUrl) {
        this.mId = id;
        this.mDisplayName = displayName;
        this.mPhotoUrl = photoUrl;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getDisplayName() {
        return mDisplayName;
    }

    @NonNull
    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
